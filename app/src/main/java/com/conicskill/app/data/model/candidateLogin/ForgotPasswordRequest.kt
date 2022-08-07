package com.conicskill.app.data.model.candidateLogin

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(

	@field:SerializedName("data")
	val data: ForgotPasswordData? = null
)

data class ForgotPasswordData(

	@field:SerializedName("companyId")
	val companyId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
