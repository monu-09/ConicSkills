package com.conicskill.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordRequest
import com.conicskill.app.data.model.candidateLogin.ForgotPasswordResponse
import com.conicskill.app.data.model.jobNotification.JobNotificationResponse
import com.conicskill.app.data.model.jobNotification.JobRequest
import com.conicskill.app.data.rest.RepoRepository
import com.conicskill.app.util.TinyDB
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class ForgotPasswordViewModel @Inject public constructor(application: Application, public var tinyDB: TinyDB,
                                                         public var repoRepository: RepoRepository) :
        AndroidViewModel(application) {

    private var disposable: CompositeDisposable? = CompositeDisposable()
    private val apiError = MutableLiveData<Boolean>()
    private val apiLoading = MutableLiveData<Boolean>()
    val forgotPasswordResponse = MutableLiveData<Response<ForgotPasswordResponse?>>()

    fun callForgotPasswordApi(forgotPasswordRequest: ForgotPasswordRequest) {
        apiLoading.value = true
        disposable!!.add(repoRepository!!.callForgotPasswordApi(forgotPasswordRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<ForgotPasswordResponse?>?>() {
                    override fun onSuccess(carouselResponseWrapper: Response<ForgotPasswordResponse?>) {
                        apiLoading.value = false
                        apiError.value = false
                        forgotPasswordResponse.value = carouselResponseWrapper
                    }

                    override fun onError(e: Throwable) {
                        apiError.value = true
                        apiLoading.value = false
                    }
                }))
    }

}