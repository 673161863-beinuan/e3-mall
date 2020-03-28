package cn.e3mall.manager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.entity.EasyUITreeNode;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.entity.TbContent;

/**
 * 内容分类管理
 * 
 * @author 赵明磊
 *
 */
@Controller
public class ContentCatController {

	@Reference
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name = "id", defaultValue = "0") Long id) {
		List<EasyUITreeNode> list = contentCategoryService.getContentCat(id);
		return list;

	}

	// 增加分类节点
	@RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
	@ResponseBody
	public E3mallResult addContentCategory(Long parentId, String name) {
		E3mallResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}

	// 修改(重命名)
	@RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
	@ResponseBody
	public E3mallResult updateContentCategory(Long id, String name) {
		E3mallResult e3mallResult = contentCategoryService.updateContentCategory(id, name);
		return e3mallResult;

	}

	// 删除分类
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3mallResult deleteContentCategory(Long id) {
		E3mallResult result = contentCategoryService.deleteContentCategory(id);
		return result;

	}
}
