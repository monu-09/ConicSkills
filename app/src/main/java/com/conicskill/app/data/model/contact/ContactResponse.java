package com.conicskill.app.data.model.contact;

import com.google.gson.annotations.SerializedName;

public class ContactResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private ContactResponseData data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	public int getCode(){
		return code;
	}

	public ContactResponseData getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public boolean isError(){
		return error;
	}

	public String getStatus(){
		return status;
	}
}