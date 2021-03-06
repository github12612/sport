package com.sport.service.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sport.bean.cart.BuyCart;
import com.sport.bean.cart.BuyItem;
import com.sport.bean.order.Detail;
import com.sport.bean.order.Order;
import com.sport.mapper.order.OrderDao;
import com.sport.query.order.OrderQuery;

import cn.itcast.common.page.Pagination;

/**
 * 订单主
 * 
 * @author lixu
 * @Date [2014-3-27 下午03:31:57]
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Resource
	OrderDao orderDao;
	@Resource
	DetailService detailService;

	/**
	 * 插入数据库
	 * 
	 * @return
	 */
	public Integer addOrder(Order order, BuyCart buyCart) {
		// 生成订单号
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String oid = df.format(new Date());
		order.setOid(oid);
		// 运费
		order.setDeliverFee(buyCart.getFrr());
		// 应付金额
		order.setPayableFee(buyCart.getTotalPrice());
		// 订单金额
		order.setTotalPrice(buyCart.getProductPrice());
		// 支付状态
		if (order.getPaymentWay() == 0) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}
		// 订单状态 提交订单
		order.setState(0);
		// 订单生成时间
		order.setCreateDate(new Date());
		
		// 保存订单
		Integer addOrder = orderDao.addOrder(order);
		
		//购物项集合
		List<BuyItem> items = buyCart.getItems();
		for (BuyItem buyItem : items) {
			//保存订单详情
			Detail detail = new Detail();
			//订单id
			detail.setOrderId(order.getId());
			//商品名称
			detail.setProductName(buyItem.getSku().getProduct().getName());
			//商品编号
			detail.setProductNo(buyItem.getSku().getProduct().getNo());
			//颜色
			detail.setColor(buyItem.getSku().getColor().getName());
			//尺码
			detail.setSize(buyItem.getSku().getSize());
			//价格
			detail.setSkuPrice(buyItem.getSku().getSkuPrice());
			//数量
			detail.setAmount(buyItem.getAmount());
			//保存订单详情
			detailService.addDetail(detail);
			
		}
		
		return addOrder;
	}

	/**
	 * 根据主键查找
	 */
	@Transactional(readOnly = true)
	public Order getOrderByKey(Integer id) {
		return orderDao.getOrderByKey(id);
	}

	@Transactional(readOnly = true)
	public List<Order> getOrdersByKeys(List<Integer> idList) {
		return orderDao.getOrdersByKeys(idList);
	}

	/**
	 * 根据主键删除
	 * 
	 * @return
	 */
	public Integer deleteByKey(Integer id) {
		return orderDao.deleteByKey(id);
	}

	public Integer deleteByKeys(List<Integer> idList) {
		return orderDao.deleteByKeys(idList);
	}

	/**
	 * 根据主键更新
	 * 
	 * @return
	 */
	public Integer updateOrderByKey(Order order) {
		return orderDao.updateOrderByKey(order);
	}

	@Transactional(readOnly = true)
	public Pagination getOrderListWithPage(OrderQuery orderQuery) {
		Pagination p = new Pagination(orderQuery.getPageNo(), orderQuery.getPageSize(),
				orderDao.getOrderListCount(orderQuery));
		p.setList(orderDao.getOrderListWithPage(orderQuery));
		return p;
	}

	@Transactional(readOnly = true)
	public List<Order> getOrderList(OrderQuery orderQuery) {
		return orderDao.getOrderList(orderQuery);
	}
}
