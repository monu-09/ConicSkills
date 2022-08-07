package com.conicskill.app.data.model.videoCouses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(tableName = "Courses")
public class CourseListItem implements Diffable, Serializable {

	@SerializedName("duration")
	@ColumnInfo(name = "duration")
	private String duration;

	@SerializedName("createdAt")
	@ColumnInfo(name = "createdAt")
	private String createdAt;

	@SerializedName("companyId")
	@ColumnInfo(name = "companyId")
	private String companyId;

	@SerializedName("courseName")
	@ColumnInfo(name = "courseName")
	private String courseName;

	@SerializedName("thumbnail")
	@ColumnInfo(name = "thumbnail")
	private String thumbnail;

	@SerializedName("lectureCount")
	@ColumnInfo(name = "lectureCount")
	private String lectureCount;

	@SerializedName("createdBy")
	@ColumnInfo(name = "createdBy")
	private String createdBy;

	@SerializedName("price")
	@ColumnInfo(name = "price")
	private String price;

	@SerializedName("description")
	@ColumnInfo(name = "description")
	private String description;

	@SerializedName("active")
	@ColumnInfo(name = "active")
	private String active;

	@NonNull
	@PrimaryKey
	@SerializedName("courseId")
	@ColumnInfo(name = "courseId")
	private String courseId;

	@SerializedName("purchased")
	@ColumnInfo(name = "purchased")
	private String purchased;

	@SerializedName("isNew")
	@ColumnInfo(name = "isNew")
	private String isNew;

	@SerializedName("isLive")
	@ColumnInfo(name = "isLive")
	private String isLive;

	@SerializedName("mrp")
	@ColumnInfo(name = "mrp")
	private String mrp;

	@SerializedName("discountPercent")
	@ColumnInfo(name = "discountPercent")
	private String discountPercent;

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

	public void setThumbnail(String thumbnail){
		this.thumbnail = thumbnail;
	}

	public String getThumbnail(){
		return thumbnail;
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

	public void setCourseId(String courseId){
		this.courseId = courseId;
	}

	public String getCourseId(){
		return courseId;
	}

	public String getPurchased() {
		return purchased;
	}

	public void setPurchased(String purchased) {
		this.purchased = purchased;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getIsLive() {
		return isLive;
	}

	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	@Override
 	public String toString(){
		return 
			"CourseListItem{" + 
			"duration = '" + duration + '\'' + 
			",createdAt = '" + createdAt + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",courseName = '" + courseName + '\'' + 
			",thumbnail = '" + thumbnail + '\'' + 
			",lectureCount = '" + lectureCount + '\'' + 
			",createdBy = '" + createdBy + '\'' + 
			",price = '" + price + '\'' + 
			",description = '" + description + '\'' + 
			",active = '" + active + '\'' + 
			",courseId = '" + courseId + '\'' + 
			",tags = '" + "" + '\'' +
			"}";
		}

	@Override
	public boolean areContentTheSame(@NotNull Object o) {
		return false;
	}

	@Override
	public long getUniqueIdentifier() {
		return System.identityHashCode(this);
	}
}