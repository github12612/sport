package com.sport.controller.profile;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.sport.bean.user.Buyer;
import com.sport.common.encode.Md5Pwd;
import com.sport.common.session.SessionProvider;
import com.sport.service.country.CityService;
import com.sport.service.country.ProvinceService;
import com.sport.service.country.TownService;
import com.sport.service.user.BuyerService;
import com.sport.web.Constants;

/**
 * 跳转登陆界面 登陆 个人资料 收货地址
 * 
 * 
 * @author chenguanhua
 *
 */
@Controller
public class ProfileController {

	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private Md5Pwd md5;
	@Autowired
	private BuyerService buyerService;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	//省
	@Autowired
	private ProvinceService ProvinceService;
	//市
	private CityService cityService;
	//县
	private TownService townService;
	
	// get 进入登陆界面
	@RequestMapping(value = "/shopping/login.shtml", method = RequestMethod.GET)
	public String login() {

		return "buyer/login";
	}

	// POST
	@RequestMapping(value = "/shopping/login.shtml", method = RequestMethod.POST)
	public String login(String returnUrl, Buyer buyer, String captcha, HttpServletRequest request, ModelMap modelMap) {

		// 判断验证码是否为null
		if (StringUtils.isNotBlank(captcha)) {
			// 1.JSESSIONID
			// 2.验证码
			if (imageCaptchaService.validateResponseForID(sessionProvider.getSessionId(request), captcha)) {
				// 判断用户名
				if (buyer != null && StringUtils.isNotBlank(buyer.getUsername())) {
					if (StringUtils.isNotBlank(buyer.getPassword())) {
						Buyer b = buyerService.getBuyerByKey(buyer.getUsername());
						if (b != null) {
							// 判断密码
							if (b.getPassword().equals(md5.encode(buyer.getPassword()))) {
								// 把用户对象放入session
								sessionProvider.setAttrbute(request, Constants.BUYER_SESSION, b);

								if (StringUtils.isNotBlank(returnUrl)) {

									return "redirect:" + returnUrl;
								} else {
									// 个人中心
									return "redirect:/buyer/index.shtml";
								}
							} else {
								modelMap.addAttribute("error", "密码输入错误");
							}
						} else {
							modelMap.addAttribute("error", "用户名错误");
						}
					} else {
						modelMap.addAttribute("error", "请输入密码");
					}
				} else {
					modelMap.addAttribute("error", "请输入用户名");
				}
			} else {
				modelMap.addAttribute("error", "验证码输入错误");
			}
		} else {
			modelMap.addAttribute("error", "请输入验证码");
		}

		return "buyer/login";
	}

	/**
	 * 登出
	 * 
	 * @param returnUrl
	 * @param request
	 * @return
	 */
	@RequestMapping("/shopping/logout.shtml")
	public String logOut(String returnUrl, HttpServletRequest request) {
		sessionProvider.logOut(request);
		return "redirect:returnUrl";
	}

	/**
	 * 个人中心
	 * @return
	 */
	@RequestMapping("/buyer/index.shtml")
	public String index(){
		
		return "buyer/index";
	}

	/**
	 * 个人资料
	 * @return
	 */
	@RequestMapping("/buyer/profile.shtml")
	public String profile(){
		
		return "buyer/profile";
	}

	/**
	 * 收货地址
	 * @return
	 */
	@RequestMapping("/buyer/deliver_address.shtml")
	public String address() {

		return "buyer/deliver_address";
	}
}
