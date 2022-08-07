package com.conicskill.app.data.model.home

import com.google.gson.annotations.SerializedName

data class HomeLayoutRequest(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("token")
	val token: String? = null
)
