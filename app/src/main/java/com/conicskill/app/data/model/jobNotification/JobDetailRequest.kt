package com.conicskill.app.data.model.jobNotification

import com.google.gson.annotations.SerializedName

data class JobDetailRequest(

	@field:SerializedName("data")
	val jobRequest: JobFetch? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class JobFetch(

	@field:SerializedName("jobId")
	val jobId: String? = null
)
