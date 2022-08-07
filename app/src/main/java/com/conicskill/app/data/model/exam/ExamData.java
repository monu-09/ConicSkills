package com.conicskill.app.data.model.exam;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExamData {

	@SerializedName("questionData")
	private JsonObject questionData;

	@SerializedName("moduleData")
	private List<Modules> moduleData;

	public void setQuestionData(JsonObject questionData){
		this.questionData = questionData;
	}

	public JsonObject getQuestionData(){
		return questionData;
	}

	public void setModuleData(List<Modules> moduleData){
		this.moduleData = moduleData;
	}

	public List<Modules> getModuleData(){
		return moduleData;
	}

	@Override
 	public String toString(){
		return 
			"ExamData{" +
			"questionData = '" + questionData + '\'' + 
			",moduleData = '" + moduleData + '\'' + 
			"}";
		}
}