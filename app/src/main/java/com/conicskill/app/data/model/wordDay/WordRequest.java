package com.conicskill.app.data.model.wordDay;

import com.google.gson.annotations.SerializedName;

public class WordRequest{

	@SerializedName("data")
	private WordRequestData wordRequestData;

	@SerializedName("token")
	private String token;

	public void setWordRequestData(WordRequestData wordRequestData){
		this.wordRequestData = wordRequestData;
	}

	public WordRequestData getWordRequestData(){
		return wordRequestData;
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
			"WordRequest{" + 
			"wordRequestData = '" + wordRequestData + '\'' +
			",token = '" + token + '\'' + 
			"}";
		}
}