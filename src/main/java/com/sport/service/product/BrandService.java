package com.sport.service.product;

import java.util.List;

import com.sport.bean.product.Brand;
import com.sport.query.product.BrandQuery;

import cn.itcast.common.page.Pagination;
/*
 * 品牌service
 */
public interface BrandService {
	
	/**
	 * 分页数据
	 * @param brand
	 * @return
	 */
	public Pagination getBrandListWithPage(Brand brand);
	
	/**
	 * 集合
	 * @param brand
	 * @return
	 */
	public List<Brand> getBrandList(BrandQuery query);
	
	/**
	 * 添加
	 * @param brand
	 */
	public void addBrand(Brand brand);
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteBrandById(Integer id);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteBrandByIds(Integer [] ids);
	
	/**
	 * 更新
	 * @param brand
	 */
	public void updateBrandById(Brand brand);
	
	/**
	 * 根据id获取brand
	 * @param id
	 * @return
	 */
	public Brand getBrandById(Integer id);
}
