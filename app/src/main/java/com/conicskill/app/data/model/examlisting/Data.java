package com.conicskill.app.data.model.examlisting;

import com.google.gson.annotations.SerializedName;

public class Data{
	@SerializedName("filters")
	private Filter filter;

	public void setFilter(Filter filter){
		this.filter = filter;
	}

	public Filter getFilter(){
		return filter;
	}

	@Override
 	public String toString(){
		return 
			"CurrentAffairsData{" +
			"filter = '" + filter + '\'' + 
			"}";
		}
}
