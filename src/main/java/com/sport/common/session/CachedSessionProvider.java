package com.sport.common.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 远程session 存放memcached
 * @author chenguanhua
 *
 */
public class CachedSessionProvider implements SessionProvider{

	@Override
	public void setAttrbute(HttpServletRequest request, String name, Serializable value) {
		
	}

	@Override
	public Serializable getAttrbute(HttpServletRequest request, String name) {
		return null;
	}

	@Override
	public void logOut(HttpServletRequest request) {
		
	}

	@Override
	public String getSessionId(HttpServletRequest request) {
		return null;
	}

}
