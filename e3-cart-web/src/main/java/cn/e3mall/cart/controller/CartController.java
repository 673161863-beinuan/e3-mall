package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbUser;
import cn.e3mall.manager.service.ItemService;

/**
 * 购物车添加商品controller
 * 
 * @author Administrator
 *
 */
@Controller
public class CartController {

	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;

	@Value("${COOKIE_CART_TIME}")
	private Integer COOKIE_CART_TIME;

	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登陆状态，将信息写入redis
		if (user != null) {
			// 保存到服务端
			cartService.addItemToCart(itemId, user.getId(), num);
			return "cartSuccess";
		}
		// 如果未登录，使用cookie
		// 从cookie中查询商品列表
		List<TbItem> cartList = getCartList(request);
		boolean flag = false;
		// 判断商品是否在购物车列表中
		for (TbItem tbItem : cartList) {
			// 如果存在，商品数量增加
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				// 设置添加到购物车的商品数量
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}
		if (!flag) {
			// 不存在，根据商品id获取商品信息
			TbItem tbItem = itemService.getTbItemById(itemId);
			// 取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			tbItem.setNum(num);
			// 将商品添加到商品列表
			cartList.add(tbItem);
		}
		// 添加到cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_TIME, true);
		// 返回添加成功页面
		return "cartSuccess";
	}

	/**
	 * 在cookie中购物车列表
	 * 
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(json)) {
			// 如果为空
			return new ArrayList<>();
		}
		// 不为空
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;

	}

	// 展示购物车商品详情列表
	@RequestMapping("/cart/cart")
	public String showCartItem(HttpServletRequest request, HttpServletResponse response) {
		// 在cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		// 判断用户是否是登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 如果user不为空，用户是登录状态，将cookie中的购物车列表和服务端的列表合并
			cartService.mergeCart(user.getId(), cartList);
			// 把cookie中的商品删除
			CookieUtils.deleteCookie(request, response, "cart");
			// 在服务端获取购物列表
			cartList = cartService.getCart(user.getId());
		}
		request.setAttribute("cartList", cartList);
		return "cart";

	}

	// 删除购物车中的商品
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCart(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// 获取到购物车列表
		List<TbItem> cartList = getCartList(request);
		// 遍历列表，找到要删除的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				// 删除
				cartList.remove(tbItem);
				break;
			}
		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_TIME, true);
		// 重定向到购物车列表redirect
		return "redirect:/cart/cart.html";
	}

	// 更新购物车中的商品数量
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3mallResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.updateCart(user.getId(), itemId, num);

			return E3mallResult.ok();
		}
		// 获取cookie中的购物车列表
		List<TbItem> cartList = getCartList(request);
		// 遍历列表
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				// 更新数量
				tbItem.setNum(num);
			}
			break;
		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_TIME, true);
		return E3mallResult.ok();

	}
}
