package com.conicskill.app.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.ui.courseDetail.PostsPagerAdapter
import com.conicskill.app.ui.courseDetail.UpcomingFragment
import com.conicskill.app.ui.pdf.PdfViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject

class DownloadCategoryFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: PdfViewModel

    @BindView(R.id.tabLayout)
    lateinit var tabLayout: TabLayout

    @BindView(R.id.pager)
    lateinit var pager: ViewPager2

    @BindView(R.id.linearLayoutLoading)
    lateinit var linearLayoutLoading: RelativeLayout

    private lateinit var pagerAdapter: PostPagerDownloadAdapter
    private var categoryListItems = ArrayList<CategoryListItem>()
    private lateinit var course: CourseListItem

    companion object {
        @JvmStatic
        fun newInstance(
            courseListItem: CourseListItem
        ) =
            DownloadCategoryFragment().apply {
                this.course = courseListItem
            }
    }

    override fun layoutRes(): Int {
        return R.layout.layout_download_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PdfViewModel::class.java)
        viewModel.mAppDatabase.moduleDao().getCategoryList(course.courseId).observeOn(Schedulers.io())
            .subscribe(object: DisposableSingleObserver<List<CategoryListItem>>() {
                override fun onSuccess(t: List<CategoryListItem>) {
                    categoryListItems = t as ArrayList<CategoryListItem>
                    requireActivity().runOnUiThread {
                        setUpViewPager()
                    }
                }

                override fun onError(e: Throwable) {

                }

            })
    }

    private fun setUpViewPager() {
        linearLayoutLoading.visibility = View.GONE
        pager.visibility = View.VISIBLE
        tabLayout.visibility = View.VISIBLE
        pagerAdapter = PostPagerDownloadAdapter(this, categoryListItems, course)
        pager.adapter = pagerAdapter
        TabLayoutMediator(
            tabLayout, pager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setText(
                categoryListItems.get(position).getCategoryName()
            )
        }.attach()
        tabLayout.tabRippleColor = null
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(0, 10, 20, 10)
            tab.requestLayout()
        }
    }
}