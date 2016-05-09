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
import com.sport.service.product.SkuService;
import com.sport.web.Constants;

@Controller
public class CartController {

	@Autowired
	private SkuService skuService;

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
			sku.setSkuUpperLimit(buyLimit);

			// 购物项
			BuyItem buyItem = new BuyItem();
			// 设置
			buyItem.setSku(sku);
			// 数量
			buyItem.setAmount(amount);

			// 添加购物项
			buyCart.addItem(buyItem);
			// 设置productid
			buyCart.setProductId(productId);

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
}
