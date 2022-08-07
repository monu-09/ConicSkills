package com.conicskill.app.data.model.currentAffairs;

import com.google.gson.annotations.SerializedName;

public class CurrentAffairsRequestData {

	@SerializedName("courseId")
	private String courseId;

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("langCode")
	private String langCode;

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	@Override
	public String toString(){
		return
				"CurrentAffairsRequestData{" +
						"}";
	}
}