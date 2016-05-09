package com.sport.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sport.bean.product.Color;
import com.sport.bean.product.Product;
import com.sport.bean.product.Sku;
import com.sport.mapper.product.SkuDao;
import com.sport.query.product.SkuQuery;

import cn.itcast.common.page.Pagination;

/**
 * 最小销售单元事务层
 * 
 * @author lixu
 * @Date [2014-3-27 下午03:31:57]
 */
@Service
@Transactional
public class SkuServiceImpl implements SkuService {

	@Resource
	SkuDao skuDao;
	@Resource
	ColorService colorService;
	@Resource
	ProductService productService;

	/**
	 * 插入数据库
	 * 
	 * @return
	 */
	public Integer addSku(Sku sku) {
		return skuDao.addSku(sku);
	}

	/**
	 * 根据主键查找
	 */
	@Transactional(readOnly = true)
	public Sku getSkuByKey(Integer id) {

		Sku sku = skuDao.getSkuByKey(id);
		// 通过商品id
		Product product = productService.getProductByKey(sku.getProductId());
		// 设置进去
		sku.setProduct(product);

		// 加载颜色
		// 获取对应的color
		Color color = colorService.getColorByKey(sku.getColorId());
		sku.setColor(color);
		
		return sku;
	}

	@Transactional(readOnly = true)
	public List<Sku> getSkusByKeys(List<Integer> idList) {
		return skuDao.getSkusByKeys(idList);
	}

	/**
	 * 根据主键删除
	 * 
	 * @return
	 */
	public Integer deleteByKey(Integer id) {
		return skuDao.deleteByKey(id);
	}

	public Integer deleteByKeys(List<Integer> idList) {
		return skuDao.deleteByKeys(idList);
	}

	/**
	 * 根据主键更新
	 * 
	 * @return
	 */
	public Integer updateSkuByKey(Sku sku) {
		return skuDao.updateSkuByKey(sku);
	}

	@Transactional(readOnly = true)
	public Pagination getSkuListWithPage(SkuQuery skuQuery) {
		Pagination p = new Pagination(skuQuery.getPageNo(), skuQuery.getPageSize(), skuDao.getSkuListCount(skuQuery));
		p.setList(skuDao.getSkuListWithPage(skuQuery));
		return p;
	}

	@Transactional(readOnly = true)
	public List<Sku> getSkuList(SkuQuery skuQuery) {
		List<Sku> skuList = skuDao.getSkuList(skuQuery);
		// 加载颜色
		for (Sku sku : skuList) {
			// 获取对应的color
			Color color = colorService.getColorByKey(sku.getColorId());
			sku.setColor(color);
		}
		return skuList;
	}

	@Override
	public List<Sku> getStock(Integer porductId) {
		List<Sku> list = skuDao.getStock(porductId);
		// 加载颜色
		for (Sku sku : list) {
			// 获取对应的color
			Color color = colorService.getColorByKey(sku.getColorId());
			sku.setColor(color);
		}
		return list;
	}
}
