package com.sport.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.bean.product.Brand;
import com.sport.bean.product.Feature;
import com.sport.bean.product.Product;
import com.sport.bean.product.Type;
import com.sport.query.product.BrandQuery;
import com.sport.query.product.FeatureQuery;
import com.sport.query.product.ProductQuery;
import com.sport.query.product.TypeQuery;
import com.sport.service.product.BrandService;
import com.sport.service.product.FeatureService;
import com.sport.service.product.ProductService;
import com.sport.service.product.TypeService;

import cn.itcast.common.page.Pagination;

/**
 * 商品前台页面
 * 
 * @author chenguanhua
 *
 */
@Controller
public class FrontProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private FeatureService featureService;
	
	@RequestMapping()
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,ModelMap modelMap) {
		// 加载商品类型
		TypeQuery typeQuery = new TypeQuery();
		// 查询字段
		typeQuery.setFields("id,name");
		// 设置可见
		typeQuery.setIsDisplay(1);
		// 设置关联类型id
		typeQuery.setParentId(0);
		// 集合
		List<Type> typeList = typeService.getTypeList(typeQuery);
		modelMap.addAttribute("typeList", typeList);

		// 品牌查询条件
		BrandQuery brandQuery = new BrandQuery();
		// 查询字段
		brandQuery.setFields("id,name");
		// 设置可见
		brandQuery.setIsDisplay(1);

		// 加载品牌
		List<Brand> brandList = brandService.getBrandList(brandQuery);
		modelMap.addAttribute("brandList", brandList);

		// 加载商品属性
		FeatureQuery featureQuery = new FeatureQuery();
		List<Feature> featureList = featureService.getFeatureList(featureQuery);
		modelMap.addAttribute("featureList", featureList);
		
		//查询条件
		ProductQuery productQuery=new ProductQuery();
		//设置页码
		productQuery.setPageNo(pageNo);
		//设置页数
		productQuery.setPageSize(Product.FRONT_PAGE_SIZE);
		//加载分页商品
		Pagination page = productService.getProductListWithPage(productQuery);
		//parms 为页面展示分页数据 下一页...
		StringBuilder params = new StringBuilder();
		String url = "/product/list.do";
		// 页面展示
		page.pageView(url, params.toString());

		modelMap.addAttribute("page", page);
		
		return "product/product";
	}
}
