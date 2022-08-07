package com.conicskill.app.data.model.currentAffairDetails;

import com.google.gson.annotations.SerializedName;

public class CADetailRequestData {

    @SerializedName("currentAffairId")
    private String currentAffairId;

    public String getCurrentAffairId() {
        return currentAffairId;
    }

    public void setCurrentAffairId(String currentAffairId) {
        this.currentAffairId = currentAffairId;
    }
}
