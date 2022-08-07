package com.conicskill.app.data.model.videoCouses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoData {

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("description")
	private String description;

	@SerializedName("active")
	private String active;

	@SerializedName("tags")
	private String tags;

	@SerializedName("duration")
	private String duration;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("courseName")
	private String courseName;

	@SerializedName("lectureCount")
	private String lectureCount;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("price")
	private String price;

	@SerializedName("courseVideo")
	private List<VideoListItem> videoList;

	@SerializedName("courseId")
	private String courseId;

	public void setThumbnail(String thumbnail){
		this.thumbnail = thumbnail;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setTags(String tags){
		this.tags = tags;
	}

	public String getTags(){
		return tags;
	}

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setCourseName(String courseName){
		this.courseName = courseName;
	}

	public String getCourseName(){
		return courseName;
	}

	public void setLectureCount(String lectureCount){
		this.lectureCount = lectureCount;
	}

	public String getLectureCount(){
		return lectureCount;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setVideoList(List<VideoListItem> videoList){
		this.videoList = videoList;
	}

	public List<VideoListItem> getVideoList(){
		return videoList;
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
			"thumbnail = '" + thumbnail + '\'' + 
			",description = '" + description + '\'' + 
			",active = '" + active + '\'' + 
			",tags = '" + tags + '\'' + 
			",duration = '" + duration + '\'' + 
			",createdAt = '" + createdAt + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",courseName = '" + courseName + '\'' + 
			",lectureCount = '" + lectureCount + '\'' + 
			",createdBy = '" + createdBy + '\'' + 
			",price = '" + price + '\'' + 
			",videoList = '" + videoList + '\'' + 
			",courseId = '" + courseId + '\'' + 
			"}";
		}
}