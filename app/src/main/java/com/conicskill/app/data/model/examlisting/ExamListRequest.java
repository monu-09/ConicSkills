package com.conicskill.app.data.model.examlisting;

import com.google.gson.annotations.SerializedName;

public class ExamListRequest{

	@SerializedName("data")
	private Data data;

	@SerializedName("token")
	private String token;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
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
			"ExamListRequest{" + 
			"data = '" + data + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}