package com.conicskill.app.data.model.currentAffairs;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CurrentAffairsData {

	@SerializedName("currentAffairs")
	private List<CurrentAffairsItem> currentAffairs;

	@SerializedName("count")
	private String count;

	public void setCurrentAffairs(List<CurrentAffairsItem> currentAffairs){
		this.currentAffairs = currentAffairs;
	}

	public List<CurrentAffairsItem> getCurrentAffairs(){
		return currentAffairs;
	}

	public void setCount(String count){
		this.count = count;
	}

	public String getCount(){
		return count;
	}

	@Override
 	public String toString(){
		return 
			"CurrentAffairsData{" +
			"currentAffairs = '" + currentAffairs + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}