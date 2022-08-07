package com.conicskill.app.data.model.contact;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AdditionalInfo{

	@SerializedName("website")
	private String website;

	@SerializedName("twitter")
	private String twitter;

	@SerializedName("address")
	private String address;

	@SerializedName("whatsApp")
	private long whatsApp;

	@SerializedName("companyLogo")
	private String companyLogo;

	@SerializedName("facebook")
	private String facebook;

	@SerializedName("youTube")
	private String youTube;

	@SerializedName("telegram")
	private String telegram;

	@SerializedName("mobileNo")
	private long mobileNo;

	@SerializedName("email")
	private String email;

	@SerializedName("bannerImages")
	private List<Object> bannerImages;

	public String getWebsite(){
		return website;
	}

	public String getTwitter(){
		return twitter;
	}

	public String getAddress(){
		return address;
	}

	public long getWhatsApp(){
		return whatsApp;
	}

	public String getCompanyLogo(){
		return companyLogo;
	}

	public String getFacebook(){
		return facebook;
	}

	public String getYouTube(){
		return youTube;
	}

	public String getTelegram(){
		return telegram;
	}

	public long getMobileNo(){
		return mobileNo;
	}

	public String getEmail(){
		return email;
	}

	public List<Object> getBannerImages(){
		return bannerImages;
	}
}