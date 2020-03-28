package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbOrderItem;
import cn.e3mall.entity.TbOrderShipping;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.entity.OrderInfo;
import cn.e3mall.order.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired 
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	//#生成订单号key
	@Value("${CREATE_ORDER_ID}")
	private String CREATE_ORDER_ID;
	//生成订单号key的起始值
	@Value("${CREATE_ORDER_ID_START}")
	private String CREATE_ORDER_ID_START;
	//生成订单明细key
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	@Override
	public E3mallResult createOrder(OrderInfo orderInfo) {
		//生成订单号，使用redis的incr生成
		if(!jedisClient.exists(CREATE_ORDER_ID)){
			jedisClient.set(CREATE_ORDER_ID, CREATE_ORDER_ID_START);
		}
		String orderId = jedisClient.incr(CREATE_ORDER_ID).toString();
		
		//补全orderinfo的属性
		orderInfo.setOrderId(orderId);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		//插入订单表
		orderMapper.insert(orderInfo);
		//想订单明细插入数据
		List<TbOrderItem> items = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : items) {
			String odId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//想订单物流插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		
		orderShipping.setOrderId(orderId);
		orderShipping.setUpdated(new Date());
		orderShipping.setCreated(new Date());
		orderShippingMapper.insert(orderShipping);
		return E3mallResult.ok(orderId);
	}

}
