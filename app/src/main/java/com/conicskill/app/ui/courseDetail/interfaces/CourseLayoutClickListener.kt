package com.conicskill.app.ui.courseDetail.interfaces

import com.conicskill.app.data.model.home.LayoutItem

interface CourseLayoutClickListener {

    fun layoutClick(layoutItem: LayoutItem, position: Int)
}