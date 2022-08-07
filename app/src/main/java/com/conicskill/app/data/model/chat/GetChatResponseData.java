package com.conicskill.app.data.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChatResponseData {

	@SerializedName("messageList")
	private List<MessageListItem> messageList;

	@SerializedName("messageId")
	private String messageId;

	@SerializedName("currentMessageId")
	private String currentMessageId;

	public void setMessageList(List<MessageListItem> messageList){
		this.messageList = messageList;
	}

	public List<MessageListItem> getMessageList(){
		return messageList;
	}

	public void setCurrentMessageId(String currentMessageId){
		this.currentMessageId = currentMessageId;
	}

	public String getCurrentMessageId(){
		return currentMessageId;
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
			"messageList = '" + messageList + '\'' + 
			",currentMessageId = '" + currentMessageId + '\'' + 
			"}";
		}
}