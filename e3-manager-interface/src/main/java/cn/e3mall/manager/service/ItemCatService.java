package cn.e3mall.manager.service;

import java.util.List;

import cn.e3mall.common.entity.EasyUITreeNode;

public interface ItemCatService {
	
	public List<EasyUITreeNode> getItemCatList(long parentId);

}
