package com.sport.controller.order;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sport.bean.cart.BuyCart;
import com.sport.bean.cart.BuyItem;
import com.sport.bean.order.Order;
import com.sport.bean.product.Sku;
import com.sport.bean.user.Buyer;
import com.sport.common.session.SessionProvider;
import com.sport.service.order.OrderService;
import com.sport.service.product.SkuService;
import com.sport.web.Constants;

/**
 * 订单controller
 * 
 * @author chenguanhua
 *
 */
@Controller
public class FrontOrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private SkuService skuService;
	/**
	 * 提交订单
	 * 
	 * @return
	 */
	@RequestMapping("/buyer/confirmOrder.shtml")
	public String confirmOrder(Order order, HttpServletRequest request,HttpServletResponse response ) {

		BuyCart buyCart = null;
		// springmvc 转json
		ObjectMapper om = new ObjectMapper();
		// 设置为null的数据不显示
		om.setSerializationInclusion(Include.NON_NULL);

		// 判断是否有cookie
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {

			for (Cookie cookie : cookies) {

				if (Constants.BUYCART_COOKIE.equals(cookie.getName())) {
					// json转对象
					// 如果cookie有值 就使用购物车
					String value = cookie.getValue();
					try {
						buyCart = om.readValue(value, BuyCart.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}

		// 装满购物车
		List<BuyItem> items = buyCart.getItems();
		for (BuyItem item : items) {
			Sku sk = skuService.getSkuByKey(item.getSku().getId());
			item.setSku(sk);
		}

		Buyer buyer = (Buyer) sessionProvider.getAttrbute(request, Constants.BUYER_SESSION);
		// 用户id
		order.setBuyerId(buyer.getUsername());

		// 保存订单 订单详情2张表
		orderService.addOrder(order, buyCart);
		//提交后清空购物车
		Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);

		return "product/confirmOrder";
	}
}
