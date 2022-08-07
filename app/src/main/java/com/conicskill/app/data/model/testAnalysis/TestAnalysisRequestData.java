package com.conicskill.app.data.model.testAnalysis;

import com.google.gson.annotations.SerializedName;

public class TestAnalysisRequestData {

	@SerializedName("sendModuleArrayValues")
	private String sendModuleArrayValues;

	public String getSendModuleArrayValues(){
		return sendModuleArrayValues;
	}

	public void setSendModuleArrayValues(String sendModuleArrayValues) {
		this.sendModuleArrayValues = sendModuleArrayValues;
	}
}