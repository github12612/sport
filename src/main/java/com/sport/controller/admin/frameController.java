package com.sport.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模块跳转页面
 * @author chenguanhua
 *
 */
@Controller
@RequestMapping("/admin/")
public class frameController {
	
	/**
	 * 商品main
	 * @return
	 */
	@RequestMapping("frame/product_main.do")
	public String productMain(){
		return "frame/product_main";
	}
	
	/**
	 * left商品
	 * @return
	 */
	@RequestMapping("frame/product_left.do")
	public String productLeft(){
		return "frame/product_left";
	}
	
	/**
	 * 订单main
	 * @return
	 */
	@RequestMapping("frame/order_main.do")
	public String orderMain(){
		return "frame/order_main";
	}
	
	/**
	 * left订单
	 * @return
	 */
	@RequestMapping("frame/order_left.do")
	public String orderLeft(){
		return "frame/order_left";
	}
	
}
