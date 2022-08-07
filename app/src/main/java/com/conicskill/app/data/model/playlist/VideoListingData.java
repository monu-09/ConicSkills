package com.conicskill.app.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class VideoListingData {

	@SerializedName("filters")
	private VideoFilters filters;

	@SerializedName("courseId")
	private String courseId;

	public void setFilters(VideoFilters filters){
		this.filters = filters;
	}

	public VideoFilters getFilters(){
		return filters;
	}

	public void setCourseId(String courseId){
		this.courseId = courseId;
	}

	public String getCourseId(){
		return courseId;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"filters = '" + filters + '\'' + 
			",courseId = '" + courseId + '\'' + 
			"}";
		}
}