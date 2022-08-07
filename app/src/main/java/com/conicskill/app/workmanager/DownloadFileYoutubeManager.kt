package com.conicskill.app.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.conicskill.app.R
import com.conicskill.app.base.BaseApplication
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.database.AppDatabase
import com.conicskill.app.database.DatabaseConstant
import com.conicskill.app.database.DownloadedVideo
import com.conicskill.app.database.Downloads
import com.conicskill.app.data.model.VideoFormat
import com.conicskill.app.util.Utils
import com.google.gson.Gson
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.IOException


class DownloadFileYoutubeManager(
    private val context: Context,
    private val workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    lateinit var mAppDatabase: AppDatabase

    private lateinit var courseListItem: CourseListItem
    private lateinit var categoryListItem: CategoryListItem
    private lateinit var subCategoryItem: SubCategoryItem
    private var fileUrl: String = ""
    private var fileName: String = ""
    private var filePath: String = ""
    private var type: String = ""
    private var videoQuality: String = ""
    private var originalUrl: String = ""
    private var thumbnailUrl:String = ""
    private var notificationTime: Long = 0L
    var destUri: Uri? = null
    private lateinit var videoFormatItem: VideoFormat

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    lateinit var notification: Notification

    private var androidDownloadChannel: NotificationChannel? = null
    private var NOTIFICATION_ID: Int = BaseApplication.NOTIFICATION_ID

    override suspend fun doWork(): Result {
        val tmpFile = File.createTempFile(context.packageName, null, context.cacheDir)
        try {
            mAppDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DatabaseConstant.DATABASE_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

            fileUrl = inputData.getString(KEY_INPUT_URL)
                ?: return Result.failure()
            fileName = inputData.getString(KEY_NAME)
                ?: return Result.failure()
            filePath = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()
            type = inputData.getString(KEY_FILE_TYPE) ?: return Result.failure()
            if (type == FILE_TYPE_VIDEO) {
                videoQuality = inputData.getString(KEY_VIDEO_QUALITY) ?: return Result.failure()
                originalUrl = inputData.getString(KEY_ORIGINAL_URL) ?: return Result.failure()
                thumbnailUrl = inputData.getString(KEY_FILE_THUMBNAIL) ?: return Result.failure()
            }
            val course = inputData.getString(KEY_COURSE_ITEM) ?: return Result.failure()
            val category = inputData.getString(KEY_CATEGORY_ITEM) ?: return Result.failure()
            val subcategory = inputData.getString(KEY_SUBCATEGORY_ITEM) ?: return Result.failure()
            val videoFormat = inputData.getString(KEY_VIDEO_FORMAT_ITEM) ?: return Result.failure()

            courseListItem = Gson().fromJson(course, CourseListItem::class.java)
            categoryListItem = Gson().fromJson(category, CategoryListItem::class.java)
            subCategoryItem = Gson().fromJson(subcategory, SubCategoryItem::class.java)
            videoFormatItem = Gson().fromJson(videoFormat, VideoFormat::class.java)

            NOTIFICATION_ID = id.hashCode()
            notificationTime = System.currentTimeMillis()
            val foregroundInfo = ForegroundInfo(
                NOTIFICATION_ID,
                showProgress(
                    0, fileName, Utils.getETAString(context, 0)
                )
            )
            setForeground(foregroundInfo)

            val request =
                YoutubeDLRequest("https://www.youtube.com/watch?v=${videoFormatItem.videoId}")
            tmpFile.delete()
            tmpFile.mkdir()
            tmpFile.deleteOnExit()
            request.addOption("-o", "${tmpFile.absolutePath}/${fileName}.%(ext)s")
            val videoOnly = videoFormatItem.vcodec != "none" && videoFormatItem.acodec == "none"
            if (videoOnly) {
                request.addOption("-f", "${videoFormatItem.formatId}+bestaudio")
            }

            destUri = Uri.fromFile(
                File(
                    context.cacheDir.absolutePath + "/" + fileName + "." +
                            videoFormatItem.ext
                )
            )
            YoutubeDL.getInstance()
                .execute(request) { progress, etaInSeconds ->
                    showProgress(
                        progress.toInt(), fileName, Utils.getETAString(context, etaInSeconds)
                    )
                }

            tmpFile.listFiles()?.forEach {
                val ins = it.inputStream()
                val ops = applicationContext.contentResolver.openOutputStream(destUri!!)
                IOUtils.copy(ins, ops)
                IOUtils.closeQuietly(ops)
                IOUtils.closeQuietly(ins)
            }
        } catch (e: IOException) {
            return Result.retry()
        } finally {
            tmpFile.deleteRecursively()
        }
        insertCourse(courseListItem)

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val id = applicationContext.getString(R.string.download_channel_id)
        val channelName = applicationContext.getString(R.string.download_channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidDownloadChannel = notificationManager.getNotificationChannel(id)
            if (androidDownloadChannel == null) {
                androidDownloadChannel = NotificationChannel(
                    id, channelName, NotificationManager.IMPORTANCE_LOW
                )
            }
            notificationManager.createNotificationChannel(androidDownloadChannel!!)
        }
    }

    private fun showProgress(
        progress: Int,
        name: String,
        progressDisplayLine: String
    ): Notification {
        val id = applicationContext.getString(R.string.download_channel_id)
        val cancel = applicationContext.getString(R.string.text_cancel)
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        notification = NotificationCompat.Builder(applicationContext, id)
            .setSmallIcon(R.drawable.icon_square)
            .setSubText(progressDisplayLine)
            .setWhen(notificationTime)
            .setContentTitle("Downloading $name ...")
            .setProgress(100, progress, false)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
        return notification
    }

    companion object {
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
        const val KEY_NAME = "KEY_NAME"
        const val KEY_COURSE_ITEM = "KEY_COURSE_ITEM"
        const val KEY_CATEGORY_ITEM = "KEY_CATEGORY_ITEM"
        const val KEY_SUBCATEGORY_ITEM = "KEY_SUBCATEGORY_ITEM"
        const val KEY_VIDEO_FORMAT_ITEM = "KEY_VIDEO_FORMAT_ITEM"
        const val KEY_FILE_TYPE = "KEY_FILE_TYPE"
        const val KEY_VIDEO_QUALITY = "KEY_VIDEO_QUALITY"
        const val KEY_ORIGINAL_URL = "KEY_ORIGINAL_URL"
        const val FILE_TYPE_PDF = "FILE_TYPE_PDF"
        const val FILE_TYPE_VIDEO = "FILE_TYPE_VIDEO"
        const val KEY_FILE_THUMBNAIL = "KEY_FILE_THUMBNAIL"
    }

    private fun insertCourse(courseListItem: CourseListItem) {
        mAppDatabase.moduleDao().insertCourse(courseListItem).observeOn(Schedulers.io())
            .subscribe(object : DisposableMaybeObserver<Long?>() {
                override fun onSuccess(aLong: Long) {
                    Log.e("TAG, onSuccess: ", aLong.toString())
                    // call insert
                    categoryListItem.courseId = courseListItem.courseId
                    insertCategory(categoryListItem)
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG, onError: ", e.localizedMessage)
                }

                override fun onComplete() {
                    Log.e("TAG, onComplete: ", "Completed")
                }
            })
    }

    private fun insertCategory(category: CategoryListItem) {
        // insert category details
        mAppDatabase.moduleDao().insertCategory(category).observeOn(Schedulers.io())
            .subscribe(object : DisposableMaybeObserver<Long?>() {
                override fun onSuccess(aLong: Long) {
                    Log.e("TAG, onSuccess: ", aLong.toString())
                    // call insert subcategory
                    insertSubCategory(subCategoryItem)
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG, onError: ", e.localizedMessage)
                }

                override fun onComplete() {
                    Log.e("TAG, onComplete: ", "Completed")
                }
            })
    }

    private fun insertSubCategory(subCategoryItem: SubCategoryItem) {
        // insert category details
        mAppDatabase.moduleDao().insertSubCategory(subCategoryItem)
            .observeOn(Schedulers.io())
            .subscribe(object : DisposableMaybeObserver<Long?>() {
                override fun onSuccess(aLong: Long) {
                    Log.e("TAG, onSuccess: ", aLong.toString())
                    // call insert subcategory
                    insertDownloads()
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG, onError: ", e.localizedMessage)
                }

                override fun onComplete() {
                    Log.e("TAG, onComplete: ", "Completed")
                }
            })
    }

    private fun insertDownloads() {
        if (type.equals(FILE_TYPE_PDF)) {
            val downloads = Downloads()
            downloads.displayName = fileName
            downloads.downloadLocation = context.cacheDir.toString() + "/" + filePath
            downloads.courseId = courseListItem.courseId
            downloads.categoryId = subCategoryItem.catId!!
            downloads.subCategoryId = subCategoryItem.subCategoryId
            downloads.url = fileUrl
            mAppDatabase.moduleDao().insertDownloads(downloads).observeOn(Schedulers.io())
                .subscribe(object : DisposableMaybeObserver<Long?>() {
                    override fun onSuccess(aLong: Long) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG , onError: ", e.localizedMessage)
                    }

                    override fun onComplete() {
                        Log.e("TAG, onComplete: ", "")
                    }
                })
        } else {
            val downloads = DownloadedVideo()
            downloads.displayName = fileName
            downloads.downloadLocation = context.cacheDir.toString() + "/" + fileName + "." +
                    videoFormatItem.ext
            downloads.courseId = courseListItem.courseId
            downloads.categoryId = subCategoryItem.catId!!
            downloads.subCategoryId = subCategoryItem.subCategoryId
            downloads.url = fileUrl
            downloads.videoQuality = videoQuality
            downloads.originalUrl = videoFormatItem.videoId
            downloads.thumbnail = thumbnailUrl
            mAppDatabase.moduleDao().insertDownloadsVideo(downloads).observeOn(Schedulers.io())
                .subscribe(object : DisposableMaybeObserver<Long?>() {
                    override fun onSuccess(aLong: Long) {
                        val id = applicationContext.getString(R.string.download_channel_id)
                        val cancel = applicationContext.getString(R.string.text_cancel)

                        // Create a Notification channel if necessary
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            createChannel()
                        }
                        notification = NotificationCompat.Builder(applicationContext, id)
                            .setSmallIcon(R.drawable.ic_done_all_green_800_24dp)
                            .setContentTitle("Download Completed $fileName")
                            .build()
                        notificationManager.notify(NOTIFICATION_ID, notification)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG , onError: ", e.localizedMessage)
                    }

                    override fun onComplete() {
                        Log.e("TAG, onComplete: ", "")
                    }
                })
        }
    }
}
