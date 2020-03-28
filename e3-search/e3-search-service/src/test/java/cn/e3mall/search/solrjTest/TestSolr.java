package cn.e3mall.search.solrjTest;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {

	// 添加文档
	@Test
	public void addsolr() throws Exception {
		// 创建一个solrServer对象,创建一个链接，参数是solr的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		// 创建SolrInputDocument对象
		SolrInputDocument document = new SolrInputDocument();
		// 象文档中添加域，必须包含一个id域，所有的域都是必须在scham.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 20000);
		// 把文档写入索引库
		solrServer.add(document);
		// 提交
		solrServer.commit();
	}

	// 更新文档
	// 与添加文档一致 保证id域一样即可
	@Test
	public void updateSolr() throws Exception {
		// 创建一个SolrServer对象,创建一个链接，参数是solr的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		// 创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		// 向文档对象中添加域。文档中必须包含一个id域，所有的域名城必须是在schema.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品02");
		document.addField("item_price", 2000);
		// 把文档写入索引库
		solrServer.add(document);
		// 提交
		solrServer.commit();
	}

	// 删除
	@Test
	public void deleteSolr() throws Exception {
		// 创建一个SolrServer对象,创建一个链接，参数是solr的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		// 删除的id名称
		solrServer.deleteById("doc01");
		// 提交
		solrServer.commit();
	}

	@Test
	public void queryItemList() throws Exception {
		// 创建连接
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		// 创建查询对象
		SolrQuery query = new SolrQuery();
		// 查询条件
		query.setQuery("*:*");
		// 执行查询
		QueryResponse response = solrServer.query(query);
		SolrDocumentList results = response.getResults();
		System.out.println(results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_image"));
		}
	}
/*
	@Test
	public void queryItemFuZa() throws Exception {
		// 创建连接
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		// 创建查询对象
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		query.setQuery("测试");
		// 分页起始索引
		query.setStart(0);
		// 分页显示多少条
		query.setRows(20);
		// 设置默认查询
		query.set("df", "item_title");
		// 开启高亮
		query.setHighlight(true);
		// 高亮显示的域
		query.addHighlightField("item_title");
		// 高亮显示的前缀
		query.setHighlightSimplePre("<em>");
		// 高亮显示的后缀
		query.setHighlightSimplePost("</em>");
		// 执行查询
		QueryResponse queryResponse = solrServer.query(query);
		// 取文档列表,查询结果的总记录数
		SolrDocumentList documentList = queryResponse.getResults();
		System.out.println("总记录数" + documentList.getNumFound());
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			// 取高亮
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get("id").get("item_title");
			String title = null;
			if (list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_image"));
		}
	}*/

	@Test
	public void queryDocumentWithHighLighting() throws Exception {
		// 第一步：创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr");
		// 第二步：创建一个SolrQuery对象。
		SolrQuery query = new SolrQuery();
		// 第三步：向SolrQuery中添加查询条件、过滤条件
		query.setQuery("手机");
		// 指定默认搜索域
		query.set("df", "item_title");
		// 开启高亮显示
		query.setHighlight(true);
		// 高亮显示的域
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		// 第四步：执行查询。得到一个Response对象。
		QueryResponse response = solrServer.query(query);
		// 第五步：取查询结果。
		SolrDocumentList solrDocumentList = response.getResults();
		System.out.println("查询结果的总记录数：" + solrDocumentList.getNumFound());
		// 第六步：遍历结果并打印。
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			// 取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = null;
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			System.out.println(itemTitle);
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_image"));
		}
	}

	// 使用solrJ实现查询
	@Test
	public void queryDocument() throws Exception {
		// 创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr");
		// 创建一个查询对象，可以参考solr的后台的查询功能设置条件
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		// query.setQuery("阿尔卡特");
		query.set("q", "手机");
		// 设置分页条件
		query.setStart(1);
		query.setRows(20);
		// 开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		// 设置默认搜索域
		query.set("df", "item_title");
		// 执行查询，得到一个QueryResponse对象。
		QueryResponse queryResponse = solrServer.query(query);
		// 取查询结果总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
		// 取查询结果
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			// 取高亮后的结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list != null && list.size() > 0) {
				// 取高亮后的结果
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}

	}

}
