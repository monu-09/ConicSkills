package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;

public class Snippet{

	@SerializedName("publishTime")
	private String publishTime;

	@SerializedName("publishedAt")
	private String publishedAt;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("thumbnails")
	private Thumbnails thumbnails;

	@SerializedName("channelId")
	private String channelId;

	@SerializedName("channelTitle")
	private String channelTitle;

	@SerializedName("liveBroadcastContent")
	private String liveBroadcastContent;

	public String getPublishTime(){
		return publishTime;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public String getDescription(){
		return description;
	}

	public String getTitle(){
		return title;
	}

	public Thumbnails getThumbnails(){
		return thumbnails;
	}

	public String getChannelId(){
		return channelId;
	}

	public String getChannelTitle(){
		return channelTitle;
	}

	public String getLiveBroadcastContent(){
		return liveBroadcastContent;
	}
}