package com.conicskill.app.data.model.wordDay;

import com.google.gson.annotations.SerializedName;

public class wordData {

	@SerializedName("wordOfTheDay")
	private WordOfTheDay wordOfTheDay;

	public void setWordOfTheDay(WordOfTheDay wordOfTheDay){
		this.wordOfTheDay = wordOfTheDay;
	}

	public WordOfTheDay getWordOfTheDay(){
		return wordOfTheDay;
	}

	@Override
 	public String toString(){
		return 
			"wordData{" +
			"wordOfTheDay = '" + wordOfTheDay + '\'' + 
			"}";
		}
}