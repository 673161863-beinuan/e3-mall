package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.e3mall.common.entity.EasyUIDataGridResult;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.entity.TbContent;
import cn.e3mall.entity.TbContentExample;
import cn.e3mall.entity.TbContentExample.Criteria;
import cn.e3mall.mapper.TbContentMapper;
import redis.clients.jedis.JedisCluster;

/**
 * 内容管理service
 * 
 * @author Administrator
 *
 */

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentmapper;

	// 注入JedisClient
	@Autowired
	private JedisClient jedisClient;

	@Value("${CONETNT_KEY}")
	private String CONETNT_KEY;
	
	//删除使得缓存同步
	@Value("${DEL_CONETNT_VAL}")
	private String DEL_CONETNT_VAL;

	/**
	 * 查询内容管理
	 */
	// 设置分页
	@Override
	public EasyUIDataGridResult getList(int page, int rows,long categoryId) {
		
		// 设置分页信息
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		// 执行查询
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentmapper.selectByExample(example);
		// 创建返回对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		// 取分页结果
		result.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		
		return result;
		}
	/**
	 * 增加内容管理内容
	 * 
	 * 解决缓存同步 对内容信息做增删改操作后只需要把对应缓存删除即可。可以根据cid删除
	 */
	@Override
	public E3mallResult addContent(TbContent content) {
		// 补全属性
		content.setUpdated(new Date());
		content.setCreated(new Date());
		// 执行添加
		contentmapper.insert(content);
		// 缓存同步。
		jedisClient.hdel(CONETNT_KEY, content.getCategoryId().toString());
		return E3mallResult.ok();
	}

	/**
	 * 修改内容管理内容
	 */
	@Override
	public E3mallResult updateContent(TbContent content) {
		// 补全属性
		content.setUpdated(new Date());
		content.setCreated(new Date());
		// 执行修改(编辑)
		contentmapper.updateByPrimaryKey(content);

		// 缓存同步
		jedisClient.hdel(CONETNT_KEY, content.getCategoryId().toString());
		return E3mallResult.ok();

	}

	/**
	 * 删除内容管理内容
	 */
	// 删除内容
	@Override
	public E3mallResult deleteContent(long ids) {
		contentmapper.deleteByPrimaryKey(ids);
		// 缓存同步
		jedisClient.hdel(CONETNT_KEY, DEL_CONETNT_VAL);
		return E3mallResult.ok();
	}

	/**
	 * 通过id查询内容 首页展示轮播图
	 * 
	 */
	@Override
	public List<TbContent> getContentListByid(long cid) {
		// 查询reids缓存
		try {
			String json = jedisClient.hget(CONETNT_KEY, cid + "");
			// 判断json是否为空
			if (StringUtils.isNotBlank(json)) {
				// 不为空，把json转换成字符串
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				// 返回结果
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		// 执行查询
		List<TbContent> list = contentmapper.selectByExampleWithBLOBs(example);
		// 添加到redis缓存中
		try {
			// JsonUtils.objectToJson(list) 把list转换成json
			jedisClient.hset(CONETNT_KEY, cid + "", JsonUtils.objectToJson(list));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
