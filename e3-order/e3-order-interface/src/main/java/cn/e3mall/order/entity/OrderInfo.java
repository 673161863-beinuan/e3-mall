package cn.e3mall.order.entity;

import java.io.Serializable;
import java.util.List;

import cn.e3mall.entity.TbOrder;
import cn.e3mall.entity.TbOrderItem;
import cn.e3mall.entity.TbOrderShipping;

public class OrderInfo extends TbOrder implements Serializable{
	
	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
	

}
