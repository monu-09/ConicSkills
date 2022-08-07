package com.conicskill.app.data.model.candidateRegister;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest{

	@SerializedName("data")
	private RegisterRequestData data;

	public void setData(RegisterRequestData data){
		this.data = data;
	}

	public RegisterRequestData getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"RegisterRequest{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}
