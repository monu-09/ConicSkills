package com.conicskill.app.ui.help;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.conicskill.app.data.model.candidateLogin.BaseResponse;
import com.conicskill.app.data.model.candidateLogin.HelpResponse;
import com.conicskill.app.data.model.currentAffairDetails.CADetailRequest;
import com.conicskill.app.data.model.currentAffairDetails.CADetailResponse;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsItem;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentResponse;
import com.conicskill.app.data.model.help.HelpRequest;
import com.conicskill.app.data.model.wordDay.WordRequest;
import com.conicskill.app.data.model.wordDay.WordResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HelpViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    MutableLiveData<Response<HelpResponse>> helpResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;


    @Inject
    public HelpViewModel(Application application, TinyDB tinyDB, RepoRepository repoRepository) {
        super(application);
        this.tinyDB = tinyDB;
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
    }

    LiveData<Response<HelpResponse>> helpResponse() {
        return helpResponse;
    }


    public LiveData<Boolean> getError() {
        return loginError;
    }

    public LiveData<Boolean> getLoading() {
        return loginLoading;
    }

    public void callHelpApi(HelpRequest currentAffairsRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callHelpRequest(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<HelpResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(@NonNull Response<HelpResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        helpResponse.setValue(responseOfFeedFromServer);
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
