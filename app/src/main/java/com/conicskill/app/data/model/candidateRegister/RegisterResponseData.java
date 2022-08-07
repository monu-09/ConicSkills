package com.conicskill.app.data.model.candidateRegister;

import com.google.gson.annotations.SerializedName;

public class RegisterResponseData {

	@SerializedName("candidateId")
	private String candidateId;

	public void setCandidateId(String candidateId){
		this.candidateId = candidateId;
	}

	public String getCandidateId(){
		return candidateId;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"candidateId = '" + candidateId + '\'' + 
			"}";
		}
}