package com.conicskill.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.databinding.FragmentDownloadFileHolderBinding
import com.conicskill.app.databinding.FragmentHomeVerticalBinding
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DownloadFileHolderFragment : BaseFragment() {

    private lateinit var binding: FragmentDownloadFileHolderBinding

    val animalsArray = arrayOf(
        "Videos",
        "Notes"
    )

    private lateinit var course: CourseListItem
    lateinit var categories: SubCategoryItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_download_file_holder, container,
            false
        )
        return binding.root
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_download_file_holder
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
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
                binding.viewPager.currentItem = position
            }
            tab.customView = tabOne
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object :
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
        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 0,0)
            tab.requestLayout()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(subCategoryArrayList: SubCategoryItem,
                        courseListItem: CourseListItem) =
            DownloadFileHolderFragment().apply {
                    this.categories = subCategoryArrayList
                    this.course = courseListItem
            }
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return animalsArray.size
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return DownloadPdfListFragment.newInstance(categories, course, false)
                1 -> return DownloadPdfListFragment.newInstance(categories, course, true)
            }
            return EmptyFragment.newInstance()
        }
    }
}