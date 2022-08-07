package com.conicskill.app.data.model.candidateLogin

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)
