package com.conicskill.app.data.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginWrapper {

    @SerializedName("body")
    private LoginRequest request;

    public LoginRequest getRequest() {
        return request;
    }

    public void setRequest(LoginRequest request) {
        this.request = request;
    }
}
