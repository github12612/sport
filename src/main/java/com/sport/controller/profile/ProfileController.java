package com.sport.controller.profile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.sport.bean.country.City;
import com.sport.bean.country.Province;
import com.sport.bean.country.Town;
import com.sport.bean.user.Buyer;
import com.sport.common.ResponUtils;
import com.sport.common.encode.Md5Pwd;
import com.sport.common.session.SessionProvider;
import com.sport.query.country.CityQuery;
import com.sport.query.country.TownQuery;
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
	private ProvinceService provinceService;
	//市
	@Autowired
	private CityService cityService;
	//县
	@Autowired
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
	@RequestMapping(value="/buyer/profile.shtml")
	public String profile(HttpServletRequest request,ModelMap modelMap){
		
		//加载用户
		Buyer buyer = (Buyer) sessionProvider.getAttrbute(request,Constants.BUYER_SESSION );
		modelMap.addAttribute("buyer", buyer);
		
		//省
		List<Province> provinceList = provinceService.getProvinceList(null);
		modelMap.addAttribute("provinceList", provinceList);
		
		//市
		CityQuery cityQuery = new CityQuery();	
		cityQuery.setProvince(buyer.getProvince());
		List<City> cityList = cityService.getCityList(cityQuery);
		modelMap.addAttribute("cityList", cityList);
		
		//县
		TownQuery townQuery = new TownQuery();
		townQuery.setCity(buyer.getCity());
		List<Town> townList = townService.getTownList(townQuery);
		modelMap.addAttribute("townList", townList);
		
		return "buyer/profile";
	}
	
	/**
	 * ajax市
	 * @param code
	 * @param response
	 */
	@RequestMapping(value="/buyer/city.shtml")
	public void citys(String code,HttpServletResponse response){
		//市
		CityQuery cityQuery = new CityQuery();
		cityQuery.setProvince(code);
		List<City> cityList = cityService.getCityList(cityQuery);
		
		JSONObject js= new JSONObject();
		js.put("cityList", cityList);
		
		ResponUtils.renderJson(response, js.toString());
	}
	/**
	 * ajax县
	 * @param code
	 * @param response
	 */
	@RequestMapping(value="/buyer/town.shtml")
	public void towns(String code,HttpServletResponse response){
		//市
		TownQuery townQuery = new TownQuery();
		townQuery.setCity(code);
		List<Town> townList = townService.getTownList(townQuery);
		
		JSONObject js= new JSONObject();
		js.put("townList", townList);
		
		ResponUtils.renderJson(response, js.toString());
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
