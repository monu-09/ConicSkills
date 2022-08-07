package com.conicskill.app.data.model.saveTest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestObject{

	@SerializedName("feedbackData")
	private List<Object> feedbackData;

	@SerializedName("candidateResponse")
	private List<CandidateResponseItem> candidateResponse;

	@SerializedName("startTime")
	private String startTime;

	@SerializedName("endTime")
	private String endTime;

	@SerializedName("registrationData")
	private List<Object> registrationData;

	@SerializedName("isCompleted")
	private Integer isCompleted;

	public List<Object> getFeedbackData(){
		return feedbackData;
	}

	public List<CandidateResponseItem> getCandidateResponse(){
		return candidateResponse;
	}

	public String getStartTime(){
		return startTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public List<Object> getRegistrationData(){
		return registrationData;
	}

	public Integer getIsCompleted(){
		return isCompleted;
	}

	public void setFeedbackData(List<Object> feedbackData) {
		this.feedbackData = feedbackData;
	}

	public void setCandidateResponse(List<CandidateResponseItem> candidateResponse) {
		this.candidateResponse = candidateResponse;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setRegistrationData(List<Object> registrationData) {
		this.registrationData = registrationData;
	}

	public void setIsCompleted(Integer isCompleted) {
		this.isCompleted = isCompleted;
	}
}