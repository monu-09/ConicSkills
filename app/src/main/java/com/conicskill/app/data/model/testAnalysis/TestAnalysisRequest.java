package com.conicskill.app.data.model.testAnalysis;

import com.google.gson.annotations.SerializedName;

public class TestAnalysisRequest{

	@SerializedName("data")
	private TestAnalysisRequestData data;

	@SerializedName("token")
	private String token;

	public TestAnalysisRequestData getData(){
		return data;
	}

	public String getToken(){
		return token;
	}

	public void setData(TestAnalysisRequestData data) {
		this.data = data;
	}

	public void setToken(String token) {
		this.token = token;
	}
}