package com.conicskill.app.data.model.candidateLogin

import com.google.gson.annotations.SerializedName

data class CandidateLogOut(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("token")
	val token: String? = null
)
