package com.sport.service.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sport.bean.product.Img;
import com.sport.bean.product.Product;
import com.sport.bean.product.Sku;
import com.sport.mapper.product.ProductDao;
import com.sport.query.product.ImgQuery;
import com.sport.query.product.ProductQuery;

import cn.itcast.common.page.Pagination;
/**
 * 商品事务层
 * @author lixu
 * @Date [2014-3-27 下午03:31:57]
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Resource
	ProductDao productDao;
	@Resource
	ImgService imgService;
	@Resource
	SkuService skuService;
	
	/**
	 * 插入数据库
	 * 
	 * @return
	 */
	public Integer addProduct(Product product) {
		//设置商品编号
		DateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String no = df.format(new Date());
		product.setNo(no);
		
		//添加时间
		product.setCreateTime(new Date());
		
		//影响行数 返回商品id
		Integer i = productDao.addProduct(product);
		
		//关联图片表 
		//设置商品id
		product.getImg().setProductId(product.getId());
		//是否默认
		product.getImg().setIsDef(1);
		imgService.addImg(product.getImg());
		
		//关联sku表
		Sku sku=new Sku();
		//关联id
		sku.setProductId(product.getId());
		//运费
		sku.setDeliveFee(10.00);
		//售价
		sku.setSkuPrice(0.00);
		//市场价
		sku.setMarketPrice(0.00);
		//库存
		sku.setStockInventory(0);
		//购买限制
		sku.setSkuUpperLimit(0);
		//添加时间
		sku.setCreateTime(new Date());
		//是否最新
		sku.setLastStatus(1);
		//商品
		sku.setSkuType(1);
		//销量
		sku.setSales(0);
		
		//页面的color为多选框 需遍历循环
		for(String color:product.getColor().split(",")){
			//颜色id 
			sku.setColorId(Integer.parseInt(color));
			
			for (String size:product.getSize().split(",")) {
			//size
				sku.setSize(size);
				//保存
				skuService.addSku(sku);
			}
		}
		
		return i;
	}

	/**
	 * 根据主键查找
	 */
	@Transactional(readOnly = true)
	public Product getProductByKey(Integer id) {
		//用于商品显示
		Product product = productDao.getProductByKey(id);
		//img查询对象
		ImgQuery imgQuery=new ImgQuery();
		imgQuery.setProductId(product.getId());
		imgQuery.setIsDef(1);
		
		List<Img> list = imgService.getImgList(imgQuery);
		product.setImg(list.get(0));
		
		return product;
	}
	
	@Transactional(readOnly = true)
	public List<Product> getProductsByKeys(List<Integer> idList) {
		return productDao.getProductsByKeys(idList);
	}

	/**
	 * 根据主键删除
	 * 
	 * @return
	 */
	public Integer deleteByKey(Integer id) {
		return productDao.deleteByKey(id);
	}

	public Integer deleteByKeys(List<Integer> idList) {
		return productDao.deleteByKeys(idList);
	}

	/**
	 * 根据主键更新
	 * 
	 * @return
	 */
	public Integer updateProductByKey(Product product) {
		return productDao.updateProductByKey(product);
	}
	
	@Transactional(readOnly = true)
	public Pagination getProductListWithPage(ProductQuery productQuery) {
		Pagination p = new Pagination(productQuery.getPageNo(),productQuery.getPageSize(),productDao.getProductListCount(productQuery));
		List<Product> products = productDao.getProductListWithPage(productQuery);
		
		//遍历商品 查询图片 用于页面展示
		for (Product product : products) {
			
			//img查询对象
			ImgQuery imgQuery=new ImgQuery();
			imgQuery.setProductId(product.getId());
			imgQuery.setIsDef(1);
			
			List<Img> list = imgService.getImgList(imgQuery);
			product.setImg(list.get(0));
		}
		
		p.setList(products);
		return p;
	}
	
	@Transactional(readOnly = true)
	public List<Product> getProductList(ProductQuery productQuery) {
		return productDao.getProductList(productQuery);
	}
}
