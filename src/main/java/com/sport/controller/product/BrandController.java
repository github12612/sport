package com.sport.controller.product;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.bean.product.Brand;
import com.sport.service.product.BrandService;

import cn.itcast.common.page.Pagination;

/**
 * 品牌管理
 * @author chenguanhua
 *
 */
@Controller
@RequestMapping("/brand/")
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	/**
	 * 品牌集合
	 * @return
	 */
	@RequestMapping("list.do")
	public String list(String name,Integer isDisplay ,@RequestParam(value="pageNo",defaultValue="1")Integer pageNo,ModelMap modelMap){
		//parms 为页面展示分页数据
		StringBuilder params=new StringBuilder();
		
		Brand brand =new Brand();
		if(StringUtils.isNotBlank(name)){
			brand.setName(name);
			params.append("name=").append(name);
		}
		
		brand.setPageNo(pageNo);
		
		if(isDisplay !=null){
			brand.setIsDisplay(isDisplay);
			params.append("&").append("isDisplay=").append(isDisplay);
		}else {
			brand.setIsDisplay(1);
			params.append("&").append("isDisplay=").append(1);
		}
		
		Pagination page = brandService.getBrandListWithPage(brand);
		
		//分页展示
		String url="/brand/list.do";
		page.pageView(url, params.toString());
		
		//放进request域
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("name", name);
		modelMap.addAttribute("isDisplay", isDisplay);
		return "brand/list";
	}
	
	/**
	 * 去添加页面
	 * 
	 * @return
	 */
	@RequestMapping("toAdd.do")
	public String toAdd(){
	
		return "brand/add";
	}
	
	/**
	 * 保存
	 * @param bran
	 * @return
	 */
	@RequestMapping("add")
	public String save(Brand brand){
		brandService.addBrand(brand);
		return "redirect:/brand/list.do";
	}
	/**
	 * 删除一个品牌 并解决重定向中文的传递问题
	 * @param id
	 * @param name
	 * @param isDisplay
	 * @return
	 */
	@RequestMapping("delete.do")
	public String delete(Integer id,String name,Integer isDisplay,ModelMap modelMap){
		
		brandService.deleteBrandById(id);
		
		if(StringUtils.isNotBlank(name)){
			modelMap.addAttribute("name",name);
		}
		
		if(isDisplay != null){
			modelMap.addAttribute("isDisplay", isDisplay);
		}
		
		return "redirect:/brand/list.do";
	}
	
	/**
	 * 删除多个品牌 并解决重定向中文的传递问题
	 * @param id
	 * @param name
	 * @param isDisplay
	 * @return
	 */
	@RequestMapping("deletes.do")
	public String deletes(Integer[] ids,String name,Integer isDisplay,ModelMap modelMap){
		
		brandService.deleteBrandByIds(ids);
		
		if(StringUtils.isNotBlank(name)){
			modelMap.addAttribute("name",name);
		}
		
		if(isDisplay != null){
			modelMap.addAttribute("isDisplay", isDisplay);
		}
		
		return "redirect:/brand/list.do";
	}
	
	/**
	 * 去更新页面
	 * @param id
	 * @return
	 */
	@RequestMapping("toEditor.do")
	public String toEditor(Integer id,ModelMap modelMap){
		Brand brand = brandService.getBrandById(id);
		//放进域
		modelMap.addAttribute("brand", brand);
		return "brand/edit";
	}
	
	/**
	 * 更新
	 * @param brand
	 * @return
	 */
	@RequestMapping("update.do")
	public String update(Brand brand,Integer id){
		if(id != null){
			brandService.updateBrandById(brand);
		}
		
		return "redirect:/brand/list.do";
	}
	
}
