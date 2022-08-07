//package com.conicskill.app.ui.courseDetail
//
//import android.util.Log
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import com.conicskill.app.data.model.playlist.CategoryListItem
//import com.conicskill.app.data.model.videoCouses.CourseListItem
//import java.util.*
//
///**
// * HomePagerAdapter is used to set the state of the Adapter which then shown the location of the user
// * for local as well as global post
// */
//
//class CategoryPagerAdapter(fm: FragmentManager, private var categoryListItems: ArrayList<CategoryListItem?>? = null, private var course:CourseListItem) : FragmentStatePagerAdapter(
//        fm,
//        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
//) {
//
//    /**
//     * The getCount function is used to return the count of the pages that will be in the viewpager
//     */
//
//    override fun getCount(): Int = categoryListItems?.size!!
//
///**
//     * Returns the fragment to be inflated in the application based in the page number
//     */
//
//    override fun getItem(i: Int): Fragment {
//        Log.e("TAG", "getItem: $i")
//        Log.e("TAG", "getItem: "+ i + " " + categoryListItems?.get(i)?.id)
////        return VideoListingbyCourseNewFragment.newInstance(course.courseId, "1", categoryListItems?.get(i)?.id)
//    }
//
///**
//     * Return the page title for the tab layout for the page entered in the application
//     */
//
//    override fun getPageTitle(position: Int): String {
//        return categoryListItems?.get(position)?.categoryName!!
//    }
//}