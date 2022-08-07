package com.conicskill.app.data.model.candidateLogin;

import com.google.gson.annotations.SerializedName;

public class CandidateLoginData {

	@SerializedName("expiryDate")
	private Object expiryDate;

	@SerializedName("authToken")
	private String authToken;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("expiryTime")
	private int expiryTime;

	@SerializedName("candidateId")
	private String candidateId;

	@SerializedName("email")
	private String email;

	public void setExpiryDate(Object expiryDate){
		this.expiryDate = expiryDate;
	}

	public Object getExpiryDate(){
		return expiryDate;
	}

	public void setAuthToken(String authToken){
		this.authToken = authToken;
	}

	public String getAuthToken(){
		return authToken;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setExpiryTime(int expiryTime){
		this.expiryTime = expiryTime;
	}

	public int getExpiryTime(){
		return expiryTime;
	}

	public void setCandidateId(String candidateId){
		this.candidateId = candidateId;
	}

	public String getCandidateId(){
		return candidateId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"CandidateLoginData{" +
			"expiryDate = '" + expiryDate + '\'' + 
			",authToken = '" + authToken + '\'' + 
			",name = '" + name + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",expiryTime = '" + expiryTime + '\'' + 
			",candidateId = '" + candidateId + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}