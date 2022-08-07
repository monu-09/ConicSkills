package com.conicskill.app.data.model.testListing;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("testList")
	private List<TestListItem> testList;

	@SerializedName("testCount")
	private String testCount;

	public void setTestList(List<TestListItem> testList){
		this.testList = testList;
	}

	public List<TestListItem> getTestList(){
		return testList;
	}

	public void setTestCount(String testCount){
		this.testCount = testCount;
	}

	public String getTestCount(){
		return testCount;
	}

	@Override
 	public String toString(){
		return 
			"CandidateLoginData{" +
			"testList = '" + testList + '\'' + 
			",testCount = '" + testCount + '\'' + 
			"}";
		}
}