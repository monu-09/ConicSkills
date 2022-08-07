package com.conicskill.app.data.model.testAnalysis;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

public class ModuleDataItem implements Diffable {

	@SerializedName("score")
	private double score;

	@SerializedName("timeTaken")
	private int timeTaken;

	@SerializedName("totalQuestions")
	private int totalQuestions;

	@SerializedName("moduleType")
	private int moduleType;

	@SerializedName("moduleName")
	private String moduleName;

	@SerializedName("attemptedQuestions")
	private int attemptedQuestions;

	@SerializedName("moduleId")
	private int moduleId;

	@SerializedName("maxScore")
	private double maxScore;

	@SerializedName("correctAnswers")
	private int correctAnswers;

	@SerializedName("version")
	private int version;

	public double getScore(){
		return score;
	}

	public int getTimeTaken(){
		return timeTaken;
	}

	public int getTotalQuestions(){
		return totalQuestions;
	}

	public int getModuleType(){
		return moduleType;
	}

	public String getModuleName(){
		return moduleName;
	}

	public int getAttemptedQuestions(){
		return attemptedQuestions;
	}

	public int getModuleId(){
		return moduleId;
	}

	public double getMaxScore(){
		return maxScore;
	}

	public int getCorrectAnswers(){
		return correctAnswers;
	}

	public int getVersion(){
		return version;
	}

	@Override
	public boolean areContentTheSame(@NotNull Object o) {
		return false;
	}

	@Override
	public long getUniqueIdentifier() {
		return moduleId;
	}
}