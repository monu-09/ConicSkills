package com.conicskill.app.data.model.examlisting;

import com.google.gson.annotations.SerializedName;

public class TestSeriesItem{

	@SerializedName("testSeriesId")
	private String testSeriesId;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("active")
	private String active;

	@SerializedName("testSeriesType")
	private String testSeriesType;

	@SerializedName("title")
	private String title;

	@SerializedName("price")
	private String price;

	@SerializedName("purchased")
	private String purchased;

	public void setTestSeriesId(String testSeriesId){
		this.testSeriesId = testSeriesId;
	}

	public String getTestSeriesId(){
		return testSeriesId;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setTestSeriesType(String testSeriesType){
		this.testSeriesType = testSeriesType;
	}

	public String getTestSeriesType(){
		return testSeriesType;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPurchased() {
		return purchased;
	}

	public void setPurchased(String purchased) {
		this.purchased = purchased;
	}

	@Override
 	public String toString(){
		return 
			"TestSeriesItem{" + 
			"testSeriesId = '" + testSeriesId + '\'' + 
			",imageUrl = '" + imageUrl + '\'' + 
			",active = '" + active + '\'' + 
			",testSeriesType = '" + testSeriesType + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}