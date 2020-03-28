package cn.e3mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbItemDesc;
import cn.e3mall.manager.service.ItemService;

/**
 * 商品管理controller
 * 
 * @author Administrator
 *
 */
@Controller
public class ItemController {

	@Reference
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	@ResponseBody()
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem item = itemService.getTbItemById(itemId);
		return item;
	}

	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;

	}

	// 增加商品
	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	public E3mallResult addItem(TbItem item, String desc) {
		E3mallResult result = itemService.addItem(item, desc);
		return result;

	}

	// 删除 (批量)
	@RequestMapping(value = "/rest/item/delete", method = RequestMethod.POST)
	@ResponseBody
	public E3mallResult deleteItem(String ids) {
		E3mallResult itemResult = itemService.deleteItem(ids);
		return itemResult;

	}

	// 下架
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3mallResult productShelves(String ids) {
		E3mallResult result = itemService.productShelves(ids);
		return result;

	}

	// 上架
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3mallResult productReshelf(String ids) {
		E3mallResult result = itemService.productReshelf(ids);
		return result;

	}

	// 异步重新加载回显描述
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public TbItemDesc selectTbItemDesc(@PathVariable long id) {
		TbItemDesc itemDesc = itemService.selectTbItemDesc(id);
		return itemDesc;
	}

	// 异步重新加载商品信息
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public TbItem queryById(@PathVariable long id) {
		TbItem item = itemService.getTbItemById(id);
		return item;
	}

	// 修改商品信息
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3mallResult updateItem(TbItem item, String desc) {
		E3mallResult result = itemService.updateItem(item, desc);
		return result;

	}

}