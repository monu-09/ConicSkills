package com.conicskill.app.data.model.contact;

import com.google.gson.annotations.SerializedName;

public class ContactResponseData {

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("companyName")
	private String companyName;

	@SerializedName("additionalInfo")
	private AdditionalInfo additionalInfo;

	@SerializedName("active")
	private String active;

	@SerializedName("minAppVersion")
	private String minAppVersion;

	@SerializedName("createDateTime")
	private String createDateTime;

	public String getCompanyId(){
		return companyId;
	}

	public String getCompanyName(){
		return companyName;
	}

	public AdditionalInfo getAdditionalInfo(){
		return additionalInfo;
	}

	public String getActive(){
		return active;
	}

	public String getMinAppVersion(){
		return minAppVersion;
	}

	public String getCreateDateTime(){
		return createDateTime;
	}
}