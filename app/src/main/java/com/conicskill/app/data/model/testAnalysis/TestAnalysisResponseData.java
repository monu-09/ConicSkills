package com.conicskill.app.data.model.testAnalysis;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestAnalysisResponseData {

	@SerializedName("testData")
	private TestData testData;

	@SerializedName("moduleData")
	private List<ModuleDataItem> moduleData;

	public TestData getTestData(){
		return testData;
	}

	public List<ModuleDataItem> getModuleData(){
		return moduleData;
	}
}