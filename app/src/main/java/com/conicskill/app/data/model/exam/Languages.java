package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

public class Languages {

    @SerializedName("languageId")
    private String languageId;

    @SerializedName("language")
    private String language;

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
