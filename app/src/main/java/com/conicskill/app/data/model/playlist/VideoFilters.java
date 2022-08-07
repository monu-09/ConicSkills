package com.conicskill.app.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class VideoFilters {

	@SerializedName("videoCategory")
	private String videoCategory;

	@SerializedName("videoSubCategory")
	private String videoSubCategory;

	@SerializedName("freeOnly")
	private String freeOnly;

	public void setVideoCategory(String videoCategory){
		this.videoCategory = videoCategory;
	}

	public String getVideoCategory(){
		return videoCategory;
	}

	public String getVideoSubCategory() {
		return videoSubCategory;
	}

	public void setVideoSubCategory(String videoSubCategory) {
		this.videoSubCategory = videoSubCategory;
	}

	public String getFreeOnly() {
		return freeOnly;
	}

	public void setFreeOnly(String freeOnly) {
		this.freeOnly = freeOnly;
	}

	@Override
 	public String toString(){
		return 
			"Filters{" + 
			"videoCategory = '" + videoCategory + '\'' + 
			"}";
		}
}