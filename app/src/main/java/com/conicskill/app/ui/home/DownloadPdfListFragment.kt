package com.conicskill.app.ui.home

import android.animation.Animator
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.notes.NotesListItem
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.database.Downloads
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.ui.GDriveActivity
import com.conicskill.app.ui.courseDetail.SubCategoryFragment
import com.conicskill.app.ui.pdf.PdfViewModel
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import androidx.core.content.FileProvider

import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.lang.Exception
import java.util.*

import android.content.ActivityNotFoundException
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conicskill.app.BuildConfig
import com.conicskill.app.database.DownloadedVideo
import com.conicskill.app.ui.pdf.LocalVideoPlayActivity
import com.conicskill.app.util.CommonView
import com.conicskill.app.util.GlideApp
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.mikepenz.materialize.color.Material


class DownloadPdfListFragment : BaseFragment() {

    @BindView(R.id.recyclerViewNotes)
    lateinit var recyclerViewNotes: RecyclerView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: PdfViewModel
    private lateinit var userNotesAdapter: OneAdapter

    private lateinit var courseId: String
    private lateinit var isPurchased: String
    private lateinit var categoryId: String
    private lateinit var category: CategoryListItem
    private lateinit var course: CourseListItem
    lateinit var categories: SubCategoryItem
    var isPdf: Boolean = true

    companion object {
        @JvmStatic
        fun newInstance(
            subCategoryArrayList: SubCategoryItem,
            courseListItem: CourseListItem,
            isPdf: Boolean
        ) =
            DownloadPdfListFragment().apply {
                this.categories = subCategoryArrayList
                this.course = courseListItem
                this.isPdf = isPdf
            }
    }

    override fun layoutRes(): Int {
        return R.layout.layout_download_sub_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PdfViewModel::class.java)

        val linearManager = GridLayoutManager(requireContext(), 1)
        recyclerViewNotes.layoutManager = linearManager
        recyclerViewNotes.setHasFixedSize(true)
        recyclerViewNotes.setItemViewCacheSize(20)
        recyclerViewNotes.itemAnimator = null

        if(isPdf) {
            userNotesAdapter = OneAdapter(recyclerViewNotes)
                .attachItemModule(featureNotes().addEventHook(notesClickEvent()))
            fetchFiles()
        } else {
            userNotesAdapter = OneAdapter(recyclerViewNotes)
                .attachItemModule(featureVideos())
            fetchVideos()
        }
    }

    private fun fetchFiles() {
        viewModel.mAppDatabase.moduleDao().getDownloadsById(categories.subCategoryId)
            .observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<Downloads>>() {
                override fun onSuccess(courseListItems: List<Downloads>) {
                    userNotesAdapter.setItems(courseListItems)
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG onError: ", e.localizedMessage)
                }
            })
    }

    private fun fetchVideos() {
        viewModel.mAppDatabase.moduleDao().getDownloadedVideoById(categories.subCategoryId)
            .observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<DownloadedVideo>>() {
                override fun onSuccess(courseListItems: List<DownloadedVideo>) {
                    userNotesAdapter.setItems(courseListItems)
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG onError: ", e.localizedMessage)
                }
            })
    }

    private fun featureNotes(): ItemModule<Downloads> {
        return object : ItemModule<Downloads>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_pdf_list
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<Downloads>, viewBinder: ViewBinder) {
                val textViewCategoryName: AppCompatTextView =
                    viewBinder.findViewById(R.id.textViewName)
                val buttonView: MaterialButton = viewBinder.findViewById(R.id.buttonView)
                val buttonDelete: MaterialButton = viewBinder.findViewById(R.id.buttonDelete)

                textViewCategoryName.text = item.model.displayName

                buttonView.setOnClickListener {
                    viewFiles(item.model)
                }

                buttonDelete.setOnClickListener {
                    CommonView.showRoundedAlertDialog(
                        requireActivity(), "Delete PDF",
                        "Are you sure you want to delete this PDF ?", "Delete",
                        getString(R.string.text_cancel)
                    ) {
                        if (it as Boolean) {
                            viewModel.mAppDatabase.moduleDao().deletePdfFile(item.model)
                            userNotesAdapter.clear()
                            fetchFiles()
                        }
                    }
                }
            }


        }
    }

    private fun featureVideos(): ItemModule<DownloadedVideo> {
        return object : ItemModule<DownloadedVideo>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_video_list
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<DownloadedVideo>, viewBinder: ViewBinder) {
                val textViewCategoryName: AppCompatTextView =
                    viewBinder.findViewById(R.id.textViewName)
                val imageViewThumbnail: AppCompatImageView = viewBinder.findViewById(R.id.imageViewThumbnail)
                val textViewQuality: Chip = viewBinder.findViewById(R.id.textViewQuality)
                val buttonView: MaterialButton = viewBinder.findViewById(R.id.buttonView)
                val buttonDelete: MaterialButton = viewBinder.findViewById(R.id.buttonDelete)

                textViewCategoryName.text = item.model.displayName

                GlideApp.with(requireContext()).load(item.model.thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.ic_baseline_folder_24)
                    .into(imageViewThumbnail)

                textViewQuality.text = item.model.videoQuality
                buttonView.text = "Play"
                buttonView.icon = ContextCompat.getDrawable(requireContext(), R.drawable.exo_icon_circular_play)
                buttonView.setOnClickListener {
                    val intent = Intent(requireContext(), LocalVideoPlayActivity::class.java)
                    intent.putExtra("filePath", item.model.downloadLocation)
                    startActivity(intent)
                }



                buttonDelete.setOnClickListener {
                    CommonView.showRoundedAlertDialog(
                        requireActivity(), "Delete Video",
                        "Are you sure you want to delete this Video ?", "Delete",
                        getString(R.string.text_cancel)
                    ) {
                        if (it as Boolean) {
                            try {
                                var fileToBeDeleted = File(item.model.downloadLocation)
                                if(fileToBeDeleted.exists()) {
                                    fileToBeDeleted.delete()
                                }
                            } catch (e: Exception) {

                            } finally {
                                viewModel.mAppDatabase.moduleDao().deleteVideoFile(item.model)
                                userNotesAdapter.clear()
                                fetchFiles()
                            }
                        }
                    }
                }
            }


        }
    }

    private fun notesClickEvent(): ClickEventHook<Downloads> {
        return object : ClickEventHook<Downloads>() {
            override fun onClick(model: Downloads, viewBinder: ViewBinder) {
                viewFiles(model)
            }
        }
    }

    public fun viewFiles(model: Downloads) {
        val file = File(model.downloadLocation)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            galleryAddPic(file)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val uri: Uri = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName.toString() + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.type = "application/pdf"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } else {
            var intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(model.downloadLocation), "application/pdf")
            intent = Intent.createChooser(intent, "Open File")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun galleryAddPic(originalFile: File) {
        if (!originalFile.exists()) {
            return
        }
        val pathSeparator = originalFile.toString().lastIndexOf('/')
        val extensionSeparator = originalFile.toString().lastIndexOf('.')
        val filename = if (pathSeparator >= 0) originalFile.toString()
            .substring(pathSeparator + 1) else originalFile.toString()
        val extension = "pdf"
        val mimeType =
            if (extension.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension.toLowerCase(
                    Locale.ENGLISH
                )
            ) else null
        val values = ContentValues()
        values.put(MediaStore.DownloadColumns.TITLE, filename)
        values.put(MediaStore.DownloadColumns.DISPLAY_NAME, filename)
        values.put(MediaStore.DownloadColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
        if (mimeType != null && mimeType.isNotEmpty()) values.put(
            MediaStore.DownloadColumns.MIME_TYPE,
            mimeType
        )
        val externalContentUri: Uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        values.put(
            MediaStore.DownloadColumns.RELATIVE_PATH,
            "Download/" + getString(R.string.app_name)
        )
        values.put(MediaStore.DownloadColumns.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.DownloadColumns.IS_PENDING, true)
        val uri = requireContext().contentResolver.insert(externalContentUri, values)
        if (uri != null) {
            try {
                if (writeFileToStream(
                        originalFile, requireContext().contentResolver.openOutputStream(uri)!!
                    )
                ) {
                    values.put(MediaStore.MediaColumns.IS_PENDING, false)
                    requireContext().contentResolver.update(uri, values, null, null)
                    val target = Intent(Intent.ACTION_VIEW)
                    target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    target.setDataAndType(
                        uri,
                        "application/pdf"
                    ) // For now there is only type 1 (PDF).

                    val intent = Intent.createChooser(target, "Open PDF")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {

                    }
                }
            } catch (e: Exception) {
                requireContext().contentResolver.delete(uri, null, null)
            }
        }
    }

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
}