package com.conicskill.app.data.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest{

	@SerializedName("data")
	private LoginRequestData loginRequestData;

	@SerializedName("language")
	private String language;

	public void setLoginRequestData(LoginRequestData loginRequestData){
		this.loginRequestData = loginRequestData;
	}

	public LoginRequestData getLoginRequestData(){
		return loginRequestData;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	@Override
 	public String toString(){
		return 
			"LoginRequest{" + 
			"data = '" + loginRequestData + '\'' +
			",language = '" + language + '\'' + 
			"}";
		}
}