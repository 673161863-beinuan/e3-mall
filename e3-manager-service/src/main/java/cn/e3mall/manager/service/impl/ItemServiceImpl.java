package cn.e3mall.manager.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbItemDesc;
import cn.e3mall.entity.TbItemExample;
import cn.e3mall.entity.TbItemExample.Criteria;
import cn.e3mall.manager.service.ItemService;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;

/**
 * 商品管理 service
 * 
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Override
	public TbItem getTbItemById(long itemId) {
		// 根据主键查询
		// TbItem item = tbItemMapper.selectByPrimaryKey(ItemId);
		// 根据条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		// 创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		// 取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}
	//增加商品
	@Override
	public E3mallResult addItem(TbItem item, String desc) {
		// 生成商品id
		long itemId = IDUtils.genItemId();
		// 补全item的属性
		item.setId(itemId);
		// 商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		tbItemMapper.insert(item);
		// 创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		tbItemDescMapper.insert(itemDesc);
		// 返回成功
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult deleteItem(String ids) {
		/// 判断ids不为空
		if (StringUtils.isNotBlank(ids)) {
			// 分割ids
			String[] split = ids.split(",");
			for (String id : split) {
				tbItemMapper.deleteByPrimaryKey(Long.valueOf(id));
				tbItemDescMapper.deleteByPrimaryKey(Long.valueOf(id));
			}
			return E3mallResult.ok();
		}
		return null;

	}

	// 下架
	@Override
	public E3mallResult productShelves(String ids) {
		// 判断ids不为空
		if (StringUtils.isNoneBlank(ids)) {
			String[] split = ids.split(",");
			// 遍历成一个个的id进行修改下架
			for (String id : split) {
				// 通过id查询到商品信息
				TbItem item = tbItemMapper.selectByPrimaryKey(Long.valueOf(id));
				// 商品状态，1-正常，2-下架，3-删除
				item.setStatus((byte) 2);
				// 保存
				tbItemMapper.updateByPrimaryKey(item);
			}
			return E3mallResult.ok();
		}
		return null;

	}

	// 上架
	@Override
	public E3mallResult productReshelf(String ids) {
		// 判断ids不为空
		if (StringUtils.isNoneBlank(ids)) {
			String[] split = ids.split(",");
			// 遍历成一个个的id进行修改下架
			for (String id : split) {
				// 通过id查询到商品信息
				TbItem item = tbItemMapper.selectByPrimaryKey(Long.valueOf(id));
				// 商品状态，1-正常，2-下架，3-删除
				item.setStatus((byte) 1);
				// 保存
				tbItemMapper.updateByPrimaryKey(item);
			}
			return E3mallResult.ok();
		}
		return null;

	}
	//编辑回显查询
	@Override
	public TbItemDesc selectTbItemDesc(long id) {
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(id);
		return itemDesc;
	}
	 //编辑商品
	@Override
	public E3mallResult updateItem(TbItem item, String desc) {
		//修改商品的内容
		//补全内容
		// 商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		tbItemMapper.updateByPrimaryKey(item);
		// 创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全TbItemDesc的属性
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		tbItemDescMapper.updateByPrimaryKey(itemDesc);
		//返回成功
		return E3mallResult.ok();
	}
	

}
