package com.conicskill.app.data.model.examlisting;

import com.google.gson.annotations.SerializedName;

public class Filter{
	@SerializedName("testSeriesId")
	private String testsettingsid;

	@SerializedName("testSeriesType")
	private String testSeriesType;

	public void setTestsettingsid(String testsettingsid){
		this.testsettingsid = testsettingsid;
	}

	public String getTestsettingsid(){
		return testsettingsid;
	}

	public String getTestSeriesType() {
		return testSeriesType;
	}

	public void setTestSeriesType(String testSeriesType) {
		this.testSeriesType = testSeriesType;
	}

	@Override
 	public String toString(){
		return 
			"Filter{" + 
			"testsettingsid = '" + testsettingsid + '\'' + 
			"}";
		}
}
