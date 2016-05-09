package com.sport.bean.cart;

import java.util.ArrayList;
import java.util.List;

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

	// 小计

	public void addItem(BuyItem buyItem) {
		//判断是否重复
		if(items.contains(buyItem)){
			for (BuyItem it : items) {
				if(it.equals(buyItem)){
					//总数
					int result=it.getAmount()+buyItem.getAmount();
					if(it.getSku().getSkuUpperLimit() >= result){
						//设置数量
						it.setAmount(result);
					}else{
						it.setAmount(it.getSku().getSkuUpperLimit());
					}
					
				}
			}
		}else{
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
