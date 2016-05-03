package com.sport.service.product;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sport.bean.product.Brand;
import com.sport.mapper.product.BrandMapper;
import com.sport.query.product.BrandQuery;

import cn.itcast.common.page.Pagination;
@Service
@Transactional
public class BrandServiceImpl implements BrandService{

	@Resource
	private BrandMapper brandMapper;
	
	@Transactional(readOnly=true)
	@Override
	public Pagination getBrandListWithPage(Brand brand){
		
		//分页
		Pagination pagination=new Pagination(brand.getPageNo(), brand.getPageSize(), brandMapper.getBrandCount(brand));
		
		pagination.setList(brandMapper.getBrandListWithPage(brand));
		return pagination;
	}

	@Override
	public void addBrand(Brand brand) {
		brandMapper.addBrand(brand);
	}

	@Override
	public void deleteBrandById(Integer id) {
		brandMapper.deleteBrandById(id);
	}

	@Override
	public void deleteBrandByIds(Integer[] ids) {
		brandMapper.deleteBrandByIds(ids);
	}

	@Override
	public void updateBrandById(Brand brand) {
		brandMapper.updateBrandById(brand);
	}

	@Transactional(readOnly=true)
	@Override
	public Brand getBrandById(Integer id) {
		
		return brandMapper.getBrandById(id);
	}

	@Transactional(readOnly=true)
	@Override
	public List<Brand> getBrandList(BrandQuery query) {
		
		return brandMapper.getBrandList(query);
	}
	
}
