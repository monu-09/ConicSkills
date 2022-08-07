package com.conicskill.app.data.model.news;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

public class CategoryItem implements Diffable {

	@SerializedName("imgUrl")
	private String imgUrl;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("url")
	private String url;

	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"imgUrl = '" + imgUrl + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}

	@Override
	public boolean areContentTheSame(@NotNull Object o) {
		return false;
	}

	@Override
	public long getUniqueIdentifier() {
		return System.identityHashCode(this);
	}
}