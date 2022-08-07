package com.conicskill.app.data.model.currentAffairs;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

public class CurrentAffairsItem implements Diffable {

	@SerializedName("langCode")
	private String langCode;

	@SerializedName("currentAffairId")
	private String currentAffairId;

	@SerializedName("active")
	private String active;

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	@SerializedName("priority")
	private String priority;

	@SerializedName("tags")
	private Object tags;

	@SerializedName("relatedId")
	private Object relatedId;

	@SerializedName("expiryDate")
	private String expiryDate;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("updateDateTime")
	private String updateDateTime;

	@SerializedName("smallBody")
	private String smallBody;

	@SerializedName("refrences")
	private String refrences;

	public void setLangCode(String langCode){
		this.langCode = langCode;
	}

	public String getLangCode(){
		return langCode;
	}

	public void setCurrentAffairId(String currentAffairId){
		this.currentAffairId = currentAffairId;
	}

	public String getCurrentAffairId(){
		return currentAffairId;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	public void setPriority(String priority){
		this.priority = priority;
	}

	public String getPriority(){
		return priority;
	}

	public void setTags(Object tags){
		this.tags = tags;
	}

	public Object getTags(){
		return tags;
	}

	public void setRelatedId(Object relatedId){
		this.relatedId = relatedId;
	}

	public Object getRelatedId(){
		return relatedId;
	}

	public void setExpiryDate(String expiryDate){
		this.expiryDate = expiryDate;
	}

	public String getExpiryDate(){
		return expiryDate;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setUpdateDateTime(String updateDateTime){
		this.updateDateTime = updateDateTime;
	}

	public String getUpdateDateTime(){
		return updateDateTime;
	}

	public void setSmallBody(String smallBody){
		this.smallBody = smallBody;
	}

	public String getSmallBody(){
		return smallBody;
	}

	public void setRefrences(String refrences){
		this.refrences = refrences;
	}

	public String getRefrences(){
		return refrences;
	}

	@Override
 	public String toString(){
		return 
			"CurrentAffairsItem{" + 
			"langCode = '" + langCode + '\'' + 
			",currentAffairId = '" + currentAffairId + '\'' + 
			",active = '" + active + '\'' + 
			",title = '" + title + '\'' + 
			",body = '" + body + '\'' + 
			",priority = '" + priority + '\'' + 
			",tags = '" + tags + '\'' + 
			",relatedId = '" + relatedId + '\'' + 
			",expiryDate = '" + expiryDate + '\'' + 
			",imageUrl = '" + imageUrl + '\'' + 
			",updateDateTime = '" + updateDateTime + '\'' + 
			",smallBody = '" + smallBody + '\'' + 
			",refrences = '" + refrences + '\'' + 
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