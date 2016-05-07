package com.sport.controller.front;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.bean.product.Brand;
import com.sport.bean.product.Color;
import com.sport.bean.product.Feature;
import com.sport.bean.product.Product;
import com.sport.bean.product.Sku;
import com.sport.bean.product.Type;
import com.sport.common.session.HttpSessionProvider;
import com.sport.query.product.BrandQuery;
import com.sport.query.product.FeatureQuery;
import com.sport.query.product.ProductQuery;
import com.sport.query.product.TypeQuery;
import com.sport.service.product.BrandService;
import com.sport.service.product.FeatureService;
import com.sport.service.product.ProductService;
import com.sport.service.product.SkuService;
import com.sport.service.product.TypeService;

import cn.itcast.common.page.Pagination;

/**
 * 商品前台页面
 * 
 * @author chenguanhua
 *
 */
@Controller
@RequestMapping("/product/display/")
public class FrontProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private FeatureService featureService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private HttpSessionProvider sessionProvider;
	
	@RequestMapping("list.shtml")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,Integer brandId,String brandName,
						Integer typeId,String typeName,ModelMap modelMap) {

		// 加载商品属性
		FeatureQuery featureQuery = new FeatureQuery();
		List<Feature> featureList = featureService.getFeatureList(featureQuery);
		modelMap.addAttribute("featureList", featureList);
		
		//parms 为页面展示分页数据 下一页...
		StringBuilder params = new StringBuilder();
		//查询条件
		ProductQuery productQuery=new ProductQuery();
		
		//条件TOOD
		//隐藏已选条件
		boolean flag=false;
		//定义查询显示条件
		Map<String ,String>query=new LinkedHashMap<String,String>();
		
		//品牌id
		if(brandId != null){
			productQuery.setBrandId(brandId);
			flag=true;
			modelMap.addAttribute("brandId", brandId);
			modelMap.addAttribute("brandName", brandName);
			query.put("品牌", brandName);
			
			//分页展示
			params.append("&").append("brandId=").append(brandId).append("&brandName=").append(brandName);
		}else{
			// 品牌查询条件
			BrandQuery brandQuery = new BrandQuery();
			// 查询字段
			brandQuery.setFields("id,name");
			// 设置可见
			brandQuery.setIsDisplay(1);
			// 加载品牌
			List<Brand> brandList = brandService.getBrandList(brandQuery);
			modelMap.addAttribute("brandList", brandList);
		}
		
		//类型id
		if(typeId != null){
			productQuery.setTypeId(typeId);
			flag=true;
			modelMap.addAttribute("typeId", typeId);
			modelMap.addAttribute("typeName", typeName);
			query.put("类型", typeName);
			//分页展示
			params.append("&").append("typeId=").append(typeId).append("&typeName=").append(typeName);
		}else{
			// 加载商品类型
			TypeQuery typeQuery = new TypeQuery();
			// 查询字段
			typeQuery.setFields("id,name");
			// 设置可见
			typeQuery.setIsDisplay(1);
			// 设置关联类型id
			typeQuery.setParentId(0);
			// 商品类型集合
			List<Type> typeList = typeService.getTypeList(typeQuery);
			modelMap.addAttribute("typeList", typeList);
		}
		
		modelMap.addAttribute("flag", flag);
		//把查询的值回显页面
		modelMap.addAttribute("query", query);
		
		//设置页码
		productQuery.setPageNo(pageNo);
		//设置页数
		productQuery.setPageSize(Product.FRONT_PAGE_SIZE);
		//设置倒叙
		productQuery.orderbyId(false);
		//加载分页商品
		Pagination page = productService.getProductListWithPage(productQuery);
		
		String url = "/product/display/list.shtml";
		
		// 页面展示
		page.pageView(url, params.toString());
		
		modelMap.addAttribute("page", page);
		
		return "product/product";
	}
	
	/**
	 * 商品详情
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("detail.shtml")
	public String detail(Integer id,ModelMap modelMap){
		//加载商品
		Product product = productService.getProductByKey(id);
		modelMap.addAttribute("product", product);
		
		//根据productid获取对应的
		List<Sku> skuList = skuService.getStock(id);
		modelMap.addAttribute("skuList", skuList);
		
		//去除重复
		List<Color> colors=new ArrayList<Color>();
		
		//遍历sku
		for (Sku sku : skuList) {
			//判断是否有此颜色对象了 需重写color的equals和hashcode
			if(!colors.contains(sku.getColor())){
				colors.add(sku.getColor());
			}
		}
		//放进域
		modelMap.addAttribute("colors", colors);
		
		return "/product/productDetail";
	}
}
