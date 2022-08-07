package com.conicskill.app.ui.currentAffairs;

import android.app.Application;
import android.util.Log;

import com.conicskill.app.data.model.currentAffairDetails.CADetailRequest;
import com.conicskill.app.data.model.currentAffairDetails.CADetailResponse;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsItem;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentResponse;
import com.conicskill.app.data.model.wordDay.WordRequest;
import com.conicskill.app.data.model.wordDay.WordResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CurrentAffairsViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<CurrentResponse>> feedResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<WordResponse>> wordResponse = new MutableLiveData<>();
    private final MutableLiveData<CurrentAffairsItem> selectedCurrentAffair = new MutableLiveData<>();
    private final MutableLiveData<Response<CADetailResponse>> detailsResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;


    @Inject
    public CurrentAffairsViewModel(Application application, TinyDB tinyDB, RepoRepository repoRepository) {
        super(application);
        this.tinyDB = tinyDB;
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
    }

    LiveData<Response<CurrentResponse>> checkFeed() {
        return feedResponse;
    }

    public LiveData<Response<CADetailResponse>> checkDetails(){
        return detailsResponse;
    }

    public LiveData<Response<WordResponse>> checkWordOfDay() {
        return wordResponse;
    }


    public LiveData<Boolean> getError() {
        return loginError;
    }

    public LiveData<Boolean> getLoading() {
        return loginLoading;
    }

    public void callFetchCurrentAffairsAPI(CurrentAffairsRequest currentAffairsRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.fetchCurrentAffairs(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<CurrentResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<CurrentResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        feedResponse.setValue(responseOfFeedFromServer);
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

    public void callGetWordOfDayAPI(WordRequest wordRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.fetchWordOfDay(wordRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<WordResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<WordResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        wordResponse.setValue(responseOfFeedFromServer);
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

    public MutableLiveData<CurrentAffairsItem> getSelectedCurrentAffair() {
        return selectedCurrentAffair;
    }

    public void setSelectedCurrentAffair(CurrentAffairsItem item) {
        Log.e("Setting value", "" + item);
        selectedCurrentAffair.setValue(item);
    }

    public void fetchCurrentAffairDetailsAPI(CADetailRequest caDetailRequest){
        loginLoading.setValue(true);
        disposable.add(repoRepository.fetchCurrentAffairDetails(caDetailRequest)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<CADetailResponse>>() {
                    @Override
                    public void onSuccess(Response<CADetailResponse> caDetailResponseResponse) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        Log.i("CADetailResponse",""+caDetailResponseResponse);
                        detailsResponse.setValue(caDetailResponseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginError.setValue(true);
                        loginLoading.setValue(false);
                    }
                }));
    }

}
