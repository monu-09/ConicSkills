package com.conicskill.app.data.model.payments;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemListItem implements Serializable {

	@SerializedName("itemType")
	private String itemType;

	@SerializedName("courseId")
	private String courseId;

	@SerializedName("testSeriesId")
	private String testSeriesId;

	public void setItemType(String itemType){
		this.itemType = itemType;
	}

	public String getItemType(){
		return itemType;
	}

	public void setCourseId(String courseId){
		this.courseId = courseId;
	}

	public String getCourseId(){
		return courseId;
	}

	public String getTestSeriesId() {
		return testSeriesId;
	}

	public void setTestSeriesId(String testSeriesId) {
		this.testSeriesId = testSeriesId;
	}

	@Override
 	public String toString(){
		return 
			"ItemListItem{" + 
			"itemType = '" + itemType + '\'' + 
			",courseId = '" + courseId + '\'' + 
			"}";
		}
}