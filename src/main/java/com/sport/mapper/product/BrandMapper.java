package com.sport.mapper.product;

import java.util.List;

import com.sport.bean.product.Brand;
import com.sport.query.product.BrandQuery;
/**
 * 品牌接口
 * @author chenguanhua
 *
 */
public interface BrandMapper {
	/**
	 * 集合分页
	 * @param brand
	 * @return
	 */
	public List<Brand> getBrandListWithPage(Brand brand);
	
	/**
	 * 集合
	 * @param brand
	 * @return
	 */
	public List<Brand> getBrandList(BrandQuery query);
	
	/**
	 * 查询总记录数
	 * @param brand
	 * @return
	 */
	public int getBrandCount(Brand brand);
	
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
