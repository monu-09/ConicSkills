package com.conicskill.app.ui.courseDetail

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.notes.NoteData
import com.conicskill.app.data.model.notes.NotesListItem
import com.conicskill.app.data.model.notes.NotesListRequest
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.ui.GDriveActivity
import com.conicskill.app.util.Constants
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class SubCategoryFragment : BaseFragment() {

    @BindView(R.id.recyclerViewSubCategory)
    lateinit var recyclerViewSubCategory: RecyclerView

    @BindView(R.id.recyclerViewNotes)
    lateinit var recyclerViewNotes: RecyclerView
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var mCourseViewModel: CourseDetailViewModel

    private lateinit var userPostAdapter: OneAdapter
    private lateinit var userNotesAdapter: OneAdapter
    private lateinit var courseId: String
    private lateinit var isPurchased: String
    private lateinit var categoryId: String
    private lateinit var category: CategoryListItem
    private lateinit var course: CourseListItem

    var categories: List<SubCategoryItem> = arrayListOf()
    var notesList: List<NotesListItem> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCourseViewModel = ViewModelProvider(this, viewModelFactory).get(
            CourseDetailViewModel::class.java
        )
        val staggeredGridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerViewSubCategory.layoutManager = staggeredGridLayoutManager
        recyclerViewSubCategory.setHasFixedSize(true)
        recyclerViewSubCategory.setItemViewCacheSize(20)
        recyclerViewSubCategory.itemAnimator = null

        userPostAdapter = OneAdapter(recyclerViewSubCategory)
            .attachItemModule(featureItems().addEventHook(clickEventHook()))
        userPostAdapter.setItems(categories)

        val linearManager = GridLayoutManager(requireContext(), 1)
        recyclerViewNotes.layoutManager = linearManager
        recyclerViewNotes.setHasFixedSize(true)
        recyclerViewNotes.setItemViewCacheSize(20)
        recyclerViewNotes.itemAnimator = null

        userNotesAdapter = OneAdapter(recyclerViewNotes)
            .attachItemModule(featureNotes().addEventHook(notesClickEvent()))
//        observeViewModel()
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_sub_category
    }

    override fun onResume() {
        super.onResume()
        if(notesList.isEmpty()) {
            val notesData = NoteData(categoryId.toInt(), courseId)
            val notesListRequest = NotesListRequest(
                notesData, mCourseViewModel.tinyDB?.getString(
                    Constants.AUTH_TOKEN
                )
            )
            mCourseViewModel.callFetchCourseNotes(notesListRequest)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            subCategoryArrayList: List<SubCategoryItem>, isPurchased: String,
            courseId: String, categoryId: String, categoryListItem: CategoryListItem, courseListItem: CourseListItem
        ) =
            SubCategoryFragment().apply {
                this.categories = subCategoryArrayList
                this.categoryId = categoryId
                this.courseId = courseId
                this.isPurchased = isPurchased
                this.course = courseListItem
                this.category = categoryListItem
            }
    }

    private fun featureItems(): ItemModule<SubCategoryItem> {
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
                textViewVideos.text = item.model.videoCount + " videos"

                if (item.model.pdfCount != null) {
                    textViewPdf.visibility = View.VISIBLE
                    textViewPdf.text = item.model.pdfCount + " PDFs "
                } else {
                    textViewPdf.visibility = View.GONE
                }
            }


        }
    }

    private fun clickEventHook(): ClickEventHook<SubCategoryItem> {
        return object : ClickEventHook<SubCategoryItem>() {
            override fun onClick(model: SubCategoryItem, viewBinder: ViewBinder) {
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack("TestListFragment")
                    .replace(
                        R.id.screenContainer,
                        VideoListingbyCourseNewFragment.newInstance(
                            courseId,
                            "1",
                            categoryId,
                            model.subCategoryId,
                            course,
                            category,
                            model,
                            false
                        )
                    )
                    .commit()
            }
        }
    }

    private fun featureNotes(): ItemModule<NotesListItem> {
        return object : ItemModule<NotesListItem>() {
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

            override fun onBind(item: Item<NotesListItem>, viewBinder: ViewBinder) {
                val textViewCategoryName: AppCompatTextView =
                    viewBinder.findViewById(R.id.textViewName)
                val textViewVideos: AppCompatTextView = viewBinder.findViewById(R.id.textViewVideos)
                val textViewPdf: AppCompatTextView = viewBinder.findViewById(R.id.textViewPdf)

                textViewCategoryName.text = item.model.title
                textViewVideos.text = "Click here to open"

                /* if (item.model.pdfCount != null) {
                     textViewPdf.visibility = View.VISIBLE
                     textViewPdf.text = item.model.pdfCount + " PDFs "
                 } else {*/
                textViewPdf.visibility = View.GONE
                /*}*/
            }


        }
    }

    private fun notesClickEvent(): ClickEventHook<NotesListItem> {
        return object : ClickEventHook<NotesListItem>() {
            override fun onClick(model: NotesListItem, viewBinder: ViewBinder) {
                val intent = Intent(requireContext(), GDriveActivity::class.java)
                intent.putExtra("url", model.url)
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        mCourseViewModel.notesListResponse?.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.body()?.code == HttpsURLConnection.HTTP_OK) {
                    notesList = it.body()?.data?.notesList!!
                    userNotesAdapter.setItems(notesList)
                }
            }
        })
    }
}