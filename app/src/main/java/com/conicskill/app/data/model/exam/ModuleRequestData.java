package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModuleRequestData {

	@SerializedName("moduleId")
	private List<String> moduleId;

	public void setModuleId(List<String> moduleId){
		this.moduleId = moduleId;
	}

	@SerializedName("token")
	private String token;

	public List<String> getModuleId(){
		return moduleId;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"ModuleRequestData{" +
			"moduleId = '" + moduleId + '\'' + 
			"}";
		}
}