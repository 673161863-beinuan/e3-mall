package cn.e3mall.search.service;

import cn.e3mall.common.entity.SearchResult;

public interface SearchService{
	
	public SearchResult search(String keyword, int page,int rows)  throws Exception;

}
