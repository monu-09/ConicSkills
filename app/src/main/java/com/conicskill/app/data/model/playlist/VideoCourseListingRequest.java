package com.conicskill.app.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class VideoCourseListingRequest{

	@SerializedName("data")
	private VideoListingData videoListingData;

	@SerializedName("token")
	private String authToken;

	public void setVideoListingData(VideoListingData videoListingData){
		this.videoListingData = videoListingData;
	}

	public VideoListingData getVideoListingData(){
		return videoListingData;
	}

	public void setAuthToken(String authToken){
		this.authToken = authToken;
	}

	public String getAuthToken(){
		return authToken;
	}

	@Override
 	public String toString(){
		return 
			"VideoCourseListingRequest{" + 
			"data = '" + videoListingData + '\'' +
			",authToken = '" + authToken + '\'' + 
			"}";
		}
}