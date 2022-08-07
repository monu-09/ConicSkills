package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

public class ModuleRequest{

	@SerializedName("data")
	private ModuleRequestData moduleRequestData;

	@SerializedName("token")
	private String token;

	public void setModuleRequestData(ModuleRequestData moduleRequestData){
		this.moduleRequestData = moduleRequestData;
	}

	public ModuleRequestData getModuleRequestData(){
		return moduleRequestData;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
 	public String toString(){
		return 
			"ModuleRequest{" + 
			"moduleRequestData = '" + moduleRequestData + '\'' +
			"}";
		}
}