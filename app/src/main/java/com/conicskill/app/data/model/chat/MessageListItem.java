package com.conicskill.app.data.model.chat;

import com.google.gson.annotations.SerializedName;

public class MessageListItem{

	@SerializedName("messageType")
	private int messageType;

	@SerializedName("data")
	private String data;

	@SerializedName("chatId")
	private String chatId;

	@SerializedName("author")
	private String author;

	@SerializedName("authorName")
	private String authorName;

	@SerializedName("replyToMessageId")
	private int replyToMessageId;

	@SerializedName("participantType")
	private int participantType;

	@SerializedName("text")
	private String text;

	@SerializedName("messageAt")
	private String messageAt;

	@SerializedName("userId")
	private String userId;

	public void setMessageType(int messageType){
		this.messageType = messageType;
	}

	public int getMessageType(){
		return messageType;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setChatId(String chatId){
		this.chatId = chatId;
	}

	public String getChatId(){
		return chatId;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setReplyToMessageId(int replyToMessageId){
		this.replyToMessageId = replyToMessageId;
	}

	public int getReplyToMessageId(){
		return replyToMessageId;
	}

	public void setParticipantType(int participantType){
		this.participantType = participantType;
	}

	public int getParticipantType(){
		return participantType;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getMessageAt() {
		return messageAt;
	}

	public void setMessageAt(String messageAt) {
		this.messageAt = messageAt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
 	public String toString(){
		return 
			"MessageListItem{" + 
			"messageType = '" + messageType + '\'' + 
			",data = '" + data + '\'' + 
			",chatId = '" + chatId + '\'' + 
			",author = '" + author + '\'' + 
			",replyToMessageId = '" + replyToMessageId + '\'' + 
			",participantType = '" + participantType + '\'' + 
			",text = '" + text + '\'' + 
			",userId = '" + userId + '\'' +
			"}";
		}
}