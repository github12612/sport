package com.sport.common.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * session 供应类
 * @author chenguanhua
 *
 */
public interface SessionProvider {
	/**
	 * 往session 中 设置值
	 * @param request
	 * @param name
	 * @param value
	 */
	public void setAttrbute(HttpServletRequest request,String name,Serializable value);
	
	/**
	 * 取值
	 * @param request
	 * @param name
	 * @return
	 */
	public Serializable getAttrbute(HttpServletRequest request,String name);
	
	/**
	 * 登出
	 * @param request
	 */
	public void logOut(HttpServletRequest request);

	/**
	 * 获取sessionid
	 * @param request
	 * @return
	 */
	public String getSessionId(HttpServletRequest request);
}
