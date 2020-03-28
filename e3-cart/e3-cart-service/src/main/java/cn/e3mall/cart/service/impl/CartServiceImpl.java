package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.entity.TbItem;
import cn.e3mall.mapper.TbItemMapper;
/**
 * 添加商品到购物车
 * @author 赵明磊
 *
 */
@Service
public class CartServiceImpl implements CartService {

	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Override
	public E3mallResult addItemToCart(long itemId, long userId, int num) {
		//向redis中添加购物车。
		//数据类型是hash key：用户id field：商品id value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		if(hexists){
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			//如果存在数量相加
			item.setNum(item.getNum() + num);
			//写会redis
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(json));
			return E3mallResult.ok();
		}
		//如果不存在，根据商品id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置数量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)){
			item.setImage(image.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		return E3mallResult.ok();
	}
	
	//合并购物车列表
	@Override
	public E3mallResult mergeCart(long userId, List<TbItem> itemList) {
		//遍历列表
		//把列表添加到购物车
		//如果有数量相加
		//如果没有，添加新的商品
		for (TbItem tbItem : itemList) {
			addItemToCart(tbItem.getId(), userId, tbItem.getNum());
		}
		return E3mallResult.ok();
	}
	//查询购物车列表
	@Override
	public List<TbItem> getCart(long userId) {
		//根据用户id查询缓存中的购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String tbItem : jsonList) {
			//将json转化为对象，创建TbItem对象
			TbItem item = JsonUtils.jsonToPojo(tbItem, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}
	
	//更新购物车商品的数量
	@Override
	public E3mallResult updateCart(long userId, long itemId, int num) {
		//获取购物车列表
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		//更新数量
		item.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		return E3mallResult.ok();
	}
	
	//删除购物车商品的商品
	@Override
	public E3mallResult deleteCart(long userId, long itemId) {
		//根据商品id删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return  E3mallResult.ok();
	}

	@Override
	public E3mallResult clearCart(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return E3mallResult.ok();
	}

}
