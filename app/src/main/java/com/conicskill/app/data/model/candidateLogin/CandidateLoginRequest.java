package com.conicskill.app.data.model.candidateLogin;

import com.google.gson.annotations.SerializedName;

public class CandidateLoginRequest{

	@SerializedName("data")
	private CandidateLoginRequestData candidateLoginRequestData;

	public void setCandidateLoginRequestData(CandidateLoginRequestData candidateLoginRequestData){
		this.candidateLoginRequestData = candidateLoginRequestData;
	}

	public CandidateLoginRequestData getCandidateLoginRequestData(){
		return candidateLoginRequestData;
	}

	@Override
 	public String toString(){
		return 
			"CandidateLoginRequest{" + 
			"candidateLoginRequestData = '" + candidateLoginRequestData + '\'' +
			"}";
		}
}
