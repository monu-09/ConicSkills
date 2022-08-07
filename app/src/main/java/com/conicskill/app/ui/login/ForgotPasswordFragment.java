package com.conicskill.app.ui.login;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordData;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordRequest;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordResponse;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import com.conicskill.app.BuildConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textInputUsername)
    TextInputEditText textInputUsername;
    @BindView(R.id.textLayoutUsername)
    TextInputLayout textLayoutUsername;
    private LoginViewModel mLoginViewModel;
    private ProgressDialog progressOverlay;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLoginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        textInputUsername.setOnFocusChangeListener((l, hasfocus) -> {
            if (hasfocus) {
                textLayoutUsername.setError(null);
            }
        });

        observeViewModel();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.buttonReset)
    public void callResetAPI() {
        if (validateForm()) {
            // call the forgot password for the same
            String email = textInputUsername.getText().toString().trim();
            ForgotPasswordData forgotPasswordData = new ForgotPasswordData(BuildConfig.COMPANY_ID, email);
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(forgotPasswordData);
            mLoginViewModel.callForgotPassword(forgotPasswordRequest);
        }
    }

    private void observeViewModel() {
        mLoginViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    progressOverlay.dismiss();
                    Toasty.error(getContext(), getString(R.string.title_no_internet), Toast.LENGTH_SHORT, false).show();
                }
            }
        });

        mLoginViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(getActivity(), "");
            } else {
                progressOverlay.dismiss();
            }
        });

        mLoginViewModel.observeForgotPassword().observe(getViewLifecycleOwner(), forgotApiResponse -> {
            if (forgotApiResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (forgotApiResponse.body() != null && forgotApiResponse.body().component1() == HttpsURLConnection.HTTP_OK) {
                    Toasty.success(getContext(), "An email have been sent to your email with reset instructions.",
                            Toasty.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    Toasty.error(getContext(), getString(R.string.err_something_went_wrong), Toasty.LENGTH_LONG, false).show();
                }
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                Toasty.error(getContext(), "Failed to send reset email. Please try again", Toasty.LENGTH_LONG, false).show();
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_CONFLICT) {
                Toasty.error(getContext(), getString(R.string.err_something_went_wrong), Toasty.LENGTH_LONG, false).show();
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_NOT_AUTHORITATIVE) {
                Toasty.error(getContext(), getString(R.string.err_something_went_wrong), Toasty.LENGTH_LONG, false).show();
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_NO_CONTENT) {
                Toasty.error(getContext(), "User with this email does not exist. Please check your email", Toasty.LENGTH_LONG, false).show();
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                Toasty.error(getContext(), getString(R.string.err_something_went_wrong), Toasty.LENGTH_LONG, false).show();
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_CLIENT_TIMEOUT) {
                Toasty.error(getContext(), getString(R.string.err_something_went_wrong), Toasty.LENGTH_LONG, false).show();
            } else if (forgotApiResponse.code() == HttpsURLConnection.HTTP_INTERNAL_ERROR) {
                Toasty.error(getContext(), "Server Error. Contact customer support", Toasty.LENGTH_LONG, false).show();
            } else {
                Toasty.error(getContext(), getString(R.string.err_something_went_wrong), Toasty.LENGTH_LONG, false).show();
            }
        });
    }

    private boolean validateForm() {
        boolean status = true;

        textLayoutUsername.setError(null);

        if (textInputUsername.getText().toString().trim().isEmpty()) {
            status = false;
            textLayoutUsername.setError("Please enter an email");
        } else {
            String email = textInputUsername.getText().toString().trim();
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email);

            if (!mat.matches()) {
                status = false;
                textLayoutUsername.setError("Please enter a valid email");
            }
        }

        return status;
    }
}
