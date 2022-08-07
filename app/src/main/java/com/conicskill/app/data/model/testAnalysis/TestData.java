package com.conicskill.app.data.model.testAnalysis;

import com.google.gson.annotations.SerializedName;

public class TestData{

	@SerializedName("score")
	private double score;

	@SerializedName("totalQuestions")
	private int totalQuestions;

	@SerializedName("timeTaken")
	private int timeTaken;

	@SerializedName("attemptedQuestions")
	private int attemptedQuestions;

	@SerializedName("correctAnswers")
	private int correctAnswers;

	@SerializedName("maxScore")
	private int maxScore;

	public double getScore(){
		return score;
	}

	public int getTotalQuestions(){
		return totalQuestions;
	}

	public int getTimeTaken(){
		return timeTaken;
	}

	public int getAttemptedQuestions(){
		return attemptedQuestions;
	}

	public int getCorrectAnswers(){
		return correctAnswers;
	}

	public int getMaxScore(){
		return maxScore;
	}
}