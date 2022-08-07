package com.conicskill.app.data.model.jobNotification

import com.google.gson.annotations.SerializedName

data class JobRequest(

		@field:SerializedName("data")
		val data: PageParams? = null,

		@field:SerializedName("token")
		val token: String? = null
)

data class PageParams(

		@field:SerializedName("offset")
		val offset: Int? = null,

		@field:SerializedName("limit")
		val limit: Int? = null
)
