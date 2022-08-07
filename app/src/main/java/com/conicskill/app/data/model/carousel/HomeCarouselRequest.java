package com.conicskill.app.data.model.carousel;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class HomeCarouselRequest implements Serializable {

	@SerializedName("data")
	private RequestData data;

	@SerializedName("token")
	private String token;

	public void setData(RequestData data){
		this.data = data;
	}

	public RequestData getData(){
		return data;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}