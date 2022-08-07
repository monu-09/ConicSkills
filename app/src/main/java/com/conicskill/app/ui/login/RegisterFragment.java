package com.conicskill.app.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequest;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequestData;
import com.conicskill.app.data.model.candidateRegister.RegisterRequest;
import com.conicskill.app.data.model.candidateRegister.RegisterRequestData;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.HomeActivity;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.ProgressOverlay;
import com.conicskill.app.util.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class RegisterFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.buttonRegister)
    AppCompatButton buttonRegister;
    @BindView(R.id.textLayoutPhone)
    TextInputLayout textLayoutPhone;
    @BindView(R.id.textInputPhone)
    TextInputEditText textInputPhone;
    @BindView(R.id.textLayoutFirstname)
    TextInputLayout textLayoutFirstname;
    @BindView(R.id.textInputFirstname)
    TextInputEditText textInputFirstname;

    @BindView(R.id.textInputUsername)
    TextInputEditText textInputUsername;

    @BindView(R.id.textLayoutUsername)
    TextInputLayout textLayoutUsername;

    @BindView(R.id.textLayoutEmail)
    TextInputLayout textLayoutEmail;
    @BindView(R.id.textInputEmail)
    TextInputEditText textInputEmail;
    @BindView(R.id.textLayoutPassword)
    TextInputLayout textLayoutPassword;
    @BindView(R.id.textInputPassword)
    TextInputEditText textInputPassword;
    private String phoneNumber;
    private String countryCodeWithoutPlus;
    private String fullNumber;
    private ProgressDialog progressOverlay;
    private LoginViewModel mLoginViewModel;
    private AlertDialog dialog;
    private boolean logginClicked = false;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.register_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLoginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        textInputPhone.setOnClickListener((viewPhone) -> {

        });

        textInputPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {

            }
        });

        textInputFirstname.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textLayoutFirstname.setError(null);
            }
        });

        textInputEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textLayoutEmail.setError(null);
            }
        });

        textInputPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textLayoutPassword.setError(null);
            }
        });


        observeViewModel();
//        messageDialog(R.drawable.ic_info_outline_white_24dp, "Congratulations", "Registration successful");
    }

    private void observeViewModel() {
        mLoginViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(getActivity(), "Registering user.");
            } else {
                progressOverlay.dismiss();
            }
        });

        mLoginViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error) {
                    if(logginClicked) {
                        messageDialog(R.drawable.ic_info_outline_white_24dp, "Congratulations",
                                "Registration successful");
                    } else {
                        progressOverlay.dismiss();
                        Toasty.error(getContext(), "Failed to register. Please try again.", Toasty.LENGTH_LONG, false).show();
                    }
                }
            }
        });

        mLoginViewModel.observeRegisterAPI().observe(getViewLifecycleOwner(), registerResponseResponse -> {
            if(registerResponseResponse != null) {
                if(registerResponseResponse.code()== HttpsURLConnection.HTTP_OK) {
                    if(registerResponseResponse.body().getData().getCandidateId() != null &&
                            !registerResponseResponse.body().getData().getCandidateId().isEmpty()) {
//                        Toasty.info(getContext(),registerResponseResponse.body().getMessage(), Toasty.LENGTH_LONG).show();
                        messageDialog(R.drawable.ic_info_outline_white_24dp, "Congratulations",
                                "Registration successful");
                    } else {
                        Toasty.error(getContext(),"Registration failed, Please fill all the mandatory fields", Toasty.LENGTH_LONG).show();
                    }
                } else if (registerResponseResponse.code() == 5002) {
                    if(registerResponseResponse.body().getData().getCandidateId() != null &&
                            !registerResponseResponse.body().getData().getCandidateId().isEmpty()) {
                        Toasty.info(getContext(),registerResponseResponse.body().getMessage(), Toasty.LENGTH_LONG).show();
                        getBaseActivity().onBackPressed();
                    } else {
                        Toasty.error(getContext(),registerResponseResponse.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(getContext(),registerResponseResponse.body().getMessage(), Toasty.LENGTH_LONG).show();
                }
            } else {
                Toasty.error(getContext(),"Registration failed, Please fill all the mandatory fields", Toasty.LENGTH_LONG).show();
            }
        });

        mLoginViewModel.checkLogin().observe(getViewLifecycleOwner(), response -> {
            if(response != null) {
                if(response.code() == HttpsURLConnection.HTTP_OK) {
                    if(response.body() != null) {
                        if(response.body().getCode() == HttpsURLConnection.HTTP_OK) {
                            if(dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
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

        /*mLoginViewModel.registerUser().observe(this, registerResponse -> {
            if (registerResponse != null) {
                if (registerResponse.code() == HttpsURLConnection.HTTP_OK) {
                    if (registerResponse.body() != null) {
                        if (registerResponse.body().getRegister() != null) {
                            String email = registerResponse.body().getRegister().getEmail();
                            messageDialog(R.drawable.ic_circle_outline, getString(R.string.text_congratulations),
                                    getString(R.string.text_registration_success));
                        } else {
                            messageDialog(R.drawable.ic_warning_black_24dp, "Account already registered",
                                    "A user with the same email or phone is already registered with us." +
                                            "\n" +
                                            "\n" +
                                            "Kindly login using the same email.");
                        }
                    }
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                    Toasty.error(getContext(), "Bad request", Toasty.LENGTH_LONG).show();
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_CONFLICT) {
                    messageDialog(R.drawable.ic_warning_black_24dp, "Account already registered",
                            "A user with the same email or phone is already registered with us." +
                                    "\n" +
                                    "\n" +
                                    "Kindly login using the same email.");
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_NOT_AUTHORITATIVE) {
                    Toasty.error(getContext(), "Not Authoritative", Toasty.LENGTH_LONG, false).show();
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_NO_CONTENT) {
                    Toasty.error(getContext(), "No Content", Toasty.LENGTH_LONG, false).show();
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    Toasty.error(getContext(), "Unauthorized", Toasty.LENGTH_LONG, false).show();
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_CLIENT_TIMEOUT) {
                    Toasty.error(getContext(), "Timeout. Please try again", Toasty.LENGTH_LONG, false).show();
                } else if (registerResponse.code() == HttpsURLConnection.HTTP_INTERNAL_ERROR) {
                    Toasty.error(getContext(), "Server Internal Error. Please try again", Toasty.LENGTH_LONG, false).show();
                } else {
                    Toasty.error(getContext(), "Something went wrong. Please try again", Toasty.LENGTH_LONG, false).show();
                }
            }
        });*/
    }


    @OnClick(R.id.buttonRegister)
    public void completeRegistration() {
        if (validateRegistration()) {
            Utils.hideSoftKeyboard(getActivity());
            RegisterRequestData registerRequestData = new RegisterRequestData();
            registerRequestData.setCompanyId(BuildConfig.COMPANY_ID);
            registerRequestData.setEmail(textInputEmail.getText().toString());
            registerRequestData.setUsername(textInputPhone.getText().toString());
            registerRequestData.setName(textInputFirstname.getText().toString());
            registerRequestData.setMobile(textInputPhone.getText().toString());
            registerRequestData.setPassword(textInputPassword.getText().toString());

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setData(registerRequestData);

            mLoginViewModel.callRegisterCandidate(registerRequest);
        }
    }

    private boolean validateRegistration() {
        boolean success = true;
        if (textInputFirstname.getText().toString().isEmpty()) {
            success = false;
            textLayoutFirstname.setError("Please enter your name");
        } else {
            textLayoutFirstname.setError(null);
            if (textInputFirstname.getText().toString().length() < 3) {
                success = false;
                textLayoutFirstname.setError("Name cannot be less than 3 letters");

            }
        }

        /*if (textInputUsername.getText().toString().isEmpty()) {
            success = false;
            textLayoutUsername.setError("Please enter your username");
        } else {
            textLayoutUsername.setError(null);
            if (textInputFirstname.getText().toString().length() < 3) {
                success = false;
                textLayoutUsername.setError("Name cannot be less than 3 letters");

            }
        }*/

        if (textInputEmail.getText().toString().isEmpty()) {
            success = false;
            textLayoutEmail.setError("Please enter the email");
        } else {
            textLayoutEmail.setError(null);
            String email = textInputEmail.getText().toString();
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email);

            if (!mat.matches()) {
                success = false;
                textLayoutEmail.setError("Please enter a valid email to register");
            }
        }

        /*if (textInputUsername.getText().toString().isEmpty()) {
            success = false;
            textLayoutUsername.setError(getString(R.string.error_enter_password));
        } else {
            String email = textInputUsername.getText().toString();
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");
            Matcher mat = pattern.matcher(email);

            if (!mat.matches()) {
                success = false;
                textLayoutUsername.setError(getString(R.string.error_enter_valid_username));
            }
        }*/

        if (textInputPassword.getText().toString().isEmpty()) {
            success = false;
            textLayoutPassword.setError(getString(R.string.error_enter_password));
        } else if (textInputPassword.getText().toString().length() < 4) {
            success = false;
            textLayoutPassword.setError(getString(R.string.error_enter_password));
        }

        if (textInputPhone.getText().toString().isEmpty()) {
            success = false;
            textLayoutPhone.setError("Please enter a mobile number");
        } else {
            textLayoutPhone.setError(null);
            if(textInputPhone.getText().toString().length() < 10) {
                success= false;
                textLayoutPhone.setError("Please enter a valid mobile number");
            }
        }

        return success;
    }

    private void messageDialog(@DrawableRes int icon, String title, String message) {
        View dialoglayout = getLayoutInflater().inflate(R.layout.layout_dialog_register, null);
        LottieAnimationView animationView = dialoglayout.findViewById(R.id.animationViewRegister);
        assert animationView != null;
        animationView.setAnimation(R.raw.success);
        animationView.playAnimation();

        TextView registerText = dialoglayout.findViewById(R.id.registerText);
        assert registerText != null;
        registerText.setText(message);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(),
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton)
//                .setTitle(title)
                .setView(dialoglayout)
                .setIcon(icon)
                .setCancelable(false)
//                .setPositiveButton(getString(R.string.text_login), ((dialog, which) -> {
                .setPositiveButton("", ((dialog, which) -> {

                }));

        dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(()->{
            mLoginViewModel.tinyDB.putString(Constants.USER_NAME, textInputPhone.getText().toString());
            mLoginViewModel.tinyDB.putString(Constants.USER_PASSWORD, textInputPassword.getText().toString());

            CandidateLoginRequestData data = new CandidateLoginRequestData();
            data.setPassword(textInputPassword.getText().toString());
            data.setUsername(textInputPhone.getText().toString());
            data.setMobile(textInputPhone.getText().toString());
            data.setNotificationToken(mLoginViewModel.tinyDB.getString(Constants.FCM_TOKEN));
            data.setAppVersion(BuildConfig.VERSION_CODE);
            data.setAdditionalInfo(Utils.getPhoneInfo());
            data.setLoginVia(1);
            data.setCompanyId(BuildConfig.COMPANY_ID);

            CandidateLoginRequest request = new CandidateLoginRequest();
            request.setCandidateLoginRequestData(data);
            mLoginViewModel.callLoginApi(request);

            logginClicked = true;

            if(dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 3000);
    }


}
