package com.conicskill.app.di.module;

import com.conicskill.app.ui.CADetailActivity;
import com.conicskill.app.ui.CourseDetailActivity;
import com.conicskill.app.ui.ExamActivity;
import com.conicskill.app.ui.GDriveActivity;
import com.conicskill.app.ui.HomeActivity;
import com.conicskill.app.ui.LoginActivity;
import com.conicskill.app.ui.PaymentActivity;
import com.conicskill.app.ui.SplashActivity;
import com.conicskill.app.ui.courseDetail.CourseDetailBindingModule;
import com.conicskill.app.ui.exam.ExamFragmentBindingModule;
import com.conicskill.app.ui.home.DownloadsActivity;
import com.conicskill.app.ui.home.MainFragmentBindingModule;
import com.conicskill.app.ui.login.LoginFragmentBindingModule;
import com.conicskill.app.ui.pdf.PDFFragmentBindingModule;
import com.conicskill.app.ui.pdf.PlayerActivity;
import com.conicskill.app.ui.pdf.YoutubeVideoActivity;
import com.conicskill.app.ui.splash.SplashFragmentBindingModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {SplashFragmentBindingModule.class})
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = {LoginFragmentBindingModule.class})
    abstract LoginActivity bindLoginSignupActivity();

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract HomeActivity bindHomeActivity();

    @ContributesAndroidInjector(modules = {ExamFragmentBindingModule.class})
    abstract ExamActivity bindExamActivity();


    @ContributesAndroidInjector(modules = {PDFFragmentBindingModule.class})
    abstract YoutubeVideoActivity bindYoutubeVideoActivity();

    @ContributesAndroidInjector(modules = {PDFFragmentBindingModule.class})
    abstract PlayerActivity bindPlayerActivity();

    @ContributesAndroidInjector
    abstract PaymentActivity bindPaymentActivity();

    @ContributesAndroidInjector(modules = {CourseDetailBindingModule.class})
    abstract CourseDetailActivity bindCourseDetailActivity();

    @ContributesAndroidInjector
    abstract CADetailActivity bindCADetailActivity();

    @ContributesAndroidInjector
    abstract GDriveActivity bindGDriveActivity();

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract DownloadsActivity bindDownloadsActivity();


}
