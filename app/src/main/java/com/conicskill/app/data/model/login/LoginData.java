package com.conicskill.app.data.model.login;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("testData")
	private TestData testData;

	@SerializedName("lastModifiedAt")
	private String lastModifiedAt;

	@SerializedName("testAttemptId")
	private String testAttemptId;

	@SerializedName("authToken")
	private String authToken;

	@SerializedName("expiryTime")
	private int expiryTime;

	@SerializedName("testId")
	private String testId;

	@SerializedName("moduleData")
	private List<ModuleDataItem> moduleData;

	public void setTestData(TestData testData){
		this.testData = testData;
	}

	public TestData getTestData(){
		return testData;
	}

	public void setLastModifiedAt(String lastModifiedAt){
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedAt(){
		return lastModifiedAt;
	}

	public void setTestAttemptId(String testAttemptId){
		this.testAttemptId = testAttemptId;
	}

	public String getTestAttemptId(){
		return testAttemptId;
	}

	public void setAuthToken(String authToken){
		this.authToken = authToken;
	}

	public String getAuthToken(){
		return authToken;
	}

	public void setExpiryTime(int expiryTime){
		this.expiryTime = expiryTime;
	}

	public int getExpiryTime(){
		return expiryTime;
	}

	public void setTestId(String testId){
		this.testId = testId;
	}

	public String getTestId(){
		return testId;
	}

	public void setModuleData(List<ModuleDataItem> moduleData){
		this.moduleData = moduleData;
	}

	public List<ModuleDataItem> getModuleData(){
		return moduleData;
	}

	@Override
 	public String toString(){
		return 
			"LoginData{" +
			"testData = '" + testData + '\'' + 
			",lastModifiedAt = '" + lastModifiedAt + '\'' + 
			",testAttemptId = '" + testAttemptId + '\'' + 
			",authToken = '" + authToken + '\'' + 
			",expiryTime = '" + expiryTime + '\'' + 
			",testId = '" + testId + '\'' + 
			",moduleData = '" + moduleData + '\'' + 
			"}";
		}
}