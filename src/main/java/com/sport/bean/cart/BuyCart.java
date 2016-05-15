package com.sport.bean.cart;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车
 * 
 * @author chenguanhua
 *
 */
public class BuyCart {
	// 购物项
	List<BuyItem> items = new ArrayList<BuyItem>();
	// 商品id
	private Integer productId;
	
	//删除
	public void dele(BuyItem buyItem){
		items.remove(buyItem);
	}
	
	// 小计
	// 商品数量
	@JsonIgnore
	public int getProductAmount() {
		int result = 0;
		for (BuyItem buyItem : items) {
			result += buyItem.getAmount();
		}
		return result;
	}

	// 商品金额
	@JsonIgnore
	public Double getProductPrice() {
		Double result = 0.00;
		for (BuyItem buyItem : items) {
			result += buyItem.getSku().getSkuPrice() * buyItem.getAmount();
		}
		return result;
	}

	// 运费
	@JsonIgnore
	public Double getFrr() {
		Double result = 0.00;
		if (getProductPrice() <= 39) {
			result = 10.00;
		}
		return result;
	}

	// 因付金额
	@JsonIgnore
	public Double getTotalPrice() {
		return getFrr() + getProductPrice();
	}

	public void addItem(BuyItem buyItem) {
		// 判断是否重复
		if (items.contains(buyItem)) {
			for (BuyItem it : items) {
				if (it.equals(buyItem)) {
					// 总数
					int result = it.getAmount() + buyItem.getAmount();
					if (it.getSku().getSkuUpperLimit() >= result) {
						// 设置数量
						it.setAmount(result);
					} else {
						it.setAmount(it.getSku().getSkuUpperLimit());
					}

				}
			}
		} else {
			items.add(buyItem);
		}
	}

	public List<BuyItem> getItems() {
		return items;
	}

	public void setItems(List<BuyItem> items) {
		this.items = items;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

}
