package com.conicskill.app.data.model.payments;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PaymentData {

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("paymentId")
	private String paymentId;

	@SerializedName("itemList")
	private List<ItemListItem> itemList;

	@SerializedName("candidateId")
	private String candidateId;

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setPaymentId(String paymentId){
		this.paymentId = paymentId;
	}

	public String getPaymentId(){
		return paymentId;
	}

	public void setItemList(List<ItemListItem> itemList){
		this.itemList = itemList;
	}

	public List<ItemListItem> getItemList(){
		return itemList;
	}

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
			"companyId = '" + companyId + '\'' + 
			",paymentId = '" + paymentId + '\'' + 
			",itemList = '" + itemList + '\'' + 
			",candidateId = '" + candidateId + '\'' + 
			"}";
		}
}