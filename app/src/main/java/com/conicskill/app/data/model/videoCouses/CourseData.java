package com.conicskill.app.data.model.videoCouses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseData {

	@SerializedName("candidateCourseCount")
	private String courseCount;

	@SerializedName("candidateCourseList")
	private List<CourseListItem> courseList;

	public void setCourseCount(String courseCount){
		this.courseCount = courseCount;
	}

	public String getCourseCount(){
		return courseCount;
	}

	public void setCourseList(List<CourseListItem> courseList){
		this.courseList = courseList;
	}

	public List<CourseListItem> getCourseList(){
		return courseList;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"courseCount = '" + courseCount + '\'' + 
			",courseList = '" + courseList + '\'' + 
			"}";
		}
}