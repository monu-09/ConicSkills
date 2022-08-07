package com.conicskill.app.data.model.carousel;

import java.io.Serializable;
import java.util.List;

import com.conicskill.app.data.model.exam.Questions;
import com.google.gson.annotations.SerializedName;

public class JsonMember1 implements Serializable {

	@SerializedName("hi")
	private List<Questions> hi;

	@SerializedName("en")
	private List<Questions> en;

	public void setHi(List<Questions> hi){
		this.hi = hi;
	}

	public List<Questions> getHi(){
		return hi;
	}

	public void setEn(List<Questions> en){
		this.en = en;
	}

	public List<Questions> getEn(){
		return en;
	}
}