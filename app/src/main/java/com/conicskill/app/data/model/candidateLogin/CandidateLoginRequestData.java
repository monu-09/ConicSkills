package com.conicskill.app.data.model.candidateLogin;

import com.conicskill.app.util.ytextractor.model.StreamingData;
import com.google.gson.annotations.SerializedName;

public class CandidateLoginRequestData {

	@SerializedName("password")
	private String password;

	@SerializedName("username")
	private String username;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("loginVia")
	private int loginVia;

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("notificationToken")
	private String notificationToken;

	@SerializedName("appVersion")
	private int appVersion;

	@SerializedName("additionalInfo")
	private String additionalInfo;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getLoginVia() {
		return loginVia;
	}

	public void setLoginVia(int loginVia) {
		this.loginVia = loginVia;
	}

	public String getNotificationToken() {
		return notificationToken;
	}

	public void setNotificationToken(String notificationToken) {
		this.notificationToken = notificationToken;
	}

	public int getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
 	public String toString(){
		return 
			"CandidateLoginRequestData{" +
			"password = '" + password + '\'' + 
			",username = '" + username + '\'' + 
			",mobile = '" + mobile + '\'' +
			",companyId = '" + companyId + '\'' +
			"}";
		}
}