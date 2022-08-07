package com.conicskill.app.data.model.youtubeVideo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class YoutubeChannelResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	public int getCode(){
		return code;
	}

	public List<DataItem> getData(){
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