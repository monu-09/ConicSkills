package com.conicskill.app.data.model.wordDay;

import com.google.gson.annotations.SerializedName;

public class Filters{

	@SerializedName("notesType")
	private String notesType;

	public void setNotesType(String notesType){
		this.notesType = notesType;
	}

	public String getNotesType(){
		return notesType;
	}

	@Override
 	public String toString(){
		return 
			"Filters{" + 
			"notesType = '" + notesType + '\'' + 
			"}";
		}
}