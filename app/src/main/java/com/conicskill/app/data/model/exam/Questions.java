package com.conicskill.app.data.model.exam;

import com.conicskill.app.database.Converters;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

public class Questions implements Diffable {

	@Nullable
	@SerializedName("instructions")
	private String instructions;

	@PrimaryKey
	@ColumnInfo(name = "questionId")
	@SerializedName("questionId")
	private String questionId;

	@ColumnInfo(name = "moduleId")
	private String moduleId;

	@ColumnInfo(name = "visibility")
	@SerializedName("visibility")
	private String visibility;

	@ColumnInfo(name = "active")
	@SerializedName("active")
	private String active;

	@ColumnInfo(name = "published")
	@SerializedName("published")
	private String published;

/*	@Nullable
	@ColumnInfo(name = "title")
	@SerializedName("title")
	private String title;*/

/*	@Nullable
	@ColumnInfo(name = "tags")
	@SerializedName("tags")
	private Object tags;*/

	@Nullable
	@ColumnInfo(name = "questionMarks")
	@SerializedName("questionMarks")
	private String questionMarks;

/*	@Nullable
	@ColumnInfo(name = "createdAt")
	@SerializedName("createdAt")
	private Object createdAt;*/

	@Nullable
	@ColumnInfo(name = "langcode")
	@SerializedName("langcode")
	private String langcode;

	@Nullable
	@ColumnInfo(name = "questionCategory")
	@SerializedName("questionCategory")
	private String questionCategory;

	@Nullable
	@ColumnInfo(name = "isDeleted")
	@SerializedName("isDeleted")
	private String isDeleted;

	@Nullable
	@ColumnInfo(name = "translationId")
	@SerializedName("translationId")
	private String translationId;

	@Nullable
	@ColumnInfo(name = "solution")
	@SerializedName("solution")
	private String solution;

	@Nullable
	@ColumnInfo(name = "createdBy")
	@SerializedName("createdBy")
	private Object createdBy;

	@Nullable
	@ColumnInfo(name = "options")
	@SerializedName("options")
	private JsonObject options;

	@Nullable
	@ColumnInfo(name = "questionStatement")
	@SerializedName("questionStatement")
	private String questionStatement;

	@Nullable
	@ColumnInfo(name = "correctAnswer")
	@SerializedName("correctAnswer")
	private String correctAnswer;

	@Nullable
	@ColumnInfo(name = "isLocked")


	private boolean isMarkedForReview = false;

	private List<String> selectedOptions = new ArrayList<>();

	private List<Options> optionsList = new ArrayList<>();

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public List<Options> getOptionsList() {
		return optionsList;
	}

	public void setOptionsList(List<Options> optionsList) {
		this.optionsList = optionsList;
	}

	public boolean isMarkedForReview() {
		return isMarkedForReview;
	}

	public void setMarkedForReview(boolean markedForReview) {
		isMarkedForReview = markedForReview;
	}

	public List<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<String> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

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

/*	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}*/

/*	public void setTags(Object tags){
		this.tags = tags;
	}

	public Object getTags(){
		return tags;
	}*/

	public void setQuestionMarks(String questionMarks){
		this.questionMarks = questionMarks;
	}

	public String getQuestionMarks(){
		return questionMarks;
	}

/*	public void setCreatedAt(Object createdAt){
		this.createdAt = createdAt;
	}

	public Object getCreatedAt(){
		return createdAt;
	}*/

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

/*	public void setCreatedBy(Object createdBy){
		this.createdBy = createdBy;
	}

	public Object getCreatedBy(){
		return createdBy;
	}*/

	public void setOptions(JsonObject options){
		this.options = options;
	}

	public JsonObject getOptions(){
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
			"Questions{" +
			"instructions = '" + instructions + '\'' + 
			",questionId = '" + questionId + '\'' + 
			",visibility = '" + visibility + '\'' + 
			",active = '" + active + '\'' + 
			",published = '" + published + '\'' + 
//			",title = '" + title + '\'' +
//			",tags = '" + tags + '\'' +
			",questionMarks = '" + questionMarks + '\'' + 
//			",createdAt = '" + createdAt + '\'' +
			",langcode = '" + langcode + '\'' + 
			",questionCategory = '" + questionCategory + '\'' + 
			",isDeleted = '" + isDeleted + '\'' + 
			",translationId = '" + translationId + '\'' + 
			",solution = '" + solution + '\'' + 
//			",createdBy = '" + createdBy + '\'' +
			",options = '" + options + '\'' + 
			",questionStatement = '" + questionStatement + '\'' + 
//			",correctAnswer = '" + correctAnswer + '\'' +
			"}";
		}

	@Override
	public boolean areContentTheSame(@NotNull Object o) {
		return false;
	}

	@Override
	public long getUniqueIdentifier() {
		return Long.valueOf(questionId);
	}
}