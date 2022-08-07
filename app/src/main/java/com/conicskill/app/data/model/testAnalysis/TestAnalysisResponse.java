package com.conicskill.app.data.model.testAnalysis;

import com.google.gson.annotations.SerializedName;

public class TestAnalysisResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private TestAnalysisResponseData data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	public int getCode(){
		return code;
	}

	public TestAnalysisResponseData getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public boolean isError(){
		return error;
	}

	public String getStatus(){
		return status;
	}
}