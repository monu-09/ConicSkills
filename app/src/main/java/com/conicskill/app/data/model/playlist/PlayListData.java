package com.conicskill.app.data.model.playlist;

import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayListData {

	@SerializedName("todaysClass")
	private List<VideoListItem> todayLiveClass;

	@SerializedName("categoryList")
	private List<CategoryListItem> categoryList;

	public void setCategoryList(List<CategoryListItem> categoryList){
		this.categoryList = categoryList;
	}

	public List<CategoryListItem> getCategoryList(){
		return categoryList;
	}

	public List<VideoListItem> getTodayLiveClass() {
		return todayLiveClass;
	}

	public void setTodayLiveClass(List<VideoListItem> todayLiveClass) {
		this.todayLiveClass = todayLiveClass;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"categoryList = '" + categoryList + '\'' + 
			"}";
		}
}