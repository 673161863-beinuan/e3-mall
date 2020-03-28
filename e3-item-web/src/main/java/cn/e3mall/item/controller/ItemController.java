package cn.e3mall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbItemDesc;
import cn.e3mall.item.entity.Item;
import cn.e3mall.manager.service.ItemService;

@Controller
public class ItemController {
	
	@Reference
	private ItemService itemService;

	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		//调用服务取商品信息
		TbItem tbItem = itemService.getTbItemById(itemId);
		//System.out.println(itemId);
		System.out.println(tbItem);
		Item item = new Item(tbItem);
		//取商品描述信息
		TbItemDesc itemDesc = itemService.getTbItemDescById(itemId);
		//把信息传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		//返回逻辑视图
		return "item";
		
	}
}
