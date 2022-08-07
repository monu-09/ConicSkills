package com.conicskill.app.data.model.candidateLogin;

import com.google.gson.annotations.SerializedName;

public class CandidateLoginResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private CandidateLoginData candidateLoginData;

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

	public void setCandidateLoginData(CandidateLoginData candidateLoginData){
		this.candidateLoginData = candidateLoginData;
	}

	public CandidateLoginData getCandidateLoginData(){
		return candidateLoginData;
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
			"CandidateLoginResponse{" + 
			"code = '" + code + '\'' + 
			",candidateLoginData = '" + candidateLoginData + '\'' +
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}