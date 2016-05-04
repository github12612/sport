package com.sport.controller.admin.sku;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sport.bean.product.Sku;
import com.sport.common.ResponUtils;
import com.sport.query.product.SkuQuery;
import com.sport.service.product.SkuService;

/**
 * 库存管理
 * @author chenguanhua
 *
 */
@RequestMapping("/sku/")
@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;
	
	@RequestMapping("list.do")
	public String list(Integer productId,String pno,ModelMap modelMap){
		
		//回显编号
		modelMap.addAttribute("pno", pno);
		
		//最小销售单元
		SkuQuery skuQuery=new SkuQuery();
		skuQuery.setProductId(productId);
		
		List<Sku> skuList = skuService.getSkuList(skuQuery);
		modelMap.addAttribute("skuList", skuList);
		
		return "sku/list";
	}
	
	/**
	 * ajax添加
	 * @param sku
	 * @param response
	 */
	@RequestMapping("add.do")
	public void add(Sku sku,HttpServletResponse response){
		
		skuService.updateSkuByKey(sku);
		
		JSONObject json=new JSONObject();
		
		json.put("message", "添加成功");
		ResponUtils.renderJson(response, json.toString());
	}
}
