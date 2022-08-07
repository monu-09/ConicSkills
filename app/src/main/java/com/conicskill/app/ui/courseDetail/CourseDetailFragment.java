package com.conicskill.app.ui.courseDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.payments.ItemListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.PaymentActivity;
import com.conicskill.app.util.GlideApp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;

public class CourseDetailFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private CourseDetailViewModel mCourseViewModel;

    @BindView(R.id.webViewDescription)
    WebView webViewDescription;

    @BindView(R.id.imageViewCourseBanner)
    AppCompatImageView imageViewCourseBanner;

    @BindView(R.id.textViewCourseName)
    AppCompatTextView textViewCourseName;

    @BindView(R.id.chipLectures)
    Chip chipLectures;

    @BindView(R.id.chipDuration)
    Chip chipDuration;

    @BindView(R.id.buttonBuyNow)
    MaterialButton buttonBuyNow;

    @BindView(R.id.buttonViewDemo)
    MaterialButton buttonViewDemo;

    private CourseListItem course;

    public static CourseDetailFragment newInstance(CourseListItem courseListItem) {
        CourseDetailFragment courseDetailFragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("course", courseListItem);
        courseDetailFragment.setArguments(args);
        return courseDetailFragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.course_detail_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCourseViewModel = new ViewModelProvider(this, viewModelFactory).get(CourseDetailViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.getSerializable("course") != null) {
                course = (CourseListItem) bundle.getSerializable("course");
            }
        }

        webViewDescription.getSettings().setJavaScriptEnabled(true);
        webViewDescription.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));
        if (course.getDescription() != null && !course.getDescription().isEmpty()) {
            String head1 = "<head><style>@font-face {font-family: 'arial';src: url('file:///android_asset/fonts/DIN-Medium.otf');}body {font-family: 'verdana';}</style></head>";
            String text="<html>"+head1
                    + "<body >" + course.getDescription()
                    + "</body></html>";
            webViewDescription.loadDataWithBaseURL("http://127.0.0.1/", text,
                    "text/html", "UTF-8", null);
        } else {
            webViewDescription.loadDataWithBaseURL("http://127.0.0.1/", "No description found",
                    "text/html", "UTF-8", null);
        }

        textViewCourseName.setText(course.getCourseName());
        chipLectures.setText(" " + course.getLectureCount() + " Lectures");
        chipDuration.setText(" " + course.getDuration() + " Mins per Lecture");

        GlideApp.with(getActivity().getApplicationContext())
                .load(course.getThumbnail())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageViewCourseBanner);

        if(course.getPrice().equalsIgnoreCase("0")) {
            buttonBuyNow.setEnabled(false);
        }

        buttonBuyNow.setOnClickListener(l->{
            purchaseCourse();
        });

        buttonViewDemo.setOnClickListener(l->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("TestListFragment")
                    .replace(R.id.screenContainer, VideoListingbyCourseNewFragment.newInstance(course.getCourseId(), course.getPurchased(),
                            null, null, course, null, null, true))
                    .commit();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void purchaseCourse() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        ItemListItem itemListItem = new ItemListItem();
        itemListItem.setCourseId(course.getCourseId());
        itemListItem.setItemType("course");

        ArrayList<ItemListItem> paymentArrayList = new ArrayList<>();
        paymentArrayList.add(itemListItem);
        intent.putExtra("payments", paymentArrayList);
        intent.putExtra("price", course.getPrice());
        intent.putExtra("name", course.getCourseName() + " || " + course.getCourseId());
        startActivity(intent);
    }

}
