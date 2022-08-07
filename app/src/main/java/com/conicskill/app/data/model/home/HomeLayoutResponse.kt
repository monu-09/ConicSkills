package com.conicskill.app.data.model.home

import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.google.gson.annotations.SerializedName
import com.idanatz.oneadapter.external.interfaces.Diffable

data class HomeLayoutResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: LayoutData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class LayoutItem(

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("contentType")
    val contentType: String? = null,

    @field:SerializedName("content")
    val content: List<CourseListItem?>? = null,

    var isSelected: Boolean = false
): Diffable {
    override fun areContentTheSame(other: Any): Boolean {
        return false
    }

    override fun getUniqueIdentifier(): Long {
        return System.identityHashCode(this).toLong()
    }
}

data class LayoutData(

    @field:SerializedName("layout")
    val layout: List<LayoutItem?>? = null
)
