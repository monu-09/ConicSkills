package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

public class EnItem{

	@SerializedName("instructions")
	private String instructions;

	@SerializedName("questionId")
	private String questionId;

	@SerializedName("visibility")
	private String visibility;

	@SerializedName("active")
	private String active;

	@SerializedName("published")
	private String published;

	@SerializedName("title")
	private String title;

	@SerializedName("tags")
	private Object tags;

	@SerializedName("questionMarks")
	private String questionMarks;

	@SerializedName("createdAt")
	private Object createdAt;

	@SerializedName("langcode")
	private String langcode;

	@SerializedName("questionCategory")
	private String questionCategory;

	@SerializedName("isDeleted")
	private String isDeleted;

	@SerializedName("translationId")
	private String translationId;

	@SerializedName("solution")
	private String solution;

	@SerializedName("createdBy")
	private Object createdBy;

	@SerializedName("options")
	private Options options;

	@SerializedName("questionStatement")
	private String questionStatement;

	@SerializedName("correctAnswer")
	private String correctAnswer;

	public void setInstructions(String instructions){
		this.instructions = instructions;
	}

	public String getInstructions(){
		return instructions;
	}

	public void setQuestionId(String questionId){
		this.questionId = questionId;
	}

	public String getQuestionId(){
		return questionId;
	}

	public void setVisibility(String visibility){
		this.visibility = visibility;
	}

	public String getVisibility(){
		return visibility;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setPublished(String published){
		this.published = published;
	}

	public String getPublished(){
		return published;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setTags(Object tags){
		this.tags = tags;
	}

	public Object getTags(){
		return tags;
	}

	public void setQuestionMarks(String questionMarks){
		this.questionMarks = questionMarks;
	}

	public String getQuestionMarks(){
		return questionMarks;
	}

	public void setCreatedAt(Object createdAt){
		this.createdAt = createdAt;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public void setLangcode(String langcode){
		this.langcode = langcode;
	}

	public String getLangcode(){
		return langcode;
	}

	public void setQuestionCategory(String questionCategory){
		this.questionCategory = questionCategory;
	}

	public String getQuestionCategory(){
		return questionCategory;
	}

	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}

	public String getIsDeleted(){
		return isDeleted;
	}

	public void setTranslationId(String translationId){
		this.translationId = translationId;
	}

	public String getTranslationId(){
		return translationId;
	}

	public void setSolution(String solution){
		this.solution = solution;
	}

	public String getSolution(){
		return solution;
	}

	public void setCreatedBy(Object createdBy){
		this.createdBy = createdBy;
	}

	public Object getCreatedBy(){
		return createdBy;
	}

	public void setOptions(Options options){
		this.options = options;
	}

	public Options getOptions(){
		return options;
	}

	public void setQuestionStatement(String questionStatement){
		this.questionStatement = questionStatement;
	}

	public String getQuestionStatement(){
		return questionStatement;
	}

	public void setCorrectAnswer(String correctAnswer){
		this.correctAnswer = correctAnswer;
	}

	public String getCorrectAnswer(){
		return correctAnswer;
	}

	@Override
 	public String toString(){
		return 
			"EnItem{" + 
			"instructions = '" + instructions + '\'' + 
			",questionId = '" + questionId + '\'' + 
			",visibility = '" + visibility + '\'' + 
			",active = '" + active + '\'' + 
			",published = '" + published + '\'' + 
			",title = '" + title + '\'' + 
			",tags = '" + tags + '\'' + 
			",questionMarks = '" + questionMarks + '\'' + 
			",createdAt = '" + createdAt + '\'' + 
			",langcode = '" + langcode + '\'' + 
			",questionCategory = '" + questionCategory + '\'' + 
			",isDeleted = '" + isDeleted + '\'' + 
			",translationId = '" + translationId + '\'' + 
			",solution = '" + solution + '\'' + 
			",createdBy = '" + createdBy + '\'' + 
			",options = '" + options + '\'' + 
			",questionStatement = '" + questionStatement + '\'' + 
			",correctAnswer = '" + correctAnswer + '\'' + 
			"}";
		}
}