package com.conicskill.app.ui;

import android.app.Application;

import com.conicskill.app.data.model.carousel.HomeCarouselRequest;
import com.conicskill.app.data.model.carousel.HomeCarouselResponse;
import com.conicskill.app.data.model.payments.PaymentRequest;
import com.conicskill.app.data.model.payments.PaymentResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PaymentViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<HomeCarouselResponse>> homeCarouselResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<PaymentResponse>> paymentResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;

    @Inject
    public PaymentViewModel(@NonNull Application application, TinyDB tinyDB, RepoRepository repoRepository) {
        super(application);
        this.tinyDB = tinyDB;
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
    }

    public LiveData<Boolean> getError() {
        return loginError;
    }

    public LiveData<Boolean> getLoading() {
        return loginLoading;
    }

    public LiveData<Response<HomeCarouselResponse>> getHomeCarousel() {
        return homeCarouselResponse;
    }

    public LiveData<Response<PaymentResponse>> observePaymentResponse() {
        return paymentResponse;
    }

    public void callProcessPayment(PaymentRequest paymentRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callPurchaseInformation(paymentRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<PaymentResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param carouselResponseWrapper the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<PaymentResponse> carouselResponseWrapper) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        paymentResponse.setValue(carouselResponseWrapper);
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
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }

}
