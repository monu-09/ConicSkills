package com.conicskill.app.data.model.saveTest;

import com.google.gson.annotations.SerializedName;

public class SaveTestData {

	@SerializedName("testObjVersion")
	private String testObjVersion;

	@SerializedName("testObject")
	private TestObject testObject;

	public String getTestObjVersion(){
		return testObjVersion;
	}

	public TestObject getTestObject(){
		return testObject;
	}

	public void setTestObjVersion(String testObjVersion) {
		this.testObjVersion = testObjVersion;
	}

	public void setTestObject(TestObject testObject) {
		this.testObject = testObject;
	}
}