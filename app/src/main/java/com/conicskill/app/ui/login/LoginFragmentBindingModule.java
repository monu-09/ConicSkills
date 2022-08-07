package com.conicskill.app.ui.login;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoginFragmentBindingModule {

    @ContributesAndroidInjector
    abstract LoginFragment provideLoginFragment();

    @ContributesAndroidInjector
    abstract LoginSignupFragment provideLoginSignupFragment();

    @ContributesAndroidInjector
    abstract ForgotPasswordFragment provideForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract RegisterFragment provideRegisterFragment();

}
