package com.conicskill.app.data.model.notes

import com.google.gson.annotations.SerializedName
import com.idanatz.oneadapter.external.interfaces.Diffable

data class NotesListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("notesList")
	val notesList: List<NotesListItem> = arrayListOf(),

	@field:SerializedName("notesCount")
	val notesCount: String? = null
)

data class NotesListItem(

	@field:SerializedName("catId")
	val catId: String? = null,

	@field:SerializedName("notesId")
	val notesId: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("courseId")
	val courseId: String? = null,

	@field:SerializedName("url")
	val url: String? = null
): Diffable {
	override fun areContentTheSame(other: Any): Boolean {
		return false
	}

	override fun getUniqueIdentifier(): Long {
		return System.identityHashCode(this).toLong()
	}
}
