package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonMember4{

	@SerializedName("hi")
	private List<Questions> hi;

	@SerializedName("en")
	private List<EnItem> en;

	public void setHi(List<Questions> hi){
		this.hi = hi;
	}

	public List<Questions> getHi(){
		return hi;
	}

	public void setEn(List<EnItem> en){
		this.en = en;
	}

	public List<EnItem> getEn(){
		return en;
	}

	@Override
 	public String toString(){
		return 
			"JsonMember4{" + 
			"hi = '" + hi + '\'' + 
			",en = '" + en + '\'' + 
			"}";
		}
}