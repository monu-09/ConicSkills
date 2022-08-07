package com.conicskill.app.data.model.carousel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HiItem implements Serializable {

	@SerializedName("instructions")
	private String instructions;

	@SerializedName("questionId")
	private int questionId;

	@SerializedName("visibility")
	private int visibility;

	@SerializedName("active")
	private int active;

	@SerializedName("published")
	private int published;

	@SerializedName("title")
	private Object title;

	@SerializedName("tags")
	private Object tags;

	@SerializedName("questionMarks")
	private String questionMarks;

	@SerializedName("createdAt")
	private Object createdAt;

	@SerializedName("langcode")
	private String langcode;

	@SerializedName("questionCategory")
	private int questionCategory;

	@SerializedName("isDeleted")
	private int isDeleted;

	@SerializedName("translationId")
	private int translationId;

	@SerializedName("solution")
	private String solution;

	@SerializedName("createdBy")
	private Object createdBy;

	@SerializedName("options")
	private Options options;

	@SerializedName("questionStatement")
	private String questionStatement;

	@SerializedName("correctAnswer")
	private List<String> correctAnswer;

	public void setInstructions(String instructions){
		this.instructions = instructions;
	}

	public String getInstructions(){
		return instructions;
	}

	public void setQuestionId(int questionId){
		this.questionId = questionId;
	}

	public int getQuestionId(){
		return questionId;
	}

	public void setVisibility(int visibility){
		this.visibility = visibility;
	}

	public int getVisibility(){
		return visibility;
	}

	public void setActive(int active){
		this.active = active;
	}

	public int getActive(){
		return active;
	}

	public void setPublished(int published){
		this.published = published;
	}

	public int getPublished(){
		return published;
	}

	public void setTitle(Object title){
		this.title = title;
	}

	public Object getTitle(){
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

	public void setQuestionCategory(int questionCategory){
		this.questionCategory = questionCategory;
	}

	public int getQuestionCategory(){
		return questionCategory;
	}

	public void setIsDeleted(int isDeleted){
		this.isDeleted = isDeleted;
	}

	public int getIsDeleted(){
		return isDeleted;
	}

	public void setTranslationId(int translationId){
		this.translationId = translationId;
	}

	public int getTranslationId(){
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

	public void setCorrectAnswer(List<String> correctAnswer){
		this.correctAnswer = correctAnswer;
	}

	public List<String> getCorrectAnswer(){
		return correctAnswer;
	}
}