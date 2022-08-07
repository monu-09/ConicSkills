package com.conicskill.app.ui.courseDetail;

import com.conicskill.app.ui.pdf.PdfFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CourseDetailBindingModule {

    @ContributesAndroidInjector
    abstract CourseDetailFragment provideCourseDetailFragment();

    @ContributesAndroidInjector
    abstract UpcomingFragment provideUpComingFragment();

    @ContributesAndroidInjector
    abstract RecordedFragment provideRecordedFragment();

    @ContributesAndroidInjector
    abstract SubCategoryFragment provideSubCategoryFragment();

    @ContributesAndroidInjector
    abstract VideoListingbyCourseNewFragment provideVideoListingFragment();

    @ContributesAndroidInjector
    abstract PdfFragment bindPdfFragment();

    @ContributesAndroidInjector
    abstract LiveUpComing bindLiveUpComingFragment();
}