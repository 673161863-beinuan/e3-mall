package cn.e3mall.common.entity;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{

	//总记录数
	private long recourdCount;
	//总页数
	private int totalPages;
	//商品列表
	private List<SearchItem> itemList;
	
	
	public long getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	

}
