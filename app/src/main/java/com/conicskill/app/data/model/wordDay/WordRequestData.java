package com.conicskill.app.data.model.wordDay;

import com.google.gson.annotations.SerializedName;

public class WordRequestData {

	@SerializedName("filters")
	private Filters filters;

	public void setFilters(Filters filters){
		this.filters = filters;
	}

	public Filters getFilters(){
		return filters;
	}

	@Override
 	public String toString(){
		return 
			"WordRequestData{" +
			"filters = '" + filters + '\'' + 
			"}";
		}
}