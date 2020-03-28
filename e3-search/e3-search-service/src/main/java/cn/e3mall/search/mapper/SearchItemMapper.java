package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.entity.SearchItem;

public interface SearchItemMapper {
	public List<SearchItem> getItemList();
	
	//添加商品同步到索引库
	public SearchItem getItemById(long itemId);

}
