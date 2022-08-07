package com.conicskill.app.data.model.currentAffairs;

import com.google.gson.annotations.SerializedName;

public class CurrentAffairsRequest{

	@SerializedName("data")
	private CurrentAffairsRequestData currentAffairsRequestData;

	@SerializedName("token")
	private String token;

	public void setCurrentAffairsRequestData(CurrentAffairsRequestData currentAffairsRequestData){
		this.currentAffairsRequestData = currentAffairsRequestData;
	}

	public CurrentAffairsRequestData getCurrentAffairsRequestData(){
		return currentAffairsRequestData;
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
			"CurrentAffairsRequest{" + 
			"currentAffairsRequestData = '" + currentAffairsRequestData + '\'' +
			",token = '" + token + '\'' + 
			"}";
		}
}