package com.conicskill.app.util

import android.R
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.*


class FileUtils {

    //Kitkat or above
    fun getMimeTypeForUri(context: Context, finalUri: Uri): String =
            DocumentFile.fromSingleUri(context, finalUri)?.type ?: "application/octet-stream"

    //Just in case this is for Android 4.3 or below
    fun getMimeTypeForFile(finalFile: File): String =
            DocumentFile.fromFile(finalFile).type ?: "application/octet-stream"

    /*fun copyFileToDownloads(context: Context, downloadedFile: File): Uri? {
        val resolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, getName(downloadedFile))
                put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(downloadedFile))
                put(MediaStore.MediaColumns.SIZE, getFileSize(downloadedFile))
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val authority = "${context.packageName}.provider"
            val destinyFile = File(DOWNLOAD_DIR, getName(downloadedFile))
            FileProvider.getUriForFile(context, authority, destinyFile)
        }?.also { downloadedUri ->
            resolver.openOutputStream(downloadedUri).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream = BufferedInputStream(FileInputStream(downloadedFile.absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        }
    }*/

    private fun writeFileToStream(file: File, out: OutputStream): Boolean {
        try {
            FileInputStream(file).use { `in` ->
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) out.write(buf, 0, len)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }

    public fun galleryAddDocument(context: Context, originalFile: File) {
        if (!originalFile.exists()) {
            return
        }
        val pathSeparator = originalFile.toString().lastIndexOf('/')
        val extensionSeparator = originalFile.toString().lastIndexOf('.')
        val filename = if (pathSeparator >= 0) originalFile.toString().substring(pathSeparator + 1)
        else originalFile.toString()
        val extension = if (extensionSeparator >= 0) originalFile.toString().substring(extensionSeparator + 1) else ""
        val mimeType = if (extension.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase(Locale.ENGLISH)) else null
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.TITLE, filename)
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
        if (mimeType != null && mimeType.isNotEmpty()) values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        val externalContentUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI


        // Android 10 restricts our access to the raw filesystem, use MediaStore to save media in that case
        if (Build.VERSION.SDK_INT >= 29) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Downloads")
            values.put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.MediaColumns.IS_PENDING, true)
            val uri: Uri? = context.contentResolver.insert(externalContentUri, values)
            if (uri != null) {
                try {
                    if (writeFileToStream(originalFile, context.contentResolver.openOutputStream(uri)!!)) {
                        values.put(MediaStore.MediaColumns.IS_PENDING, false)
                        context.contentResolver.update(uri, values, null, null)
                    }
                } catch (e: java.lang.Exception) {
                    context.contentResolver.delete(uri, null, null)
                }
            }
            originalFile.delete()
        } else {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(originalFile)
        }
    }
}