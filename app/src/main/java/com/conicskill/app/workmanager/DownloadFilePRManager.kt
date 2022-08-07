package com.conicskill.app.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import com.conicskill.app.base.BaseApplication.fetch
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.database.AppDatabase
import com.conicskill.app.database.DatabaseConstant
import com.conicskill.app.database.DownloadedVideo
import com.conicskill.app.database.Downloads
import com.conicskill.app.util.Utils
import com.google.gson.Gson
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import java.io.IOException


class DownloadFilePRManager(
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

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    lateinit var notification: Notification

    private var androidDownloadChannel: NotificationChannel? = null
    private var NOTIFICATION_ID: Int = BaseApplication.NOTIFICATION_ID

    override suspend fun doWork(): Result {
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
            }
            val course = inputData.getString(KEY_COURSE_ITEM) ?: return Result.failure()
            val category = inputData.getString(KEY_CATEGORY_ITEM) ?: return Result.failure()
            val subcategory = inputData.getString(KEY_SUBCATEGORY_ITEM) ?: return Result.failure()

            courseListItem = Gson().fromJson(course, CourseListItem::class.java)
            categoryListItem = Gson().fromJson(category, CategoryListItem::class.java)
            subCategoryItem = Gson().fromJson(subcategory, SubCategoryItem::class.java)

            NOTIFICATION_ID = System.identityHashCode(this)
            val foregroundInfo = ForegroundInfo(
                NOTIFICATION_ID,
                showProgress(
                    0, fileName, Utils.getDownloadSpeedString(
                        context,
                        0
                    )
                )
            )
            setForeground(foregroundInfo)
            var file = context.cacheDir.toString() + "/" + filePath
            val request = Request(
                fileUrl,
                file
            )
            request.priority = Priority.HIGH
            request.networkType = NetworkType.ALL

            fetch.enqueue(request, { updatedRequest ->

            }) { error ->

            }

            val fetchListener: FetchListener = object : FetchListener {
                override fun onQueued(@NotNull download: Download, waitingOnNetwork: Boolean) {
                    if (request.id == download.id) {

                    }
                }

                override fun onCompleted(@NotNull download: Download) {
                    insertCourse(courseListItem)
                }
                fun onError(@NotNull download: Download) {

                }

                override fun onProgress(
                    @NotNull download: Download,
                    etaInMilliSeconds: Long,
                    downloadedBytesPerSecond: Long
                ) {
                    if (request.id == download.id) {
                        showProgress(
                            download.progress, fileName, Utils.getDownloadSpeedString(
                                context,
                                downloadedBytesPerSecond
                            )
                        )
                    }
                }

                override fun onPaused(@NotNull download: Download) {}
                override fun onResumed(@NotNull download: Download) {}
                override fun onCancelled(@NotNull download: Download) {}
                override fun onRemoved(@NotNull download: Download) {}
                override fun onDeleted(@NotNull download: Download) {}
                override fun onAdded(download: Download) {

                }

                override fun onDownloadBlockUpdated(
                    download: Download,
                    downloadBlock: DownloadBlock,
                    totalBlocks: Int
                ) {

                }

                override fun onError(
                    download: Download,
                    error: Error,
                    throwable: Throwable?
                ) {
                    Log.e("TAG", "onError: " + error.httpResponse )
                }

                override fun onStarted(
                    download: Download,
                    downloadBlocks: List<DownloadBlock>,
                    totalBlocks: Int
                ) {

                }

                override fun onWaitingNetwork(download: Download) {

                }
            }

            fetch.addListener(fetchListener)
        } catch (e: IOException) {
            return Result.retry()
        }
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
        const val KEY_FILE_TYPE = "KEY_FILE_TYPE"
        const val KEY_VIDEO_QUALITY = "KEY_VIDEO_QUALITY"
        const val KEY_ORIGINAL_URL = "KEY_ORIGINAL_URL"
        const val FILE_TYPE_PDF = "FILE_TYPE_PDF"
        const val FILE_TYPE_VIDEO = "FILE_TYPE_VIDEO"
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
        } else {
            val downloads = DownloadedVideo()
            downloads.displayName = fileName
            downloads.downloadLocation = context.cacheDir.toString() + "/" + filePath
            downloads.courseId = courseListItem.courseId
            downloads.categoryId = subCategoryItem.catId!!
            downloads.subCategoryId = subCategoryItem.subCategoryId
            downloads.url = fileUrl
            downloads.videoQuality = videoQuality
            downloads.originalUrl = videoQuality
            mAppDatabase.moduleDao().insertDownloadsVideo(downloads).observeOn(Schedulers.io())
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
        }
    }
}
