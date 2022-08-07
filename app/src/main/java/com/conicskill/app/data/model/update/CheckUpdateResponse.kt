package com.conicskill.app.data.model.update

import com.google.gson.annotations.SerializedName

data class CheckUpdateResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: UpdateData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UpdateData(

	@field:SerializedName("latestVersion")
	val latestVersion: String? = null,

	@field:SerializedName("forceUpdate")
	val forceUpdate: String? = null
)
