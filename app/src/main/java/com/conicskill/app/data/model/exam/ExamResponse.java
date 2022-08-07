package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

public class ExamResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private ExamData examData;

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

	public void setExamData(ExamData examData){
		this.examData = examData;
	}

	public ExamData getExamData(){
		return examData;
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
			"ExamResponse{" + 
			"code = '" + code + '\'' + 
			",data = '" + examData + '\'' +
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}