package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbContent;

public interface ContentService {
	//设置分页,通过categoryId查询内容
	public EasyUIDataGridResult getList(int page, int rows,long categoryId);
	//增加内容
	public E3mallResult addContent(TbContent content);
	//修改内容
	public E3mallResult updateContent(TbContent content);
	
	//删除内容
	//long cid 缓存同步
	public E3mallResult deleteContent(long ids);
	
	//查询内容展示轮播图
	public List<TbContent> getContentListByid(long cid);

}
