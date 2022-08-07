package com.conicskill.app.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.di.util.ViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginSignupFragment extends BaseFragment {

    @BindView(R.id.imageViewLogoLogin)
    AppCompatImageView imageViewLogoLogin;

    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    @BindView(R.id.buttonRegister)
    Button buttonRegister;

    @Inject
    ViewModelFactory viewModelFactory;
    private LoginViewModel mLoginViewModel;

    public static LoginSignupFragment newInstance() {
        return new LoginSignupFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.login_signup_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLoginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
    }


    @OnClick(R.id.buttonLogin)
    public void clickToShowLoginFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addSharedElement(buttonLogin, getString(R.string.text_login))
                .addToBackStack("login")
                .replace(R.id.container, LoginFragment.newInstance())
                .commit();
    }

    @OnClick(R.id.buttonRegister)
    public void clickToShowRegisterFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addSharedElement(buttonRegister, getString(R.string.text_register))
                .addToBackStack("register")
                .replace(R.id.container, RegisterFragment.newInstance())
                .commit();
    }
}
