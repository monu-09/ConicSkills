package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;

public class Thumbnails{

	@SerializedName("default")
	private JsonMemberDefault jsonMemberDefault;

	@SerializedName("high")
	private High high;

	@SerializedName("medium")
	private Medium medium;

	public JsonMemberDefault getJsonMemberDefault(){
		return jsonMemberDefault;
	}

	public High getHigh(){
		return high;
	}

	public Medium getMedium(){
		return medium;
	}
}