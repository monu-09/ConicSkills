package com.conicskill.app.ui.splash;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SplashFragmentBindingModule {

    @ContributesAndroidInjector
    abstract SplashFragment provideSplashFragment();

}
