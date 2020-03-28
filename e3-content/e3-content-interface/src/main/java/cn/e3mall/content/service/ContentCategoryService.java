package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.entity.EasyUITreeNode;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbContent;

public interface ContentCategoryService {

	//查询内容列表
	public List<EasyUITreeNode> getContentCat(long parentId); 
	//增加叶子节点
	public E3mallResult  addContentCategory(Long parentId,String name);
	//修改分类名称
	public E3mallResult  updateContentCategory(Long id, String name);
	//删除分类
	public E3mallResult deleteContentCategory(Long id);
	//zengjia
}
