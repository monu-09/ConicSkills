package com.conicskill.app.data.model.payments;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {

	@SerializedName("data")
	private PaymentData data;

	public void setData(PaymentData data){
		this.data = data;
	}

	public PaymentData getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"PaymentRequestData{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}