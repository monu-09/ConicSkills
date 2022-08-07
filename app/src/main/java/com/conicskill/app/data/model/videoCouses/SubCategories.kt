package com.conicskill.app.data.model.videoCouses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.idanatz.oneadapter.external.interfaces.Diffable
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "Subcategory")
data class SubCategoryItem(

	@field:SerializedName("videoCount")
	@ColumnInfo(name = "videoCount")
	val videoCount: String? = null,

	@field:SerializedName("catId")
	@ColumnInfo(name = "catId")
	val catId: String? = null,

	@field:SerializedName("subCategory")
	@ColumnInfo(name = "subCategory")
	val subCategory: String? = null,

	@NotNull
	@PrimaryKey
	@field:SerializedName("subCategoryId")
	@ColumnInfo(name = "subCategoryId")
	val subCategoryId: String,

	@field:SerializedName("pdfCount")
	@ColumnInfo(name = "pdfCount")
	val pdfCount: String? = null
): Diffable, Serializable {
	override fun areContentTheSame(other: Any): Boolean {
		return false
	}

	override fun getUniqueIdentifier(): Long {
		return System.identityHashCode(this).toLong()
	}
}
