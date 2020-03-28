package cn.e3mall.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.entity.EasyUITreeNode;
import cn.e3mall.manager.service.ItemCatService;

/**
 * 商品分类管理controller
 * 
 * @author Administrator
 */

@Controller
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;

	@RequestMapping("item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCat(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
		List<EasyUITreeNode> catList = itemCatService.getItemCatList(parentId);
		return catList;

	};
}
