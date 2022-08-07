package com.conicskill.app.data.model.chat;

import com.google.gson.annotations.SerializedName;

public class ChatRequest{

	@SerializedName("data")
	private ChatRequestData data;

	public void setData(ChatRequestData data){
		this.data = data;
	}

	public ChatRequestData getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ChatRequest{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}