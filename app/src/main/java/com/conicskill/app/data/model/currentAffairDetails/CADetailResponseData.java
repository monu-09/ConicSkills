package com.conicskill.app.data.model.currentAffairDetails;

import com.google.gson.annotations.SerializedName;

public class CADetailResponseData {

    @SerializedName("currentAffairId")
    private String currentAffairId;

    @SerializedName("title")
    private String title;

    @SerializedName("smallBody")
    private String smallBody;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("body")
    private String body;

    @SerializedName("priority")
    private String priority;

    @SerializedName("langCode")
    private String langCode;

    @SerializedName("updateDateTime")
    private String updateDateTime;

    @SerializedName("expiryDate")
    private String expiryDate;

    @SerializedName("active")
    private String active;

    @SerializedName("references")
    private String references;

    @SerializedName("tags")
    private String tags;

    @SerializedName("relatedId")
    private String relatedId;

    public String getCurrentAffairId() {
        return currentAffairId;
    }

    public void setCurrentAffairId(String currentAffairId) {
        this.currentAffairId = currentAffairId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallBody() {
        return smallBody;
    }

    public void setSmallBody(String smallBody) {
        this.smallBody = smallBody;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }
}
