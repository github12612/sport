package com.sport.controller.product;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.bean.product.Brand;
import com.sport.bean.product.Color;
import com.sport.bean.product.Feature;
import com.sport.bean.product.Img;
import com.sport.bean.product.Product;
import com.sport.bean.product.Type;
import com.sport.query.product.BrandQuery;
import com.sport.query.product.ColorQuery;
import com.sport.query.product.FeatureQuery;
import com.sport.query.product.ProductQuery;
import com.sport.query.product.TypeQuery;
import com.sport.service.product.BrandService;
import com.sport.service.product.ColorService;
import com.sport.service.product.FeatureService;
import com.sport.service.product.ProductService;
import com.sport.service.product.TypeService;

import cn.itcast.common.page.Pagination;

/**
 * 后台商品管理 商品列表 商品添加 商品上架
 * 
 * @author chenguanhua
 *
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private FeatureService featureService;
	@Autowired
	private ColorService colorService;

	@RequestMapping("list.do")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, String name, Integer brandId,
			Integer isShow, ModelMap modelMap) {

		// 品牌查询条件
		BrandQuery brandQuery = new BrandQuery();
		System.out.println(name);
		brandQuery.setFields("id,name");
		// 设置可见
		brandQuery.setIsDisplay(1);

		// 加载品牌
		List<Brand> brandList = brandService.getBrandList(brandQuery);

		modelMap.addAttribute("brandList", brandList);

		// 商品查询条件
		ProductQuery productQuery = new ProductQuery();

		// parms 为页面展示分页数据 下一页...
		StringBuilder params = new StringBuilder();

		// 设置页数计算
		productQuery.setPageNo(pageNo);

		// 判断是否为null
		if (StringUtils.isNotBlank(name)) {
			productQuery.setName(name);
			// 开启去模糊查询
			productQuery.setNameLike(true);
			params.append("&name=").append(name);
			// 回显查询条件
			modelMap.addAttribute("name", name);
		}
		// 设置品牌id
		if (brandId != null) {
			productQuery.setBrandId(brandId);
			params.append("&").append("brandId=").append(brandId);
			// 回显查询条件
			modelMap.addAttribute("brandId", brandId);

		}
		// 设置上下架
		if (isShow != null) {
			productQuery.setIsShow(isShow);
			params.append("&").append("isShow=").append(isShow);

			// 回显查询条件
			modelMap.addAttribute("isShow", isShow);
		} else {
			productQuery.setIsShow(0);
			params.append("&").append("isShow=").append(0);

			// 回显查询条件
			modelMap.addAttribute("isShow", 0);
		}

		// 设置倒序
		productQuery.orderbyBrandId(false);
		// 加载分页商品
		Pagination page = productService.getProductListWithPage(productQuery);

		String url = "/product/list.do";
		// 页面展示
		page.pageView(url, params.toString());

		modelMap.addAttribute("page", page);

		return "product/list";
	}

	/**
	 * 去添加页面并显示要添加的内容
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("toAdd.do")
	public String toAdd(ModelMap modelMap) {

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

		// 颜色查询条件
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.setParentId(0);
		List<Color> colorList = colorService.getColorList(colorQuery);
		modelMap.addAttribute("colorList", colorList);

		return "product/add";
	}

	/**
	 * 添加
	 * 
	 * @param product
	 * @param img
	 * @return
	 */
	@RequestMapping("add.do")
	public String add(Product product, Img img) {

		product.setImg(img);
		productService.addProduct(product);

		return "redirect:/product/list.do";
	}

	/**
	 * 去更新页面
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * public String edit(Integer id){ Product product =
	 * productService.getProductByKey(id);
	 * 
	 * return ""; }
	 */

	/**
	 * 上架
	 * 
	 * @param pageNo
	 * @param name
	 * @param brandId
	 * @param isShow
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("isShow.do")
	public String isShow(Integer[] ids, Integer pageNo, String name, Integer brandId, Integer isShow,
			ModelMap modelMap) {

		// 实例化商品
		Product product = new Product();
		product.setIsShow(1);
		// 上架
		if (ids != null && ids.length > 0) {
			for (Integer id : ids) {
				product.setId(id);
				// 修改上架状态
				productService.updateProductByKey(product);
			}
		}

		if (pageNo != null) {
			modelMap.addAttribute("pageNo", pageNo);
		}
		if (StringUtils.isNotBlank(name)) {
			modelMap.addAttribute("name", name);
		}
		if (brandId != null) {
			modelMap.addAttribute("brandId", brandId);
		}
		if (isShow != null) {
			modelMap.addAttribute("isShow", isShow);
		}

		return "redirect:/product/list.do";
	}
}
