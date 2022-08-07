package com.conicskill.app.data.model.playlist;

import com.google.gson.annotations.SerializedName;

public class PlaylistResponse {

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private PlayListData playListData;

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

	public void setPlayListData(PlayListData playListData){
		this.playListData = playListData;
	}

	public PlayListData getPlayListData(){
		return playListData;
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

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"code = '" + code + '\'' + 
			",data = '" + playListData + '\'' +
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}
