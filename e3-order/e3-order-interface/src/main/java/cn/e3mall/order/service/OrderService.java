package cn.e3mall.order.service;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.order.entity.OrderInfo;

public interface OrderService {
	//创建订单
	E3mallResult createOrder(OrderInfo orderInfo);

}
