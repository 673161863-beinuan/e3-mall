package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.entity.EasyUITreeNode;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.entity.TbContentCategory;
import cn.e3mall.entity.TbContentCategoryExample;
import cn.e3mall.entity.TbContentCategoryExample.Criteria;
import cn.e3mall.mapper.TbContentCategoryMapper;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	
	@Autowired
	private  TbContentCategoryMapper tbContentCategoryMapper;
	//查询内容
	@Override
	public List<EasyUITreeNode> getContentCat(long parentId) {
		//parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> catList = tbContentCategoryMapper.selectByExample(example);
		//装换成EasyUITreeNode的列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到列表
			nodeList.add(node);
			
		}
		return nodeList;
	}

	
	//增加
	@Override
	public E3mallResult addContentCategory(Long parentId, String name) {
		
		//创建一个与数据库表对象的实体对象
		TbContentCategory contentCategory = new TbContentCategory();
		//设置实体属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//默认排序
		contentCategory.setSortOrder(1);
		// 1(正常) 2(删除)
		contentCategory.setStatus(1);
		//新添加的节点一定是叶子结点
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库
		tbContentCategoryMapper.insert(contentCategory);
		//判断父节点的parentId的属性  如果不是true改为true
		//根据parentId查询父节点
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			//跟新到数据库
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		//返回结果,包含实体
		return E3mallResult.ok(contentCategory);
	}

	//修改(重命名)
	@Override
	public E3mallResult updateContentCategory(Long id, String name) {
		TbContentCategory content = tbContentCategoryMapper.selectByPrimaryKey(id);
		//设置实体属性
		content.setName(name);
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入到数据库
		tbContentCategoryMapper.updateByPrimaryKey(content);
		//返回结果,包含实体
		return E3mallResult.ok();
		
	}

	@Override
	public E3mallResult deleteContentCategory(Long id) {
		//通过id查询准备删除的列
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(id);
		//返回结果
		return E3mallResult.ok();
	}

}
