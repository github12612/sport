package com.sport.controller.cart;

import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sport.bean.cart.BuyCart;
import com.sport.bean.cart.BuyItem;
import com.sport.bean.product.Sku;
import com.sport.bean.user.Addr;
import com.sport.bean.user.Buyer;
import com.sport.common.session.SessionProvider;
import com.sport.query.user.AddrQuery;
import com.sport.service.product.SkuService;
import com.sport.service.user.AddrService;
import com.sport.web.Constants;

@Controller
public class CartController {

	@Autowired
	private SkuService skuService;
	@Autowired
	private AddrService addrService;
	@Autowired
	private SessionProvider sessionProvider;
	/**
	 * 购物车
	 * 
	 * @param skuId
	 * @param amount
	 * @param buyLimit
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/shopping/BuyCart.shtml")
	public String buyCart(Integer skuId, Integer amount, Integer buyLimit, Integer productId,
			HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

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

		if (buyCart == null) {
			// 购物车
			buyCart = new BuyCart();
		}
		if (skuId != null) {
			// 库存
			Sku sku = new Sku();
			sku.setId(skuId);
			// 限制
			if (buyLimit != null) {
				sku.setSkuUpperLimit(buyLimit);
			}
			// 购物项
			BuyItem buyItem = new BuyItem();
			// 设置
			buyItem.setSku(sku);
			// 数量
			buyItem.setAmount(amount);

			// 添加购物项
			buyCart.addItem(buyItem);
			// 设置productid
			if (productId != null) {
				buyCart.setProductId(productId);
			}
			// 对象转json 写的过程
			StringWriter s = new StringWriter();
			try {
				om.writeValue(s, buyCart);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 放入cooki
			Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, s.toString());
			// 关闭浏览器也要有cookie
			// 默认-1 关闭浏览器就没了
			// 0 马上就没了
			// expiry 秒 一天
			cookie.setMaxAge(60 * 60 * 24);
			// 设置路径 默认请求的uri
			cookie.setPath("/");

			// 发送
			response.addCookie(cookie);

		}
		// 装满购物车
		List<BuyItem> items = buyCart.getItems();
		for (BuyItem item : items) {
			Sku sk = skuService.getSkuByKey(item.getSku().getId());
			item.setSku(sk);
		}
		modelMap.addAttribute("buyCart", buyCart);

		return "product/cart";
	}

	/**
	 * 清空购物车
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/shopping/clean.shtml")
	public String cleanCart(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, null);

		cookie.setMaxAge(0);

		cookie.setPath("/");

		response.addCookie(cookie);
		return "redirect:/shopping/BuyCart.shtml";
	}

	/**
	 * 删除一个
	 * 
	 * @param request
	 * @param skuId
	 * @return
	 */
	@RequestMapping("/shopping/delete.shtml")
	public String delte(HttpServletRequest request, Integer skuId, HttpServletResponse response) {
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

		if (buyCart != null) {
			Sku sku = new Sku();
			sku.setId(skuId);
			// 购物项
			BuyItem buyItem = new BuyItem();
			// 设置
			buyItem.setSku(sku);
			// 删除
			buyCart.dele(buyItem);

			// 对象转json 写的过程
			StringWriter s = new StringWriter();
			try {
				om.writeValue(s, buyCart);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 放入cooki
			Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, s.toString());
			// 关闭浏览器也要有cookie
			// 默认-1 关闭浏览器就没了
			// 0 马上就没了
			// expiry 秒 一天
			cookie.setMaxAge(60 * 60 * 24);
			// 设置路径 默认请求的uri
			cookie.setPath("/");

			// 发送
			response.addCookie(cookie);
		}
		return "redirect:/shopping/BuyCart.shtml";
	}

	/**
	 * 结算
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/buyer/toBuy.shtml")
	public String toBuy(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) {

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
		// 判断购物车是否有商品
		if (buyCart != null) {
			List<BuyItem> items = buyCart.getItems();
			//清理前商品个数
			Integer i = items.size();
			
			//判断购物项是否有库存
			if (items != null && items.size()>0) {
				
				for (BuyItem buyItem : items) {
					Sku sku = skuService.getSkuByKey(buyItem.getSku().getId());
					//判断库存
					if(sku.getStockInventory() < buyItem.getAmount()){
						//购物车删除一个
						buyCart.dele(buyItem);
					}
				}
				//清理后商品个数
				Integer I= items.size();
				
				if(i > I){
					
					// 对象转json 写的过程
					StringWriter s = new StringWriter();
					try {
						om.writeValue(s, buyCart);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// 放入cooki
					Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, s.toString());
					// 关闭浏览器也要有cookie
					// 默认-1 关闭浏览器就没了
					// 0 马上就没了
					// expiry 秒 一天
					cookie.setMaxAge(60 * 60 * 24);
					// 设置路径 默认请求的uri
					cookie.setPath("/");

					// 发送
					response.addCookie(cookie);
					
					return "redirect:/shopping/BuyCart.shtml";
				}else{
					
					//加载收获地址
					Buyer buyer = (Buyer) sessionProvider.getAttrbute(request, Constants.BUYER_SESSION);
					
					AddrQuery addrQuery = new AddrQuery();
					addrQuery.setBuyerId(buyer.getUsername());
					addrQuery.setIsDef(1);
					List<Addr> addrList = addrService.getAddrList(addrQuery);
					
					modelMap.addAttribute("addrList", addrList.get(0));
					
					// 装满购物车
					List<BuyItem> its = buyCart.getItems();
					for (BuyItem item : its) {
						Sku sk = skuService.getSkuByKey(item.getSku().getId());
						item.setSku(sk);
					}
					modelMap.addAttribute("buyCart", buyCart);
					
					//正常 返回结算页面
					return "product/productOrder";
				}
				
			} else {
				return "redirect:/shopping/BuyCart.shtml";
			}
		} else {
			return "redirect:/shopping/BuyCart.shtml";
		}

		
	}
}
