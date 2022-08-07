package com.conicskill.app.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.conicskill.app.ui.help.HelpFragment

class ViewPagerAdapter(private val fragmentsPager: ArrayList<String>, fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return fragmentsPager.size
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return HomeVerticalFragment.newInstance()
                1 -> return MockTestFragment.newInstance(null, "live")
                2 -> return MockTestFragment.newInstance("1", "live")
                3 -> return HelpFragment.newInstance()
            }
            return EmptyFragment.newInstance()
        }
    }