package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbItem;

public interface CartService {
	
	//添加购物车
	E3mallResult addItemToCart(long itemId,long userId,int num);
	//合并cookie和用户登录状态的购物车列表
	E3mallResult mergeCart(long userId,List<TbItem> itemList);
	//查看购物车列表
	List<TbItem> getCart(long userId);
	//更新购物车列表
	E3mallResult updateCart(long userId,long itemId,int num);
	//删除购物车的商品
	E3mallResult deleteCart(long userId,long itemId);
	//清空购物车
	E3mallResult clearCart(long userId);

}
