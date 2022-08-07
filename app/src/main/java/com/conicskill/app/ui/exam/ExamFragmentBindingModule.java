package com.conicskill.app.ui.exam;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ExamFragmentBindingModule {

    @ContributesAndroidInjector
    abstract ExamFragment provideExamFragment();

    @ContributesAndroidInjector
    abstract ExamSwipeableFragment provideExamSwipeableFragment();

    @ContributesAndroidInjector
    abstract ResultFragment provideResultFragment();

    @ContributesAndroidInjector
    abstract SolutionsFragment provideSolutionFragment();

}