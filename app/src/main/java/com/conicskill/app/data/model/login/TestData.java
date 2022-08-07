package com.conicskill.app.data.model.login;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TestData{

	@SerializedName("testTypeId")
	@ColumnInfo(name = "testTypeId")
	private String testTypeId;

	@SerializedName("createdBy")
	@ColumnInfo(name = "createdBy")
	private String createdBy;

	@SerializedName("allowedModules")
	@ColumnInfo(name = "allowedModules")
	private String allowedModules;

	@SerializedName("active")
	@ColumnInfo(name = "active")
	private String active;

	@NonNull
	@PrimaryKey
	@SerializedName("testId")
	@ColumnInfo(name = "testId")
	private String testId;

	@SerializedName("startTime")
	@ColumnInfo(name = "startTime")
	private String startTime;

	@SerializedName("endTime")
	@ColumnInfo(name = "endTime")
	private String endTime;

	@SerializedName("testDuration")
	@ColumnInfo(name = "testDuration")
	private String testDuration;

	@SerializedName("testName")
	@ColumnInfo(name = "testName")
	private String testName;

	@SerializedName("createDateTime")
	@ColumnInfo(name = "createDateTime")
	private String createDateTime;

	public String getTestTypeId() {
		return testTypeId;
	}

	public void setTestTypeId(String testTypeId) {
		this.testTypeId = testTypeId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAllowedModules() {
		return allowedModules;
	}

	public void setAllowedModules(String allowedModules) {
		this.allowedModules = allowedModules;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(String testDuration) {
		this.testDuration = testDuration;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	@Override
 	public String toString(){
		return 
			"TestData{" + 
			"testTypeId = '" + testTypeId + '\'' + 
			",createdBy = '" + createdBy + '\'' + 
			",allowedModules = '" + allowedModules + '\'' + 
			",active = '" + active + '\'' + 
			",testId = '" + testId + '\'' + 
			",startTime = '" + startTime + '\'' + 
			",endTime = '" + endTime + '\'' + 
			",testDuration = '" + testDuration + '\'' + 
			",testName = '" + testName + '\'' + 
			",createDateTime = '" + createDateTime + '\'' + 
			"}";
		}
}