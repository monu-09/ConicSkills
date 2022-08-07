package com.conicskill.app.data.model.carousel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class RequestData implements Serializable {

	@SerializedName("companyId")
	private String companyId;

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}
}