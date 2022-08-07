package com.conicskill.app.data.model.currentAffairDetails;

import com.google.gson.annotations.SerializedName;

public class CADetailResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private CADetailResponseData caDetailResponseData;

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private boolean error;

    @SerializedName("status")
    private String status;

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public void setCaDetailResponseData(CADetailResponseData caDetailResponseData){
        this.caDetailResponseData = caDetailResponseData;
    }

    public CADetailResponseData getCaDetailResponseData(){
        return caDetailResponseData;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean isError(){
        return error;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

//    @Override
//    public String toString(){
//        return
//                "CurrentResponse{" +
//                        "code = '" + code + '\'' +
//                        ",currentAffairsData = '" + currentAffairsData + '\'' +
//                        ",message = '" + message + '\'' +
//                        ",error = '" + error + '\'' +
//                        ",status = '" + status + '\'' +
//                        "}";
//    }

}


