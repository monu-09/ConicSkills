package com.conicskill.app.ui.home

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.ui.CourseDetailActivity
import com.conicskill.app.ui.courseDetail.CourseDetailViewModel
import com.conicskill.app.ui.home.DownloadFragment.Companion.newInstance
import com.conicskill.app.ui.pdf.PdfViewModel
import com.conicskill.app.util.GlideApp
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DownloadFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: PdfViewModel
    private var oneAdapter: OneAdapter? = null

    @BindView(R.id.recyclerViewCourses)
    lateinit var recyclerViewCourses: RecyclerView

    override fun layoutRes(): Int {
        return R.layout.fragment_download
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PdfViewModel::class.java)
        val staggeredGridLayoutManager = GridLayoutManager(context, 1)
        recyclerViewCourses.layoutManager = staggeredGridLayoutManager
        recyclerViewCourses.itemAnimator = null
        oneAdapter = OneAdapter(recyclerViewCourses)
            .attachItemModule(featureItems().addEventHook(clickEventHook()))
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DownloadFragment().apply {

            }
    }

    override fun onResume() {
        super.onResume()
        viewModel.mAppDatabase.moduleDao().allCourses.observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<CourseListItem>>() {
                override fun onSuccess(courseListItems: List<CourseListItem>) {
                    oneAdapter!!.setItems(courseListItems)
                }
                override fun onError(e: Throwable) {
                    Log.e("TAG onError: ",e.localizedMessage)
                }
            })
    }

    private fun featureItems(): ItemModule<CourseListItem> {
        return object : ItemModule<CourseListItem>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_video_course
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<CourseListItem>, viewBinder: ViewBinder) {
                val textViewNewsHeading =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewNewsHeading)
                val textViewNew = viewBinder.findViewById<AppCompatTextView>(R.id.textViewNew)
                textViewNew.visibility = View.GONE
                val textViewSubDescription =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewSubDescription)
                val imageViewNews = viewBinder.findViewById<AppCompatImageView>(R.id.imageViewNews)
                val textViewTotalLecture =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewTotalLecture)
                val textViewTotalDuration =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewTotalDuration)
                val linearLayoutTotalLecture =
                    viewBinder.findViewById<LinearLayout>(R.id.linearLayoutVideoLecture)
                val textViewPrice = viewBinder.findViewById<AppCompatTextView>(R.id.textViewPrice)
                textViewPrice.visibility = View.GONE
                val textViewMRP = viewBinder.findViewById<AppCompatTextView>(R.id.textViewMRP)
                textViewMRP.visibility = View.GONE
                val textViewDiscount = viewBinder.findViewById<AppCompatTextView>(R.id.textViewDiscount)
                textViewDiscount.visibility = View.GONE
                linearLayoutTotalLecture.visibility = View.VISIBLE
                textViewNewsHeading.text = item.model.courseName
                if (item.model.description != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textViewSubDescription.text =
                            Html.fromHtml(item.model.description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        textViewSubDescription.text = Html.fromHtml(item.model.description)
                    }
                }
                GlideApp.with(context!!).load(item.model.thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageViewNews)
                textViewTotalDuration.text = item.model.duration
                textViewTotalLecture.text = item.model.lectureCount

            }
        }
    }

    private fun clickEventHook(): ClickEventHook<CourseListItem> {
        return object : ClickEventHook<CourseListItem>() {
            override fun onClick(model: CourseListItem, viewBinder: ViewBinder) {
                val intent = Intent(requireContext(), DownloadsActivity::class.java)
                intent.putExtra("course", model)
                startActivity(intent)
            }
        }
    }
}