package com.conicskill.app.data.model.videoCouses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpcomingData {

	@SerializedName("classCount")
	private String classCount;

	@SerializedName("previousLiveClass")
	private List<VideoListItem> previousLiveClass;

	@SerializedName("todayLiveClass")
	private List<VideoListItem> todayLiveClass;

	public void setClassCount(String classCount){
		this.classCount = classCount;
	}

	public String getClassCount(){
		return classCount;
	}

	public void setPreviousLiveClass(List<VideoListItem> previousLiveClass){
		this.previousLiveClass = previousLiveClass;
	}

	public List<VideoListItem> getPreviousLiveClass(){
		return previousLiveClass;
	}

	public void setTodayLiveClass(List<VideoListItem> todayLiveClass){
		this.todayLiveClass = todayLiveClass;
	}

	public List<VideoListItem> getTodayLiveClass(){
		return todayLiveClass;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"classCount = '" + classCount + '\'' + 
			",previousLiveClass = '" + previousLiveClass + '\'' + 
			",todayLiveClass = '" + todayLiveClass + '\'' + 
			"}";
		}
}