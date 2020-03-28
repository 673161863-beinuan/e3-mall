package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.entity.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * 商品搜索controller
 * @author Administrator
 *
 */

@Controller
public class SearchController {
	
	@Reference(timeout=60000)
	private SearchService searchService;

	@Value("${SEARCH_ROWS}")
	private Integer SEARCH_ROWS;
	
	@RequestMapping("/search")
	public String searchItemList(String keyword,@RequestParam(defaultValue="1")Integer page,
			Model model) throws Exception{
		
			keyword = new String(keyword.getBytes("iso-8859-1"), "UTF-8");
			SearchResult searchResult = searchService.search(keyword, page, SEARCH_ROWS);
			model.addAttribute("query", keyword);
			model.addAttribute("totalPages", searchResult.getTotalPages());
			model.addAttribute("page", page);
			model.addAttribute("recourdCount",searchResult.getRecourdCount());
			model.addAttribute("itemList",searchResult.getItemList());
			//返回逻辑视图
			//测试全局处理器
			//int a = 1/0;
			return "search";
	}
}
