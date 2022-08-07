package com.conicskill.app.ui.pdf;

import org.checkerframework.checker.units.qual.C;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PDFFragmentBindingModule {

    @ContributesAndroidInjector
    abstract PdfFragment providePdfFragment();

    @ContributesAndroidInjector
    abstract ChatFragment provideChatFragment();

    @ContributesAndroidInjector
    abstract DownloadVideoFragment provideDownloadVideoFragment();

}