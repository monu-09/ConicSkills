package com.conicskill.app.ui.splash;

import android.app.Application;

import com.conicskill.app.BuildConfig;
import com.conicskill.app.data.model.news.Feed;
import com.conicskill.app.data.model.update.CheckUpdateResponse;
import com.conicskill.app.data.model.update.UpdateRequest;
import com.conicskill.app.data.model.update.UpdateRequestData;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;
import javax.inject.Inject;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SplashViewModel extends AndroidViewModel {

    private final RepoRepository repoRepository;
    public final TinyDB mTinyDB;
    private CompositeDisposable disposable;

    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Response<CheckUpdateResponse>> checkUpdateResponse = new MutableLiveData<Response<CheckUpdateResponse>>();

    @Inject
    public SplashViewModel(RepoRepository repoRepository, TinyDB tinyDB, Application application) {
        super(application);
        this.repoRepository = repoRepository;
        this.mTinyDB = tinyDB;
        disposable = new CompositeDisposable();
        UpdateRequestData updateRequestData = new UpdateRequestData(
                Integer.parseInt(BuildConfig.COMPANY_ID),
                BuildConfig.VERSION_CODE
        );
        UpdateRequest updateRequest = new UpdateRequest(updateRequestData);
        callCheckUpdateApi(updateRequest);
    }

    LiveData<Response<CheckUpdateResponse>> observeUpdateCheck() {
        return checkUpdateResponse;
    }
    LiveData<Boolean> getError() {
        return repoLoadError;
    }
    LiveData<Boolean> getLoading() {
        return loading;
    }

    private void callCheckUpdateApi(UpdateRequest updateRequest) {
        loading.setValue(true);
        disposable.add(repoRepository.callCheckUpdate(updateRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<CheckUpdateResponse>>(){
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(@NotNull Response<CheckUpdateResponse> responseOfFeedFromServer) {
                        loading.setValue(false);
//                        repoLoadError.setValue(false);
                        checkUpdateResponse.setValue(responseOfFeedFromServer);
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
                        repoLoadError.setValue(true);
                        loading.setValue(false);
                    }
                }));

    }

    public boolean checkLoginStatus() {
        return false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
