package com.conicskill.app.data.model.videoCouses;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class VideoListItem implements Diffable, Serializable {

	@SerializedName("duration")
	private String duration;

	@SerializedName("password")
	private String password;

	@SerializedName("hostedAt")
	private String hostedAt;

	@SerializedName("description")
	private String description;

	@SerializedName("videoId")
	private String videoId;

	@SerializedName("title")
	private String title;

	@SerializedName("videoOrder")
	private String videoOrder;

	@SerializedName("Url")
	private String url;

	@SerializedName("embeddedUrl")
	private String embeddedUrl;

	@SerializedName("tags")
	private String tags;

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("isPaid")
	private String isPaid;

	@SerializedName("chatId")
	private String chatId;

	@SerializedName("upcoming")
	private int upcoming;

	@SerializedName("isLive")
	private int isLive;

	@SerializedName("pdfUrl")
	private String pdfUrl;

	@SerializedName("chatEnabled")
	private String chatEnabled;

	@SerializedName("eventDateTime")
	private String eventDateTime;

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setHostedAt(String hostedAt){
		this.hostedAt = hostedAt;
	}

	public String getHostedAt(){
		return hostedAt;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return videoId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setVideoOrder(String videoOrder){
		this.videoOrder = videoOrder;
	}

	public String getVideoOrder(){
		return videoOrder;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setEmbeddedUrl(String embeddedUrl){
		this.embeddedUrl = embeddedUrl;
	}

	public String getEmbeddedUrl(){
		return embeddedUrl;
	}

	public void setTags(String tags){
		this.tags = tags;
	}

	public String getTags(){
		return tags;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public int isUpcoming() {
		return upcoming;
	}

	public void setUpcoming(int upcoming) {
		this.upcoming = upcoming;
	}

	public int isLive() {
		return isLive;
	}

	public void setLive(int live) {
		isLive = live;
	}

	public int getUpcoming() {
		return upcoming;
	}

	public int getIsLive() {
		return isLive;
	}

	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getChatEnabled() {
		return chatEnabled;
	}

	public void setChatEnabled(String chatEnabled) {
		this.chatEnabled = chatEnabled;
	}

	public String getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	@Override
	public String toString(){
		return
				"VideoListItem{" +
						"duration = '" + duration + '\'' +
						",password = '" + password + '\'' +
						",hostedAt = '" + hostedAt + '\'' +
						",description = '" + description + '\'' +
						",videoId = '" + videoId + '\'' +
						",title = '" + title + '\'' +
						",videoOrder = '" + videoOrder + '\'' +
						",url = '" + url + '\'' +
						",embeddedUrl = '" + embeddedUrl + '\'' +
						",tags = '" + tags + '\'' +
						"}";
	}

	@Override
	public boolean areContentTheSame(@NotNull Object o) {
		return false;
	}

	@Override
	public long getUniqueIdentifier() {
		return System.identityHashCode(this);
	}
}