package com.conicskill.app.ui.courseDetail.interfaces;

import com.conicskill.app.data.model.playlist.CategoryListItem;

public interface CourseClickListener {
    /**
     * This is for getting the click data for redirection and other purposes
     */
    void onPlayListItemClicked(CategoryListItem item);
}
