package com.conicskill.app.data.model.contact;

import com.google.gson.annotations.SerializedName;

public class ContactRequest{

	@SerializedName("data")
	private ContactRequestData data;

	public ContactRequestData getData(){
		return data;
	}
}