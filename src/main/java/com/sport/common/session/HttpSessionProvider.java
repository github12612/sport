package com.sport.common.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 本地session
 * 
 * @author chenguanhua
 *
 */
public class HttpSessionProvider implements SessionProvider {

	@Override
	public void setAttrbute(HttpServletRequest request, String name, Serializable value) {

		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}

	@Override
	public Serializable getAttrbute(HttpServletRequest request, String name) {
		// 不要新session 要旧的
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (Serializable) session.getAttribute(name);
		}else{
			return null;
		}
	}

	@Override
	public void logOut(HttpServletRequest request) {

		// 不要新session 要旧的
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Override
	public String getSessionId(HttpServletRequest request) {
		
		return request.getSession().getId();
	}

}
