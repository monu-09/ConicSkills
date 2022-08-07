package com.conicskill.app.data.model.saveTest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CandidateResponseItem{

	@SerializedName("moduleName")
	private String moduleName;

	@SerializedName("moduleId")
	private String moduleId;

	@SerializedName("questionList")
	private List<QuestionListItem> questionList;

	public String getModuleName(){
		return moduleName;
	}

	public String getModuleId(){
		return moduleId;
	}

	public List<QuestionListItem> getQuestionList(){
		return questionList;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public void setQuestionList(List<QuestionListItem> questionList) {
		this.questionList = questionList;
	}
}