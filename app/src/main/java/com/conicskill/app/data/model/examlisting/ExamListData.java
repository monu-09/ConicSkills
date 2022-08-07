package com.conicskill.app.data.model.examlisting;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ExamListData{

	@SerializedName("testList")
	private List<TestListItem> testList;

	public void setTestList(List<TestListItem> testList){
		this.testList = testList;
	}

	public List<TestListItem> getTestList(){
		return testList;
	}

	@Override
 	public String toString(){
		return 
			"CandidateLoginData{" +
			"testList = '" + testList + '\'' + 
			"}";
		}
}