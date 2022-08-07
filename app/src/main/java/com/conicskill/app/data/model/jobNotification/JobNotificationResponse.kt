package com.conicskill.app.data.model.jobNotification

import com.google.gson.annotations.SerializedName
import com.idanatz.oneadapter.external.interfaces.Diffable

data class JobNotificationResponse(

		@field:SerializedName("code")
		val code: Int? = null,

		@field:SerializedName("data")
		val data: JobNotification? = null,

		@field:SerializedName("message")
		val message: String? = null,

		@field:SerializedName("error")
		val error: Boolean? = null,

		@field:SerializedName("status")
		val status: String? = null
)

data class JobNotification(

		@field:SerializedName("count")
		val count: String? = null,

		@field:SerializedName("jobNotifications")
		val jobNotifications: List<JobNotificationsItem> = arrayListOf()
)

data class JobNotificationsItem(

		@field:SerializedName("jobId")
		val jobId: String? = null,

		@field:SerializedName("thumbnail")
		val thumbnail: Any? = null,

		@field:SerializedName("title")
		val title: String? = null
) : Diffable {
    override fun areContentTheSame(other: Any): Boolean {
        return other is JobNotificationsItem && jobId == other.jobId
    }

    override fun getUniqueIdentifier(): Long {
        return jobId!!.toLong()
    }
}
