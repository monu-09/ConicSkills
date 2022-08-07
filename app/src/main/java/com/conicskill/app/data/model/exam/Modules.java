package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "modules")
public class Modules {

	@SerializedName("moduleType")
	@ColumnInfo(name = "moduleType")
	private String moduleType;

	@SerializedName("createdBy")
	@ColumnInfo(name = "createdBy")
	private String createdBy;

	@SerializedName("questionSet")
	@ColumnInfo(name = "questionSet")
	private String questionSet;

	@SerializedName("moduleName")
	@ColumnInfo(name = "moduleName")
	private String moduleName;

	@SerializedName("description")
	@ColumnInfo(name = "description")
	private String description;

	@SerializedName("active")
	@ColumnInfo(name = "active")
	private String active;

	@SerializedName("moduleMaxScore")
	@ColumnInfo(name = "moduleMaxScore")
	private String moduleMaxScore;

	@NonNull
	@PrimaryKey
	@ColumnInfo(name = "moduleId")
	@SerializedName("moduleId")
	private String moduleId;

	@SerializedName("version")
	@ColumnInfo(name = "version")
	private String version;

	@SerializedName("createDateTime")
	@ColumnInfo(name = "createDateTime")
	private String createDateTime;

	@SerializedName("questionsData")
	@ColumnInfo(name="questionsData")
	private String questionsData;

	@Nullable
	@SerializedName("attemptedQuestionsData")
	@ColumnInfo(name="attemptedQuestionsData")
	private String attemptedQuestionsData;

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getQuestionSet() {
		return questionSet;
	}

	public void setQuestionSet(String questionSet) {
		this.questionSet = questionSet;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getModuleMaxScore() {
		return moduleMaxScore;
	}

	public void setModuleMaxScore(String moduleMaxScore) {
		this.moduleMaxScore = moduleMaxScore;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getQuestionsData() {
		return questionsData;
	}

	public void setQuestionsData(String questionsData) {
		this.questionsData = questionsData;
	}

	public String getAttemptedQuestionsData() {
		return attemptedQuestionsData;
	}

	public void setAttemptedQuestionsData(String attemptedQuestionsData) {
		this.attemptedQuestionsData = attemptedQuestionsData;
	}

	@Override
	public String toString(){
		return
				"Modules{" +
						"moduleType = '" + moduleType + '\'' +
						",createdBy = '" + createdBy + '\'' +
						",questionSet = '" + questionSet + '\'' +
						",moduleName = '" + moduleName + '\'' +
						",description = '" + description + '\'' +
						",active = '" + active + '\'' +
						",moduleMaxScore = '" + moduleMaxScore + '\'' +
						",moduleId = '" + moduleId + '\'' +
						",version = '" + version + '\'' +
						",createDateTime = '" + createDateTime + '\'' +
						"}";
	}
}