package com.conicskill.app.data.model.carousel;

import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.wordDay.WordOfTheDay;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DisplayData implements Serializable {

    @SerializedName("questionData")
    private QuestionData questionData;

    @SerializedName("wordOfTheDay")
    private WordOfTheDay wordOfTheDay;

    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("embeddedUrl")
    @Expose
    private String embeddedUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("filePath")
    @Expose
    private Object filePath;
    @SerializedName("hostedAt")
    @Expose
    private String hostedAt;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("isPaid")
    @Expose
    private String isPaid;
    @SerializedName("isDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("tags")
    @Expose
    private Object tags;
    @SerializedName("streamUrl")
    @Expose
    private Object streamUrl;
    @SerializedName("streamKey")
    @Expose
    private Object streamKey;
    @SerializedName("eventDateTime")
    @Expose
    private String eventDateTime;
    @SerializedName("duration")
    @Expose
    private Object duration;

    @SerializedName("courseData")
    @Expose
    private List<CourseListItem> courseData = new ArrayList<>();

    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("bannerType")
    @Expose
    private String bannerType;
    @SerializedName("candidateId")
    @Expose
    private String candidateId;

    @Expose
    @SerializedName("chatId")
    private String chatId;

    @Expose
    @SerializedName("upcoming")
    private int upcoming;

    @Expose
    @SerializedName("isLive")
    private int isLive;

    @Expose
    @SerializedName("pdfUrl")
    private String pdfUrl;

    @Expose
    @SerializedName("chatEnabled")
    private String chatEnabled;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getEmbeddedUrl() {
        return embeddedUrl;
    }

    public void setEmbeddedUrl(String embeddedUrl) {
        this.embeddedUrl = embeddedUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getFilePath() {
        return filePath;
    }

    public void setFilePath(Object filePath) {
        this.filePath = filePath;
    }

    public String getHostedAt() {
        return hostedAt;
    }

    public void setHostedAt(String hostedAt) {
        this.hostedAt = hostedAt;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public Object getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(Object streamUrl) {
        this.streamUrl = streamUrl;
    }

    public Object getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(Object streamKey) {
        this.streamKey = streamKey;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public QuestionData getQuestionData() {
        return questionData;
    }

    public void setQuestionData(QuestionData questionData) {
        this.questionData = questionData;
    }

    public WordOfTheDay getWordOfTheDay() {
        return wordOfTheDay;
    }

    public void setWordOfTheDay(WordOfTheDay wordOfTheDay) {
        this.wordOfTheDay = wordOfTheDay;
    }

    public List<CourseListItem> getCourseData() {
        return courseData;
    }

    public void setCourseData(List<CourseListItem> courseData) {
        this.courseData = courseData;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(int upcoming) {
        this.upcoming = upcoming;
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
}