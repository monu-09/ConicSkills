package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;

public class YoutubeChannelRequest{

	@SerializedName("data")
	private YoutubeChannelData data;

	@SerializedName("token")
	private String token;

	public void setData(YoutubeChannelData data){
		this.data = data;
	}

	public YoutubeChannelData getData(){
		return data;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}