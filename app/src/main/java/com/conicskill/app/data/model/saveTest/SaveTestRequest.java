package com.conicskill.app.data.model.saveTest;

import com.google.gson.annotations.SerializedName;

public class SaveTestRequest{

	@SerializedName("data")
	private SaveTestData data;

	@SerializedName("language")
	private String language;

	@SerializedName("token")
	private String token;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	@SerializedName("code")
	private int code;

	public void setData(SaveTestData data){
		this.data = data;
	}

	public SaveTestData getData(){
		return data;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
 	public String toString(){
		return 
			"SaveTestRequest{" + 
			"data = '" + data + '\'' + 
			",language = '" + language + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}