package com.conicskill.app.data.model.currentAffairDetails;

import com.google.gson.annotations.SerializedName;

public class CADetailRequest {

    @SerializedName("data")
    private CADetailRequestData caDetailRequestData;

    @SerializedName("token")
    private String token;

    public CADetailRequestData getCaDetailRequestData() {
        return caDetailRequestData;
    }

    public void setCaDetailRequestData(CADetailRequestData caDetailRequestData) {
        this.caDetailRequestData = caDetailRequestData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
