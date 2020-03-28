package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.entity.TbContent;

/**
 * 首页展示controller
 * @author Administrator
 *
 */

@Controller
public class IndexController {

	@Reference
	private ContentService contentService;
	
	@Value("${CONTENT_LUNBO}")
	private long content_lunbo;
	@RequestMapping("/index")
	public String showIndex(Model model){
		List<TbContent> adList = contentService.getContentListByid(content_lunbo);
		model.addAttribute("ad1List", adList);
		return "index";
	}
}
