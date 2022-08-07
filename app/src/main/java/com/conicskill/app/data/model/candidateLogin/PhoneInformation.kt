package com.conicskill.app.data.model.candidateLogin

import com.google.gson.annotations.SerializedName

data class PhoneInformation(
    @field:SerializedName("modelName")
    val modelName: String? = null,

    @field:SerializedName("manufacturer")
    val manufacturer: String? = null,

    @field:SerializedName("androidVersion")
    val androidVersion: String? = null,

    @field:SerializedName("androidSdkVersion")
    val androidSdkVersion: String? = null,


)
