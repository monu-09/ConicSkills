package com.conicskill.app.ui.home;

import com.conicskill.app.ui.courseDetail.LiveUpComing;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsDetailFragment;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsFragment;
import com.conicskill.app.ui.help.HelpFragment;
import com.conicskill.app.ui.news.NewsCategoryFragment;
import com.conicskill.app.ui.news.NewsFragment;
import com.conicskill.app.ui.news.YoutubeVideoFragment;

import org.checkerframework.checker.units.qual.C;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract HomeFragment provideHomeFragment();

    @ContributesAndroidInjector
    abstract MockTestFragment provideMockTestFragment();

    @ContributesAndroidInjector
    abstract TestListingFragment provideTestListingFragment();

    @ContributesAndroidInjector
    abstract SupportFragment provideSupportFragment();


    @ContributesAndroidInjector
    abstract NewsFragment provideNewsFragment();

    @ContributesAndroidInjector
    abstract NewsCategoryFragment provideNewsCategoryFragment();

    @ContributesAndroidInjector
    abstract CurrentAffairsFragment provideCurrentAffairsFragment();

    @ContributesAndroidInjector
    abstract WordFragment provideWordFragment();

    @ContributesAndroidInjector
    abstract CurrentAffairsDetailFragment provideCurrentAffairsDetailFragment();

    @ContributesAndroidInjector
    abstract QuestionOfDayFragment provideQuestionOfDayFragment();

    @ContributesAndroidInjector
    abstract YoutubeVideoFragment provideYoutubeVideoFragment();

    @ContributesAndroidInjector
    abstract VideoCourseListingFragment provideVideoCourseListingFragment();

    @ContributesAndroidInjector
    abstract AboutUsFragment provideAboutUsFragment();

    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment privacyPolicyFragment();

    @ContributesAndroidInjector
    abstract RefundPolicyFragment refundPolicyFragment();

    @ContributesAndroidInjector
    abstract TermsAndConditionsFragment termsAndConditionsFragment();

    @ContributesAndroidInjector
    abstract JobNotificationFragment jobNotificationFragment();

    @ContributesAndroidInjector
    abstract JobNotificationDetailFragment jobNotificationDetailFragment();

    @ContributesAndroidInjector
    abstract ForgotPasswordFragment forgotPasswordFragment();

    @ContributesAndroidInjector
    abstract HomeVerticalFragment homeVerticalFragment();

    @ContributesAndroidInjector
    abstract LiveClassesFragment liveClassesFragment();

    @ContributesAndroidInjector
    abstract TestOptionsFragment testOptionsFragment();

    @ContributesAndroidInjector
    abstract LiveUpComing bindLiveUpComingFragment();
	
	@ContributesAndroidInjector
    abstract DownloadFragment downloadFragment();

    @ContributesAndroidInjector
    abstract DownloadCategoryFragment downloadCategoryFragment();

    @ContributesAndroidInjector
    abstract DownloadSubCategoryFragment downloadSubCategoryFragment();

    @ContributesAndroidInjector
    abstract DownloadPdfListFragment downloadPdfListFragment();

    @ContributesAndroidInjector
    abstract DownloadFileHolderFragment bindFileHolderFragment();

    @ContributesAndroidInjector
    abstract HelpFragment downloadHelpFragment();

    @ContributesAndroidInjector
    abstract QuizListFragment bindQuizFragment();
}