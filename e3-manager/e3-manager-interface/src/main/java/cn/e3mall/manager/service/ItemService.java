package cn.e3mall.manager.service;

import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbItemDesc;

public interface ItemService {
	
	public TbItem getTbItemById(long itemId);
	//查询商品列表
	public EasyUIDataGridResult getItemList(int page,int rows);
	//添加商品
	public E3mallResult addItem(TbItem item,String desc);
	//删除商品
	public E3mallResult deleteItem(String ids);
	//修改下架
	public E3mallResult productShelves(String ids);
	//修改上架
	public E3mallResult productReshelf(String ids);
	//编辑商品回显
	public TbItemDesc selectTbItemDesc(long id);
	//编辑(修改)商品
	public E3mallResult updateItem(TbItem item,String desc);
	//查询商品详情，这是给e3-item-web提供的
	public TbItemDesc getTbItemDescById(long itemId);

}
