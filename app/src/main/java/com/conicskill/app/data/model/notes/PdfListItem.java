package com.conicskill.app.data.model.notes;

import com.google.gson.annotations.SerializedName;

public class PdfListItem{

	@SerializedName("pdfUrl")
	private String pdfUrl;

	@SerializedName("videoId")
	private String videoId;

	public void setPdfUrl(String pdfUrl){
		this.pdfUrl = pdfUrl;
	}

	public String getPdfUrl(){
		return pdfUrl;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return videoId;
	}

	@Override
 	public String toString(){
		return 
			"PdfListItem{" + 
			"pdfUrl = '" + pdfUrl + '\'' + 
			",videoId = '" + videoId + '\'' + 
			"}";
		}
}