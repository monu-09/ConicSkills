package com.conicskill.app.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.conicskill.app.data.model.candidateLogin.BaseResponse;
import com.conicskill.app.data.model.candidateLogin.CandidateLogOut;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequest;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginResponse;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordRequest;
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordResponse;
import com.conicskill.app.data.model.candidateRegister.RegisterRequest;
import com.conicskill.app.data.model.candidateRegister.RegisterResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<CandidateLoginResponse>> loginResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<RegisterResponse>> registerResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<ForgotPasswordResponse>> forgotPasswordResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<BaseResponse>> logOutResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;
    private String userName;

    @Inject
    public LoginViewModel(Application application, RepoRepository repoRepository, TinyDB tinyDB) {
        super(application);
        this.repoRepository = repoRepository;
        this.tinyDB = tinyDB;
        this.disposable = new CompositeDisposable();
    }

    public LiveData<Response<CandidateLoginResponse>> checkLogin() {
        return loginResponse;
    }
    public LiveData<Response<RegisterResponse>> observeRegisterAPI() {
        return registerResponse;
    }
    public LiveData<Response<ForgotPasswordResponse>> observeForgotPassword() {
        return forgotPasswordResponse;
    }

    public LiveData<Response<BaseResponse>> observeLogOut() {
        return logOutResponse;
    }


    LiveData<Boolean> getError() {
        return loginError;
    }

    LiveData<Boolean> getLoading() {
        return loginLoading;
    }


    /**
     * @date 27-May-2018
     * @use Call login API in the application in order to make the user login
     */
    public void callLoginApi(CandidateLoginRequest candidateLoginRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.doCandidateLogin(candidateLoginRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<CandidateLoginResponse>>(){
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param candidateLoginResponse the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<CandidateLoginResponse> candidateLoginResponse) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        loginResponse.setValue(candidateLoginResponse);
                    }

                    /**
                     * Notifies the SingleObserver that the {@link } has experienced an error condition.
                     * <p>
                     * If the {@link } calls this method, it will not thereafter call {@link #onSuccess}.
                     *
                     * @param e the exception encountered by the Single
                     */
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }
    /** EOC **/

    public void callRegisterCandidate(RegisterRequest registerRequest){
        loginLoading.setValue(true);
        disposable.add(repoRepository.callRegisterCandidate(registerRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<RegisterResponse>>(){
                    @Override
                    public void onSuccess(Response<RegisterResponse> value) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        registerResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }

    public void callForgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callForgotPasswordApi(forgotPasswordRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<ForgotPasswordResponse>>(){
                    @Override
                    public void onSuccess(Response<ForgotPasswordResponse> value) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        forgotPasswordResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }

    public void callLogOut(CandidateLogOut candidateLogOut) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.doCandidateLogout(candidateLogOut).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<BaseResponse>>(){
                    @Override
                    public void onSuccess(Response<BaseResponse> value) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        logOutResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }
}
