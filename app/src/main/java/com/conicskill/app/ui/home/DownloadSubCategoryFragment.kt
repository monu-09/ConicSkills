package com.conicskill.app.ui.home

import android.animation.Animator
import android.content.Intent
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

class DownloadSubCategoryFragment : BaseFragment() {

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
    var categories: List<SubCategoryItem> = arrayListOf()

    companion object {
        @JvmStatic
        fun newInstance(
            subCategoryArrayList: List<SubCategoryItem>,
            isPurchased: String,
            courseId: String,
            categoryId: String,
            categoryListItem: CategoryListItem,
            courseListItem: CourseListItem
        ) =
            DownloadSubCategoryFragment().apply {
                this.categories = subCategoryArrayList
                this.categoryId = categoryId
                this.courseId = courseId
                this.isPurchased = isPurchased
                this.course = courseListItem
                this.category = categoryListItem
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

        userNotesAdapter = OneAdapter(recyclerViewNotes)
            .attachItemModule(featureNotes().addEventHook(notesClickEvent()))

        viewModel.mAppDatabase.moduleDao().getSubCategoryList(categoryId).observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<SubCategoryItem>>() {
                override fun onSuccess(courseListItems: List<SubCategoryItem>) {
                    userNotesAdapter.setItems(courseListItems)
                }
                override fun onError(e: Throwable) {
                    Log.e("TAG onError: ",e.localizedMessage)
                }
            })
    }

    private fun featureNotes(): ItemModule<SubCategoryItem> {
        return object : ItemModule<SubCategoryItem>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_sub_category
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<SubCategoryItem>, viewBinder: ViewBinder) {
                val textViewCategoryName: AppCompatTextView =
                    viewBinder.findViewById(R.id.textViewName)
                val textViewVideos: AppCompatTextView = viewBinder.findViewById(R.id.textViewVideos)
                val textViewPdf: AppCompatTextView = viewBinder.findViewById(R.id.textViewPdf)

                textViewCategoryName.text = item.model.subCategory
                textViewVideos.text = "Click here to open"
                textViewPdf.visibility = View.GONE
            }


        }
    }

    private fun notesClickEvent(): ClickEventHook<SubCategoryItem> {
        return object : ClickEventHook<SubCategoryItem>() {
            override fun onClick(model: SubCategoryItem, viewBinder: ViewBinder) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.screenContainer,
                        DownloadFileHolderFragment.newInstance(model, course)
                    )
                    .commit()
            }
        }
    }
}