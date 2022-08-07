package com.conicskill.app.data.model.carousel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HomeCarouselResponse implements Serializable {

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private List<HomeDataMain> data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(List<HomeDataMain> data){
		this.data = data;
	}

	public List<HomeDataMain> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}