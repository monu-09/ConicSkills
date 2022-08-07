package com.conicskill.app.ui.courseDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.conicskill.app.R
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.databinding.FragmentLiveUpcomingBinding
import com.conicskill.app.ui.home.*
import com.google.android.material.tabs.TabLayoutMediator

class LiveUpComing : Fragment() {
    private lateinit var homeVerticalBinding: FragmentLiveUpcomingBinding

    val animalsArray = arrayOf(
        "Live",
        "Recorded"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeVerticalBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_live_upcoming, container,
            false
        )
        return homeVerticalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        homeVerticalBinding.viewPager.adapter = adapter
        homeVerticalBinding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(
            homeVerticalBinding.tabLayout,
            homeVerticalBinding.viewPager
        ) { tab, position ->
            tab.text = animalsArray[position]
        }.attach()
        for (i in 0 until homeVerticalBinding.tabLayout.tabCount) {
            val tab = (homeVerticalBinding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 10, 20, 10)
            tab.requestLayout()
        }
    }

    companion object {
        lateinit var courseListItem: CourseListItem
        @JvmStatic
        fun newInstance(course: CourseListItem) =
            LiveUpComing().apply {
                courseListItem = course
            }
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return animalsArray.size
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return UpcomingFragment.newInstance(courseListItem, "live")
                1 -> return RecordedFragment.newInstance(courseListItem, "Upcoming")
            }
            return EmptyFragment.newInstance()
        }
    }
}