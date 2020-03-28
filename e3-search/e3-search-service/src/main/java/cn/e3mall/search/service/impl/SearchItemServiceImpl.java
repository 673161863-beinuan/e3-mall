package cn.e3mall.search.service.impl;


import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.e3mall.common.entity.SearchItem;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SearchItemService;


@Service(timeout=60000)
public class SearchItemServiceImpl implements SearchItemService {

	
	@Autowired
	private SearchItemMapper searchItemMapper; 
	
	@Autowired
	private SolrServer solrServer; 
	
	@Override
	public E3mallResult importAllItem() {
		try {
			//查询所有的商品数据
			List<SearchItem> list = searchItemMapper.getItemList();
			//遍历商品数据
			for (SearchItem search : list) {
			    //创建文档对象
				SolrInputDocument document = new  SolrInputDocument();
				//向文档对象中添加域
				document.addField("id", search.getId());
				document.addField("item_title", search.getTitle());
				document.addField("item_sell_point", search.getSell_point());
				document.addField("item_price", search.getPrice());
				document.addField("item_category_name", search.getCategory_name());
				document.addField("item_image", search.getImage());
				//添加到索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			//返回成功
			return E3mallResult.ok();
			
		} catch (Exception e) {
			e.printStackTrace();
			//返回失败信息
			return E3mallResult.build(500, "导入商品数据失败!");
		}
		
	}

}
