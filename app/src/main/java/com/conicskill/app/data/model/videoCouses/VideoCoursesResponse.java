package com.conicskill.app.data.model.videoCouses;

import com.google.gson.annotations.SerializedName;

public class VideoCoursesResponse {

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private CourseData data;

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

	public void setData(CourseData data){
		this.data = data;
	}

	public CourseData getData(){
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

	@Override
 	public String toString(){
		return 
			"VideoCourseResponses{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}