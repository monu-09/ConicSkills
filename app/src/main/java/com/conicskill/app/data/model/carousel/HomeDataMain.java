package com.conicskill.app.data.model.carousel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import java.io.Serializable;

public class HomeDataMain implements Serializable, Diffable {

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("id")
    private String id;

    @SerializedName("displayData")
    private DisplayData displayData;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DisplayData getDisplayData() {
        return displayData;
    }

    public void setDisplayData(DisplayData displayData) {
        this.displayData = displayData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean areContentTheSame(@NonNull Object o) {
        return false;
    }

    @Override
    public long getUniqueIdentifier() {
        return 0;
    }
}