 package cn.e3mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchItemController {

	@Reference(timeout=60000)
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3mallResult importAllItem(){
		E3mallResult result = searchItemService.importAllItem();
		return result;
		
	}
}
