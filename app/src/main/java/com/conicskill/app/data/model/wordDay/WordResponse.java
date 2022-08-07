package com.conicskill.app.data.model.wordDay;

import com.google.gson.annotations.SerializedName;

public class WordResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private wordData wordData;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setWordData(wordData wordData){
		this.wordData = wordData;
	}

	public wordData getWordData(){
		return wordData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"WordResponse{" + 
			"code = '" + code + '\'' + 
			",wordData = '" + wordData + '\'' +
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}