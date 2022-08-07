package com.conicskill.app.data.model.contact;

import com.google.gson.annotations.SerializedName;

public class ContactRequestData {

	@SerializedName("companyId")
	private String companyId;

	public String getCompanyId(){
		return companyId;
	}
}