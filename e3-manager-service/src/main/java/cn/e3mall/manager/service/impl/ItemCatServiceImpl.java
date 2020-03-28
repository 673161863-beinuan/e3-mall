package cn.e3mall.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import cn.e3mall.common.entity.EasyUITreeNode;
import cn.e3mall.entity.TbItemCat;
import cn.e3mall.entity.TbItemCatExample;
import cn.e3mall.entity.TbItemCatExample.Criteria;
import cn.e3mall.manager.service.ItemCatService;
import cn.e3mall.entity.TbItemExample;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.mapper.TbItemMapper;
/**
 * 查看商品service
 * @author Administrator
 *
 */

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		//根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//创建返回结果list
		List<EasyUITreeNode> resultList = new ArrayList<>(); 
		//把列表转换成EasyUITreeNode列表
		for (TbItemCat tbItemCat: list) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			//设置属性
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(treeNode);
		}
		//返回结果
		return resultList;
	}

}
