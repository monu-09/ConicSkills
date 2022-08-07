package com.conicskill.app.data.model.examlisting;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.intrusoft.sectionedrecyclerview.Section;

public class TestListItem implements Section<TestSeriesItem> {

	@SerializedName("topic")
	private String topic;

	@SerializedName("testSeries")
	private List<TestSeriesItem> testSeries;

	public TestListItem(List<TestSeriesItem> childList, String sectionText) {
		this.testSeries = childList;
		this.topic = sectionText;
	}

	private boolean expanded = false;

	public void setTopic(String topic){
		this.topic = topic;
	}

	public String getTopic(){
		return topic;
	}

	public void setTestSeries(List<TestSeriesItem> testSeries){
		this.testSeries = testSeries;
	}

	public List<TestSeriesItem> getTestSeries(){
		return testSeries;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	@Override
 	public String toString(){
		return 
			"TestListItem{" + 
			"topic = '" + topic + '\'' + 
			",testSeries = '" + testSeries + '\'' + 
			"}";
		}

	@Override
	public List<TestSeriesItem> getChildItems() {
		return testSeries;
	}
}