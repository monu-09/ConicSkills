package com.conicskill.app.data.model.update

import com.google.gson.annotations.SerializedName

data class UpdateRequest(

	@field:SerializedName("data")
	val data: UpdateRequestData? = null
)

data class UpdateRequestData(

	@field:SerializedName("companyId")
	val companyId: Int? = null,

	@field:SerializedName("appVersion")
	val appVersion: Int? = null
)
