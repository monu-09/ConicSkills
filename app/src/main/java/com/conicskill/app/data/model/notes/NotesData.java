package com.conicskill.app.data.model.notes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotesData {

	@SerializedName("pdfList")
	private List<PdfListItem> pdfList;

	public void setPdfList(List<PdfListItem> pdfList){
		this.pdfList = pdfList;
	}

	public List<PdfListItem> getPdfList(){
		return pdfList;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"pdfList = '" + pdfList + '\'' + 
			"}";
		}
}