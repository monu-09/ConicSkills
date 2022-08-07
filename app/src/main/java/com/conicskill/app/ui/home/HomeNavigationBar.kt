package com.conicskill.app.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.conicskill.app.R
import com.conicskill.app.databinding.FragmentNavigationBarBinding
import com.conicskill.app.ui.help.HelpFragment

class HomeNavigationBar: Fragment() {
    private var currentSelectedItem = 0
    private lateinit var homeNavigationBar: FragmentNavigationBarBinding

    val animalsArray = arrayOf(
        "",
        "",
        "",
        "",
        ""
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
        homeNavigationBar = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation_bar, container,
            false
        )
        return homeNavigationBar.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        homeNavigationBar.viewPager.adapter = adapter
        homeNavigationBar.viewPager.offscreenPageLimit = 5
        homeNavigationBar.viewPager.isUserInputEnabled = false

        homeNavigationBar.bubbleTabBar.addBubbleListener { id ->
            when (id) {
                R.id.home -> homeNavigationBar.viewPager.currentItem = 0
                R.id.testSeries -> homeNavigationBar.viewPager.currentItem = 1
                R.id.quizzes -> homeNavigationBar.viewPager.currentItem = 2
                R.id.downloads -> homeNavigationBar.viewPager.currentItem = 3
                R.id.help -> homeNavigationBar.viewPager.currentItem = 4
            }
        }

        homeNavigationBar.viewPager.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentSelectedItem = position
                    homeNavigationBar.bubbleTabBar.setSelected(position)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            if(currentSelectedItem == 0) {
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.testSeries, false)
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
            } else if (currentSelectedItem ==1) {
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.videoClasses, true)
            } else if (currentSelectedItem == 2) {
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.ourTest, true)
            } else if (currentSelectedItem == 3) {
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.liveTest, true)
            } else if (currentSelectedItem == 4) {
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.liveTest, true)
            }
        }, 100);

        Handler(Looper.getMainLooper()).postDelayed({
            if(currentSelectedItem == 0) {
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.videoClasses, false)
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
            } else if (currentSelectedItem ==1) {
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.testSeries, true)
            } else if (currentSelectedItem == 2) {
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.quizzes, true)
            } else if (currentSelectedItem == 3) {
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.home, false)
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.downloads, true)
            } else if (currentSelectedItem == 4) {
                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.help, false)
//                homeNavigationBar.bubbleTabBar.setSelectedWithId(R.id.liveTest, true)
            }
        }, 1200);
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeNavigationBar().apply {
                arguments = Bundle().apply {

                }
            }
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return animalsArray.size
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return HomeVerticalFragment.newInstance()
                1 -> return MockTestFragment.newInstance(null, "class")
                2 -> return QuizListFragment.newInstance("100", "quiz")
                3 -> return DownloadFragment.newInstance()
                4 -> return HelpFragment.newInstance()
            }
            return EmptyFragment.newInstance()
        }
    }
}