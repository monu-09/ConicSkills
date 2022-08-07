package com.conicskill.app.data.model.testListing;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

public class TestListItem implements Diffable {

	@SerializedName("loginId")
	private String loginId;

	@SerializedName("active")
	private String active;

	@SerializedName("testType")
	private String testType;

	@SerializedName("userName")
	private String userName;

	@SerializedName("createDateTime")
	private String createDateTime;

	@SerializedName("password")
	private String password;

	@SerializedName("testTypeId")
	private String testTypeId;

	@SerializedName("testAttemptId")
	private String testAttemptId;

	@SerializedName("isDeleted")
	private String isDeleted;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("allowedModules")
	private String allowedModules;

	@SerializedName("testId")
	private String testId;

	@SerializedName("startTime")
	private String startTime;

	@SerializedName("endTime")
	private String endTime;

	@SerializedName("candidateId")
	private String candidateId;

	@SerializedName("testDuration")
	private String testDuration;

	@SerializedName("testName")
	private String testName;

	@SerializedName("questionCount")
	private String questionCount;

	@SerializedName("testScore")
	private String testScore;

	@SerializedName("isLocked")
	private boolean isLocked;

	@SerializedName("isCompleted")
	private boolean isCompleted;

	public void setLoginId(String loginId){
		this.loginId = loginId;
	}

	public String getLoginId(){
		return loginId;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setTestType(String testType){
		this.testType = testType;
	}

	public String getTestType(){
		return testType;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setCreateDateTime(String createDateTime){
		this.createDateTime = createDateTime;
	}

	public String getCreateDateTime(){
		return createDateTime;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setTestTypeId(String testTypeId){
		this.testTypeId = testTypeId;
	}

	public String getTestTypeId(){
		return testTypeId;
	}

	public void setTestAttemptId(String testAttemptId){
		this.testAttemptId = testAttemptId;
	}

	public String getTestAttemptId(){
		return testAttemptId;
	}

	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}

	public String getIsDeleted(){
		return isDeleted;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setAllowedModules(String allowedModules){
		this.allowedModules = allowedModules;
	}

	public String getAllowedModules(){
		return allowedModules;
	}

	public void setTestId(String testId){
		this.testId = testId;
	}

	public String getTestId(){
		return testId;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setCandidateId(String candidateId){
		this.candidateId = candidateId;
	}

	public String getCandidateId(){
		return candidateId;
	}

	public void setTestDuration(String testDuration){
		this.testDuration = testDuration;
	}

	public String getTestDuration(){
		return testDuration;
	}

	public void setTestName(String testName){
		this.testName = testName;
	}

	public String getTestName(){
		return testName;
	}

	public String getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(String questionCount) {
		this.questionCount = questionCount;
	}

	public String getTestScore() {
		return testScore;
	}

	public void setTestScore(String testScore) {
		this.testScore = testScore;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	@Override
 	public String toString(){
		return 
			"TestListItem{" + 
			"loginId = '" + loginId + '\'' + 
			",active = '" + active + '\'' + 
			",testType = '" + testType + '\'' + 
			",userName = '" + userName + '\'' + 
			",createDateTime = '" + createDateTime + '\'' + 
			",password = '" + password + '\'' + 
			",testTypeId = '" + testTypeId + '\'' + 
			",testAttemptId = '" + testAttemptId + '\'' + 
			",isDeleted = '" + isDeleted + '\'' + 
			",createdBy = '" + createdBy + '\'' + 
			",allowedModules = '" + allowedModules + '\'' + 
			",testId = '" + testId + '\'' + 
			",startTime = '" + startTime + '\'' + 
			",endTime = '" + endTime + '\'' + 
			",candidateId = '" + candidateId + '\'' + 
			",testDuration = '" + testDuration + '\'' + 
			",testName = '" + testName + '\'' + 
			",testScore = '" + testScore + '\'' +
			",questionCount = '" + questionCount + '\'' +
			"}";
		}

	@Override
	public boolean areContentTheSame(@NotNull Object o) {
		return false;
	}

	@Override
	public long getUniqueIdentifier() {
		return Long.valueOf(testId);
	}
}