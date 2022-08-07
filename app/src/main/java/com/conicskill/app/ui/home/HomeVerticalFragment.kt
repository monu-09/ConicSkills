package com.conicskill.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.conicskill.app.R
import com.conicskill.app.databinding.FragmentHomeVerticalBinding
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout


class HomeVerticalFragment : Fragment() {

    private lateinit var homeVerticalBinding: FragmentHomeVerticalBinding

    val animalsArray = arrayOf(
        "Home",
        "Live Classes",
        "Study Material"
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
            inflater, R.layout.fragment_home_vertical, container,
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
            val tabOne = LayoutInflater.from(activity).inflate(R.layout.item_tab, null) as View
            val materialButton: Chip = tabOne.findViewById(R.id.chipTry)
            if (position == 0) {
                materialButton.setChipBackgroundColorResource(
                    R.color.colorPrimaryDark
                )
                materialButton.setChipStrokeColorResource(R.color.colorPrimaryDark)
                materialButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.md_white_1000
                    )
                )
            } else {
                materialButton.setChipBackgroundColorResource(
                    R.color.md_white_1000
                )
                materialButton.setChipStrokeColorResource(R.color.md_grey_600)
                materialButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.md_grey_600
                    )
                )
            }
            materialButton.text = animalsArray[position]
            materialButton.setOnClickListener {
                homeVerticalBinding.viewPager.currentItem = position
            }
            tab.customView = tabOne
        }.attach()

        homeVerticalBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view = tab.customView
                val materialButton: Chip = view!!.findViewById(R.id.chipTry)
                materialButton.setChipBackgroundColorResource(
                    R.color.colorPrimaryDark
                )
                materialButton.setChipStrokeColorResource(R.color.colorPrimaryDark)
                materialButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.md_white_1000
                    )
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view = tab.customView
                val materialButton: Chip = view!!.findViewById(R.id.chipTry)
                materialButton.setChipBackgroundColorResource(
                    R.color.md_white_1000
                )
                materialButton.setChipStrokeColorResource(R.color.md_grey_600)
                materialButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.md_grey_600
                    )
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        for (i in 0 until homeVerticalBinding.tabLayout.tabCount) {
            val tab = (homeVerticalBinding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 0,0)
            tab.requestLayout()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeVerticalFragment().apply {
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
                0 -> return HomeFragment.newInstance()
                1 -> return LiveClassesFragment.newInstance()
                2 -> return TestOptionsFragment.newInstance()
            }
            return EmptyFragment.newInstance()
        }
    }
}