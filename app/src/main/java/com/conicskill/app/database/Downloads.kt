package com.conicskill.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idanatz.oneadapter.external.interfaces.Diffable
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Downloads")
class Downloads: Diffable {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="_id")
    var downloadId: Int = 0

    @ColumnInfo(name = "displayName")
    lateinit var displayName: String

    @ColumnInfo(name ="courseId")
    lateinit var courseId: String

    @ColumnInfo(name = "subCategoryId")
    lateinit var subCategoryId: String

    @ColumnInfo(name ="categoryId")
    lateinit var categoryId: String

    @ColumnInfo(name = "url")
    lateinit var url: String

    @ColumnInfo(name = "downloadLocation")
    lateinit var downloadLocation: String

    override fun areContentTheSame(other: Any): Boolean {
        return false
    }

    override fun getUniqueIdentifier(): Long {
        return System.identityHashCode(this).toLong()
    }
}