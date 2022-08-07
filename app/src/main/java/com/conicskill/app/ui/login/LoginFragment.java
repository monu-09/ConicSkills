package com.conicskill.app.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequest;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequestData;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.HomeActivity;
import com.conicskill.app.util.CommonView;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.ProgressOverlay;
import com.conicskill.app.util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginFragment extends BaseFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.buttonLogin)
    AppCompatButton buttonLogin;
    @BindView(R.id.textInputUsername)
    TextInputEditText textInputUsername;
    @BindView(R.id.textLayoutUsername)
    TextInputLayout textLayoutUsername;
    @BindView(R.id.textInputPassword)
    TextInputEditText textInputPassword;
    @BindView(R.id.textLayoutPassword)
    TextInputLayout textLayoutPassword;
    @BindView(R.id.textLayoutPhone)
    TextInputLayout textLayoutPhone;
    @BindView(R.id.textInputPhone)
    TextInputEditText textInputPhone;
    private LoginViewModel mLoginViewModel;
    private ProgressDialog progressOverlay;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLoginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        observeViewModel();
        buttonLogin.setOnClickListener(v -> {
            if (validateForm()) {
                Utils.hideSoftKeyboard(getContext());
                if (Utils.isOnline(getContext())) {
                    mLoginViewModel.tinyDB.putString(Constants.USER_NAME, textInputPhone.getText().toString());
                    mLoginViewModel.tinyDB.putString(Constants.USER_PASSWORD, textInputPassword.getText().toString());

                    CandidateLoginRequestData data = new CandidateLoginRequestData();
                    data.setPassword(textInputPassword.getText().toString());
                    data.setUsername(textInputUsername.getText().toString());
                    data.setMobile(textInputPhone.getText().toString());
                    data.setNotificationToken(mLoginViewModel.tinyDB.getString(Constants.FCM_TOKEN));
                    data.setAppVersion(BuildConfig.VERSION_CODE);
                    data.setAdditionalInfo(Utils.getPhoneInfo());
                    data.setLoginVia(1);
                    data.setCompanyId(BuildConfig.COMPANY_ID);

                    CandidateLoginRequest request = new CandidateLoginRequest();
                    request.setCandidateLoginRequestData(data);
                    mLoginViewModel.callLoginApi(request);
                } else {
                    CommonView.INSTANCE.showNoInternetDialog(getActivity(), false);
                }
            }
        });
    }

    private void observeViewModel() {
        mLoginViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    progressOverlay.dismiss();
                    mLoginViewModel.tinyDB.putString(Constants.USER_NAME, "");
                    mLoginViewModel.tinyDB.putString(Constants.NAME, "");
                    mLoginViewModel.tinyDB.putString(Constants.EMAIL, "");
                    mLoginViewModel.tinyDB.putString(Constants.USER_PASSWORD, "");
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

        mLoginViewModel.checkLogin().observe(getViewLifecycleOwner(), response -> {
            if(response != null) {
                if(response.code() == HttpsURLConnection.HTTP_OK) {
                    if(response.body() != null) {
                        if(response.body().getCode() == HttpsURLConnection.HTTP_OK) {
                            mLoginViewModel.tinyDB.putString(Constants.AUTH_TOKEN, response.body().getCandidateLoginData().getAuthToken());
                            mLoginViewModel.tinyDB.putString(Constants.NAME, response.body().getCandidateLoginData().getName());
                            mLoginViewModel.tinyDB.putString(Constants.EMAIL, response.body().getCandidateLoginData().getEmail());
                            mLoginViewModel.tinyDB.putString(Constants.CANDIDATE_ID, response.body().getCandidateLoginData().getCandidateId());
                            mLoginViewModel.tinyDB.putString(Constants.PHONE_NUMBER, response.body().getCandidateLoginData().getMobile());
                            mLoginViewModel.tinyDB.putString(Constants.USER_PASSWORD, Objects.requireNonNull(textInputPassword.getText()).toString());
                            mLoginViewModel.tinyDB.putString(Constants.USER_NAME, Objects.requireNonNull(textInputPhone.getText()).toString());
                            mLoginViewModel.tinyDB.putBoolean(Constants.IS_LOGGED_IN, true);
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
                        }
                    } else {
                        Toasty.error(getContext(), "Server connection failed", Toast.LENGTH_SHORT, false).show();
                    }
                } else if(response.code() == 5002) {
                    Toasty.error(getContext(), getString(R.string.err_incorrect_use_pass), Toast.LENGTH_SHORT, false).show();
                } else {
                    Toasty.error(getContext(), getString(R.string.title_no_internet), Toast.LENGTH_SHORT, false).show();
                }
            } else {
                Toasty.error(getContext(), getString(R.string.title_no_internet), Toast.LENGTH_SHORT, false).show();
            }
        });

    }

    private boolean validateForm() {
        boolean status = true;

        textLayoutUsername.setError(null);
        textLayoutPassword.setError(null);

        /*if (textInputUsername.getText().toString().isEmpty()) {
            status = false;
            textLayoutUsername.setError(getString(R.string.error_enter_password));
        } else {
            String email = textInputUsername.getText().toString();
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");
            Matcher mat = pattern.matcher(email);

            if (!mat.matches()) {
                status = false;
                textLayoutUsername.setError(getString(R.string.error_enter_valid_username));
            }
        }*/

        if (textInputPhone.getText().toString().isEmpty()) {
            status = false;
            textLayoutPhone.setError("Please enter a mobile number");
        } else {
            textLayoutPhone.setError(null);
            if(textInputPhone.getText().toString().length() < 10) {
                status = false;
                textLayoutPhone.setError("Please enter a valid mobile number");
            }
        }

        if (textInputPassword.getText().toString().isEmpty()) {
            status = false;
            textLayoutPassword.setError(getString(R.string.error_enter_password));
        } else if (textInputPassword.getText().toString().length() < 4) {
            status = false;
            textLayoutPassword.setError(getString(R.string.error_enter_password));
        }

        return status;
    }

    private void registerWithNotificationHubs() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            if (mLoginViewModel.tinyDB.contains(Constants.FCM_TOKEN)) {
//                Intent intent = new Intent(getContext(), RegistrationIntentService.class);
//                getContext().startService(intent);
            } else {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        mLoginViewModel.tinyDB.putString(Constants.FCM_TOKEN, token);
//                        Intent intent = new Intent(getContext(), RegistrationIntentService.class);
//                        getActivity().startService(intent);
                    }
                });
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    @OnClick(R.id.buttonReset)
    public void redirectToForgotPassword() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("reset")
                .replace(R.id.container, ForgotPasswordFragment.newInstance())
                .commit();
    }

}
