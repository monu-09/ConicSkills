package com.conicskill.app.data.model

import com.idanatz.oneadapter.external.interfaces.Diffable

data class VideoFormat(
    val formatNote: String,
    val formatId: String,
    val filesize: Long,
    val acodec: String,
    val vcodec: String,
    val videoId: String,
    val ext: String
): Diffable {
    override fun areContentTheSame(other: Any): Boolean {
        return false
    }

    override fun getUniqueIdentifier(): Long {
        return System.identityHashCode(this).toLong()
    }

}
