package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;

public class YoutubeChannelData {

	@SerializedName("channelId")
	private String channelId;

	public void setChannelId(String channelId){
		this.channelId = channelId;
	}

	public String getChannelId(){
		return channelId;
	}
}