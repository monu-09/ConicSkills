package com.conicskill.app.data.model.help

import com.google.gson.annotations.SerializedName

data class HelpRequest(

	@field:SerializedName("data")
	val data: HelpData? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class HelpData(

	@field:SerializedName("companyId")
	val companyId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("enquiryId")
	val enquiryId: Int? = null,

	@field:SerializedName("source")
	val source: String = "Android App",

	@field:SerializedName("candidateId")
	val candidateId: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = "1"
)
