package com.sport.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 跳转页面的controller
 * @author chenguanhua
 *
 */
@Controller
@RequestMapping("/admin/")
public class CenterController {
	
	/**
	 * 跳转入口页面
	 * @return
	 */
	@RequestMapping("index.do")
	public String index(){
		
		return "index";
	}
	
	/**
	 * 跳转头页面
	 * @return
	 */
	@RequestMapping("top.do")
	public String top(){
		
		return "top";
	}
	
	/**
	 * 跳转main页面
	 * @return
	 */
	@RequestMapping("main.do")
	public String main(){
		
		return "main";
	}
	
	/**
	 * 跳转right页面
	 * @return
	 */
	@RequestMapping("right.do")
	public String right(){
		
		return "right";
	}
	
	/**
	 * 跳转left页面
	 * @return
	 */
	@RequestMapping("left.do")
	public String left(){
		
		return "left";
	}
}
