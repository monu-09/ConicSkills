package com.conicskill.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.conicskill.app.data.model.examlisting.ExamListResponse
import com.conicskill.app.data.model.jobNotification.JobDetailRequest
import com.conicskill.app.data.model.jobNotification.JobDetailResponse
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


public class JobNotificationViewModel @Inject public constructor(application: Application, public var tinyDB: TinyDB,
                                                 public var repoRepository: RepoRepository) : AndroidViewModel(application) {

    private var disposable: CompositeDisposable? = CompositeDisposable()
    private val apiError = MutableLiveData<Boolean>()
    private val apiLoading = MutableLiveData<Boolean>()
    val jobNotificationList = MutableLiveData<Response<JobNotificationResponse?>>()
    val jobDetailResponse = MutableLiveData<Response<JobDetailResponse?>>()

    fun getError(): LiveData<Boolean?>? {
        return apiError
    }

    fun getLoading(): LiveData<Boolean?>? {
        return apiLoading
    }

    fun getJobNotificationList(): LiveData<Response<JobNotificationResponse?>?>? {
        return jobNotificationList
    }

    fun getJobDetails(): LiveData<Response<JobDetailResponse?>?>? {
        return jobDetailResponse
    }

    fun callFetchJobList(jobRequest: JobRequest) {
        disposable!!.add(repoRepository!!.callGetJobNotificationListing(jobRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<JobNotificationResponse?>?>() {
                    override fun onSuccess(carouselResponseWrapper: Response<JobNotificationResponse?>) {
                        apiLoading.value = false
                        apiError.value = false
                        jobNotificationList.value = carouselResponseWrapper
                    }

                    override fun onError(e: Throwable) {
                        apiError.value = true
                        apiLoading.value = false
                    }
                }))
    }

    fun callFetchJobDetails(jobDetailRequest: JobDetailRequest) {
        disposable!!.add(repoRepository!!.callGetJobDetails(jobDetailRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<JobDetailResponse?>?>() {
                    override fun onSuccess(carouselResponseWrapper: Response<JobDetailResponse?>) {
                        apiLoading.value = false
                        apiError.value = false
                        jobDetailResponse.value = carouselResponseWrapper
                    }

                    override fun onError(e: Throwable) {
                        apiError.value = true
                        apiLoading.value = false
                    }
                }))
    }
}