package com.conicskill.app.ui;

import android.os.Bundle;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.ui.courseDetail.CourseDetailFragment;
import com.conicskill.app.ui.courseDetail.LiveUpComing;
import com.conicskill.app.ui.courseDetail.UpcomingFragment;

import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;

public class CourseDetailActivity extends BaseActivity {

    @BindView(R.id.headerText)
    AppCompatTextView headerText;

    private CourseListItem courseListItem;

    @Override
    protected int layoutRes() {
        return R.layout.course_detail_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseListItem = (CourseListItem) getIntent().getExtras().getSerializable("course");
        if (savedInstanceState == null) {
            if(courseListItem.getPrice().equalsIgnoreCase("0") || courseListItem.getPurchased().equalsIgnoreCase("1")) {
                headerText.setText(courseListItem.getCourseName());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.screenContainer, LiveUpComing.newInstance(courseListItem))
                        .commitNow();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.screenContainer, CourseDetailFragment.newInstance(courseListItem))
                        .commitNow();
            }
        }
    }
}
