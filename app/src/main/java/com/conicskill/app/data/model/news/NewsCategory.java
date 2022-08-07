package com.conicskill.app.data.model.news;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NewsCategory{

	@SerializedName("data")
	private List<CategoryItem> data;

	public void setData(List<CategoryItem> data){
		this.data = data;
	}

	public List<CategoryItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"NewsCategory{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}