package com.conicskill.app.data.model.notes

import com.google.gson.annotations.SerializedName

data class NotesListRequest(

	@field:SerializedName("data")
	val notesData: NoteData? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class NoteData(

	@field:SerializedName("catId")
	val catId: Int? = null,

	@field:SerializedName("courseId")
	val courseId: String? = null
)
