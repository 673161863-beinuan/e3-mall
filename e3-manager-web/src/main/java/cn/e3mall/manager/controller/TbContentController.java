package cn.e3mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.entity.TbContent;

/**
 * 内容管理controller
 * 
 * @author Administrator
 *
 */
@Controller
public class TbContentController {

	
	@Reference
	private ContentService contentService;
	
	//查询所有的后台内容管理的内容
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getList(Integer page,Integer rows,Long categoryId){
		EasyUIDataGridResult list = contentService.getList(page, rows,categoryId);
		return list;
		
	}

	//添加内容管理的内容
	@RequestMapping("/content/save")
	@ResponseBody
	public E3mallResult addContent(TbContent content) {
		E3mallResult addContent = contentService.addContent(content);
		return addContent;
	}
	//修改内容管理的内容
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3mallResult updateContent(TbContent content){
		E3mallResult e3mallResult = contentService.updateContent(content);
		return e3mallResult;
		
		
	}
	
	//删除内容管理的内容
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3mallResult deleteContent(long ids){
		return contentService.deleteContent(ids);
		
	}

}
