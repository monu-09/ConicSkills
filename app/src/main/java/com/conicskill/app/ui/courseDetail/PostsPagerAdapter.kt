package com.conicskill.app.ui.courseDetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import java.util.ArrayList

class PostsPagerAdapter(
        fragment: Fragment,
        private var categoryListItems: ArrayList<CategoryListItem?>? = null, private var course: CourseListItem
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return categoryListItems?.size!!
    }

    override fun createFragment(position: Int): Fragment {
        return SubCategoryFragment.newInstance(categoryListItems?.get(position)?.subCategoriesArrayList!!, "1",
                course.courseId,
                categoryListItems?.get(position)?.id!!,categoryListItems?.get(position)!!, course)
    }
}