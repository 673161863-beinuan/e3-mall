package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbUser;
import cn.e3mall.order.entity.OrderInfo;
import cn.e3mall.order.service.OrderService;

/**
 * 订单处理controller
 * @author 赵明磊 
 * 2019年10月12日下午5:31:42
 */

@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> list = cartService.getCart(user.getId());
		request.setAttribute("cartList", list);
		return "order-cart";
		
	}
	
	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
		//取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//把用户信息添加到orderInfo中。
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//调用服务生成订单
		E3mallResult e3Result = orderService.createOrder(orderInfo);
		//如果订单生成成功，需要删除购物车
		if (e3Result.getStatus() == 200) {
			//清空购物车
			cartService.clearCart(user.getId());
		}
		//把订单号传递给页面
		request.setAttribute("orderId", e3Result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		//返回逻辑视图
		return "success";
	}

}
