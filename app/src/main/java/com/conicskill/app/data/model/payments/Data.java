package com.conicskill.app.data.model.payments;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("amount")
	private String amount;

	@SerializedName("isRefunded")
	private String isRefunded;

	@SerializedName("orderId")
	private String orderId;

	@SerializedName("transactionObj")
	private String transactionObj;

	@SerializedName("active")
	private String active;

	@SerializedName("tax")
	private String tax;

	@SerializedName("customData")
	private String customData;

	@SerializedName("transactionDate")
	private String transactionDate;

	@SerializedName("transactionId")
	private String transactionId;

	@SerializedName("productName")
	private String productName;

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("transactionOrderId")
	private String transactionOrderId;

	@SerializedName("gatewayFee")
	private String gatewayFee;

	@SerializedName("paymentId")
	private String paymentId;

	@SerializedName("candidateId")
	private String candidateId;

	@SerializedName("status")
	private String status;

	@SerializedName("paymentGateway")
	private String paymentGateway;

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setIsRefunded(String isRefunded){
		this.isRefunded = isRefunded;
	}

	public String getIsRefunded(){
		return isRefunded;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setTransactionObj(String transactionObj){
		this.transactionObj = transactionObj;
	}

	public String getTransactionObj(){
		return transactionObj;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setTax(String tax){
		this.tax = tax;
	}

	public String getTax(){
		return tax;
	}

	public void setCustomData(String customData){
		this.customData = customData;
	}

	public String getCustomData(){
		return customData;
	}

	public void setTransactionDate(String transactionDate){
		this.transactionDate = transactionDate;
	}

	public String getTransactionDate(){
		return transactionDate;
	}

	public void setTransactionId(String transactionId){
		this.transactionId = transactionId;
	}

	public String getTransactionId(){
		return transactionId;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setTransactionOrderId(String transactionOrderId){
		this.transactionOrderId = transactionOrderId;
	}

	public String getTransactionOrderId(){
		return transactionOrderId;
	}

	public void setGatewayFee(String gatewayFee){
		this.gatewayFee = gatewayFee;
	}

	public String getGatewayFee(){
		return gatewayFee;
	}

	public void setPaymentId(String paymentId){
		this.paymentId = paymentId;
	}

	public String getPaymentId(){
		return paymentId;
	}

	public void setCandidateId(String candidateId){
		this.candidateId = candidateId;
	}

	public String getCandidateId(){
		return candidateId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setPaymentGateway(String paymentGateway){
		this.paymentGateway = paymentGateway;
	}

	public String getPaymentGateway(){
		return paymentGateway;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"amount = '" + amount + '\'' + 
			",isRefunded = '" + isRefunded + '\'' + 
			",orderId = '" + orderId + '\'' + 
			",transactionObj = '" + transactionObj + '\'' + 
			",active = '" + active + '\'' + 
			",tax = '" + tax + '\'' + 
			",customData = '" + customData + '\'' + 
			",transactionDate = '" + transactionDate + '\'' + 
			",transactionId = '" + transactionId + '\'' + 
			",productName = '" + productName + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",transactionOrderId = '" + transactionOrderId + '\'' + 
			",gatewayFee = '" + gatewayFee + '\'' + 
			",paymentId = '" + paymentId + '\'' + 
			",candidateId = '" + candidateId + '\'' + 
			",status = '" + status + '\'' + 
			",paymentGateway = '" + paymentGateway + '\'' + 
			"}";
		}
}