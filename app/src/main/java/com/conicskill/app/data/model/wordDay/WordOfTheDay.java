package com.conicskill.app.data.model.wordDay;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WordOfTheDay implements Serializable {

	@SerializedName("wordType")
	private String wordType;

	@SerializedName("meaning")
	private String meaning;

	@SerializedName("synonyms")
	private String synonyms;

	@SerializedName("wordId")
	private int wordId;

	@SerializedName("translation")
	private String translation;

	@SerializedName("antonyms")
	private String antonyms;

	@SerializedName("active")
	private int active;

	@SerializedName("word")
	private String word;

	public void setWordType(String wordType){
		this.wordType = wordType;
	}

	public String getWordType(){
		return wordType;
	}

	public void setMeaning(String meaning){
		this.meaning = meaning;
	}

	public String getMeaning(){
		return meaning;
	}

	public void setSynonyms(String synonyms){
		this.synonyms = synonyms;
	}

	public String getSynonyms(){
		return synonyms;
	}

	public void setWordId(int wordId){
		this.wordId = wordId;
	}

	public int getWordId(){
		return wordId;
	}

	public void setTranslation(String translation){
		this.translation = translation;
	}

	public String getTranslation(){
		return translation;
	}

	public void setAntonyms(String antonyms){
		this.antonyms = antonyms;
	}

	public String getAntonyms(){
		return antonyms;
	}

	public void setActive(int active){
		this.active = active;
	}

	public int getActive(){
		return active;
	}

	public void setWord(String word){
		this.word = word;
	}

	public String getWord(){
		return word;
	}

	@Override
 	public String toString(){
		return 
			"WordOfTheDay{" + 
			"wordType = '" + wordType + '\'' + 
			",meaning = '" + meaning + '\'' + 
			",synonyms = '" + synonyms + '\'' + 
			",wordId = '" + wordId + '\'' + 
			",translation = '" + translation + '\'' + 
			",antonyms = '" + antonyms + '\'' + 
			",active = '" + active + '\'' + 
			",word = '" + word + '\'' + 
			"}";
		}
}