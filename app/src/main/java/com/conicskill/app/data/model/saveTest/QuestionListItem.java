package com.conicskill.app.data.model.saveTest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionListItem{

	@SerializedName("timeTaken")
	private String timeTaken;

	@SerializedName("questionId")
	private String questionId;

	@SerializedName("answerMarked")
	private List<String> answerMarked;

	public String getTimeTaken(){
		return timeTaken;
	}

	public String getQuestionId(){
		return questionId;
	}

	public List<String> getAnswerMarked(){
		return answerMarked;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public void setAnswerMarked(List<String> answerMarked) {
		this.answerMarked = answerMarked;
	}
}