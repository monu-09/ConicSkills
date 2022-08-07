package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;

public class Id{

	@SerializedName("kind")
	private String kind;

	@SerializedName("videoId")
	private String videoId;

	@SerializedName("channelId")
	private String channelId;

	@SerializedName("playlistId")
	private String playlistId;

	public String getKind(){
		return kind;
	}

	public String getVideoId(){
		return videoId;
	}

	public String getChannelId(){
		return channelId;
	}

	public String getPlaylistId(){
		return playlistId;
	}
}