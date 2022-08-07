package com.conicskill.app.data.model.carousel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionData implements Serializable {

	@SerializedName("-1")
	private JsonMember1 jsonMember1;

	public void setJsonMember1(JsonMember1 jsonMember1){
		this.jsonMember1 = jsonMember1;
	}

	public JsonMember1 getJsonMember1(){
		return jsonMember1;
	}
}