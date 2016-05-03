package com.sport.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 处理ajax的工具类
 * @author chenguanhua
 *
 */
public class ResponUtils {
	
	//发送
	public static void render(HttpServletResponse response,String contenType,String text){
		response.setContentType(contenType);
		try {
			response.getWriter().println(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//json
	public static void renderJson(HttpServletResponse response,String text){
		render(response, "application/json;charset=UTF-8", text);
	}
	
	//xml
	public static void renderXml(HttpServletResponse response,String text){
		render(response, "text/xml;charset=UTF-8", text);
	}
	//xml
	public static void renderText(HttpServletResponse response,String text){
		render(response, "text/plain;charset=UTF-8", text);
	}
}
