package com.conicskill.app.data.model.chat;

import com.google.gson.annotations.SerializedName;

public class ChatRequestData {

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("chatId")
	private String chatId;

	@SerializedName("messageType")
	private int messageType;

	@SerializedName("author")
	private String author;

	@SerializedName("text")
	private String text;

	@SerializedName("messageId")
	private String messageId;

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setChatId(String chatId){
		this.chatId = chatId;
	}

	public String getChatId(){
		return chatId;
	}

	public void setMessageType(int messageType){
		this.messageType = messageType;
	}

	public int getMessageType(){
		return messageType;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"companyId = '" + companyId + '\'' + 
			",chatId = '" + chatId + '\'' + 
			",messageType = '" + messageType + '\'' + 
			",author = '" + author + '\'' + 
			",text = '" + text + '\'' + 
			"}";
		}
}