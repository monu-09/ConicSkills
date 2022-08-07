package com.conicskill.app.data.model.jobNotification

import com.google.gson.annotations.SerializedName
import com.idanatz.oneadapter.external.interfaces.Diffable

data class JobDetailResponse(

		@field:SerializedName("code")
		val code: Int? = null,

		@field:SerializedName("data")
		val jobDetail: JobDetail? = null,

		@field:SerializedName("message")
		val message: String? = null,

		@field:SerializedName("error")
		val error: Boolean? = null,

		@field:SerializedName("status")
		val status: String? = null
)

data class JobDetail(

		@field:SerializedName("jobId")
		val jobId: String? = null,

		@field:SerializedName("jobNotificationLink")
		val jobNotificationLink: Any? = null,

		@field:SerializedName("thumbnail")
		val thumbnail: Any? = null,

		@field:SerializedName("description")
		val description: String? = null,

		@field:SerializedName("active")
		val active: String? = null,

		@field:SerializedName("title")
		val title: String? = null,

		@field:SerializedName("notificationDate")
		val notificationDate: Any? = null
) : Diffable {
    override fun areContentTheSame(other: Any): Boolean {
        return other is JobDetail && jobId == other.jobId
    }

    override fun getUniqueIdentifier(): Long {
        return jobId!!.toLong()
    }
}
