package com.sport.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sport.bean.order.Detail;
import com.sport.bean.order.Order;
import com.sport.bean.user.Addr;
import com.sport.query.order.DetailQuery;
import com.sport.query.order.OrderQuery;
import com.sport.query.user.AddrQuery;
import com.sport.service.order.DetailService;
import com.sport.service.order.OrderService;
import com.sport.service.user.AddrService;
/**
 * 订单列表
 * @author chenguanhua
 *
 */
@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private DetailService detailService;
	@Autowired
	private AddrService addrService;
	/**
	 * 订单列表
	 * @param isPaiy
	 * @return
	 */
	@RequestMapping("/order/list.do")
	public String orderList(Integer isPaiy,Integer state,ModelMap modelMap){
		
		OrderQuery orderQuery = new OrderQuery();
		//支付状态
		if(isPaiy != null){
			orderQuery.setIsPaiy(isPaiy);
		}
		//订单状态
		if(state != null){
			orderQuery.setState(state);
		}
		
		List<Order> orderList = orderService.getOrderList(orderQuery);
		modelMap.addAttribute("orderList", orderList);
		return "order/list";
	}
	
	/**
	 * 订单查看
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/order/view.do")
	public String view(Integer id,ModelMap modelMap){
		//订单表
		Order order = orderService.getOrderByKey(id);
		
		//订单详情表
		DetailQuery detailQuery = new DetailQuery();
		detailQuery.setOrderId(id);
		List<Detail> detailList = detailService.getDetailList(detailQuery);
		
		//收货地址
		AddrQuery addrQuery = new AddrQuery();
		addrQuery.setBuyerId(order.getBuyerId());
		addrQuery.setIsDef(1);
		List<Addr> addrList = addrService.getAddrList(addrQuery);
		
		modelMap.addAttribute("order", order);
		modelMap.addAttribute("detailList", detailList);
		modelMap.addAttribute("addrList", addrList.get(0));
		
		return "order/view";
	}
}
