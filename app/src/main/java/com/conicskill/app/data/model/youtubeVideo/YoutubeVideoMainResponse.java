package com.conicskill.app.data.model.youtubeVideo;

import com.conicskill.app.data.model.wordDay.wordData;
import com.google.gson.annotations.SerializedName;

public class YoutubeVideoMainResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private YoutubeResponse data;

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private boolean error;

    @SerializedName("status")
    private String status;

}
