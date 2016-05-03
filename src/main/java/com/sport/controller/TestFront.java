package com.sport.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestFront {
	
	@RequestMapping("/test/hello.do")
	public String test(String name,Date birthday){
		System.out.println(name);
		return "";
	}

}
