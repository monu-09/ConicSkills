package com.conicskill.app.ui.pdf

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.VideoFormat
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.data.model.videoCouses.VideoListItem
import com.conicskill.app.database.DownloadedVideo
import com.conicskill.app.databinding.DownnloadVideoFragmentBinding
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.util.Utils
import com.conicskill.app.workmanager.DownloadFileWorkManager
import com.conicskill.app.workmanager.DownloadFileYoutubeManager
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.mapper.VideoInfo
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class DownloadVideoFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var mViewModel: PdfViewModel
    private lateinit var _binding: DownnloadVideoFragmentBinding
    private lateinit var courseListItem: CourseListItem
    private lateinit var categoryListItem: CategoryListItem
    private lateinit var subCategoryItem: SubCategoryItem
    private lateinit var videoListItem: VideoListItem

    companion object {
        @JvmStatic
        fun newInstance(
            course: CourseListItem,
            category: CategoryListItem,
            subCategory: SubCategoryItem,
            video: VideoListItem
        ) =
            DownloadVideoFragment().apply {
                this.courseListItem = course
                this.categoryListItem = category
                this.subCategoryItem = subCategory
                this.videoListItem = video
            }
    }

    private lateinit var viewModel: DownloadVideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.downnload_video_fragment, container,
            false
        )
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this, viewModelFactory).get(
            PdfViewModel::class.java
        )
        val url: Array<String> = videoListItem.url.split("=".toRegex()).toTypedArray()
        mViewModel.mAppDatabase.moduleDao().checkWhetherVideoFileExists(url[1])
            .observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<DownloadedVideo>>() {
                override fun onSuccess(t: List<DownloadedVideo>) {
                    requireActivity().runOnUiThread {
                        if (t.isEmpty()) {
                            observeWorkManager(url[1])
                        } else {
                            _binding.progressBar.visibility = View.GONE
                            _binding.constraintDownloading.visibility = View.GONE
                            _binding.constraintDownloaded.visibility = View.VISIBLE
                            _binding.constraintMain.visibility = View.GONE

                            _binding.buttonWatch.setOnClickListener {
                                val intent = Intent(requireContext(), LocalVideoPlayActivity::class.java)
                                intent.putExtra("filePath", t[0].downloadLocation)
                                startActivity(intent)
                            }
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG", "onError: " + e.localizedMessage)
                }
            })
    }

    private fun observeWorkManager(url: String) {
        WorkManager.getInstance(requireContext().applicationContext)
            .getWorkInfosForUniqueWorkLiveData(
                url
            ).observe(viewLifecycleOwner, {
                if(it.isNotEmpty()) {
                    if(it[0].state == WorkInfo.State.RUNNING) {
                        _binding.progressBar.visibility = View.GONE
                        _binding.constraintDownloading.visibility = View.VISIBLE
                        _binding.constraintDownloaded.visibility = View.GONE
                        _binding.constraintMain.visibility = View.GONE
                    } else if(it[0].state == WorkInfo.State.SUCCEEDED) {
                        mViewModel.mAppDatabase.moduleDao().checkWhetherVideoFileExists(url)
                            .observeOn(Schedulers.io())
                            .subscribe(object : DisposableSingleObserver<List<DownloadedVideo>>() {
                                override fun onSuccess(t: List<DownloadedVideo>) {
                                    if(t.isNotEmpty()) {
                                        requireActivity().runOnUiThread {
                                            _binding.progressBar.visibility = View.GONE
                                            _binding.constraintDownloading.visibility = View.GONE
                                            _binding.constraintDownloaded.visibility = View.VISIBLE
                                            _binding.constraintMain.visibility = View.GONE

                                            _binding.buttonWatch.setOnClickListener {
                                                val intent = Intent(
                                                    requireContext(),
                                                    LocalVideoPlayActivity::class.java
                                                )
                                                intent.putExtra("filePath", t[0].downloadLocation)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    Log.e("TAG", "onError: " + e.localizedMessage)
                                }
                            })
                    }
                } else {
                    extractYoutubeDlUrls()
                }
        })
    }

    private fun extractYoutubeDlUrls() {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val videoList = ArrayList<VideoFormat>()
                var vidInfo: VideoInfo? = null
                val url: Array<String> = videoListItem.url.split("=".toRegex()).toTypedArray()
                vidInfo =
                    YoutubeDL.getInstance().getInfo("https://www.youtube.com/watch?v=${url[1]}")
                vidInfo?.formats?.forEach {
                    if (it.acodec == "none" && it.vcodec != "none" && !(it.formatNote.uppercase()
                            .contains("DASH"))
                    ) {
                        videoList.add(
                            VideoFormat(
                                it.formatNote,
                                it.formatId,
                                it.filesize,
                                it.acodec,
                                it.vcodec,
                                url[1],
                                it.ext
                            )
                        )
                    }
                }
                launch(Dispatchers.Main) {
                    _binding.constraintDownloading.visibility = View.GONE
                    _binding.constraintDownloaded.visibility = View.GONE
                    _binding.constraintMain.visibility = View.VISIBLE
                    _binding.recyclerViewQuality.layoutManager = LinearLayoutManager(
                        context,
                        RecyclerView.VERTICAL,
                        false
                    )
                    var oneAdapter = OneAdapter(_binding.recyclerViewQuality)
                        .attachItemModule(videoDownloadModule())
                    oneAdapter.setItems(videoList)
                    _binding.progressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            _binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun videoDownloadModule(): ItemModule<VideoFormat> {
        return object : ItemModule<VideoFormat>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_video_download
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<VideoFormat>, viewBinder: ViewBinder) {
                var buttonDownload: MaterialButton = viewBinder.findViewById(R.id.buttonDownload)
                buttonDownload.text = Utils.getQualityString(item.model.formatNote) + " - " +
                        Utils.getBytesToMBString(item.model.filesize)
                buttonDownload.setOnClickListener {
                    _binding.progressBar.visibility = View.GONE
                    _binding.constraintDownloading.visibility = View.VISIBLE
                    _binding.constraintDownloaded.visibility = View.GONE
                    _binding.constraintMain.visibility = View.GONE
                    val data = Data.Builder()
                    data.putString(DownloadFileWorkManager.KEY_INPUT_URL, videoListItem.url)
                    data.putString(
                        DownloadFileWorkManager.KEY_NAME,
                        videoListItem.title
                    )
                    data.putString(
                        DownloadFileYoutubeManager.KEY_FILE_THUMBNAIL,
                        videoListItem.thumbnail
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_OUTPUT_FILE_NAME,
                        videoListItem.title + ".mp4"
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_COURSE_ITEM,
                        Gson().toJson(courseListItem)
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_CATEGORY_ITEM,
                        Gson().toJson(categoryListItem)
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_SUBCATEGORY_ITEM,
                        Gson().toJson(subCategoryItem)
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_VIDEO_FORMAT_ITEM,
                        Gson().toJson(item.model)
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_FILE_TYPE,
                        DownloadFileWorkManager.FILE_TYPE_VIDEO
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_VIDEO_QUALITY,
                        item.model.formatNote
                    )
                    data.putString(
                        DownloadFileWorkManager.KEY_ORIGINAL_URL,
                        videoListItem.url
                    )
                    val downloadWorkRequest = OneTimeWorkRequest.Builder(
                        DownloadFileYoutubeManager::class.java
                    )
                        .addTag(item.model.videoId)
                        .setInputData(data.build())
                        .build()

                    WorkManager.getInstance(requireContext()).enqueueUniqueWork(
                        item.model.videoId,
                        ExistingWorkPolicy.KEEP,
                        downloadWorkRequest
                    )
                }
            }
        }
    }

    override fun layoutRes(): Int {
        return R.layout.downnload_video_fragment
    }
}