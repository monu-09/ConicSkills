package com.conicskill.app.ui.news;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.conicskill.app.data.model.news.Feed;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelRequest;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<Feed>> feedResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<YoutubeChannelResponse>> youtubeResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    // TODO: Implement the ViewModel
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;

    @Inject
    public NewsViewModel(TinyDB tinyDB, Application application, RepoRepository repoRepository) {
        super(application);
        this.tinyDB = tinyDB;
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
    }

    LiveData<Response<Feed>> checkFeed() {
        return feedResponse;
    }

    LiveData<Response<YoutubeChannelResponse>> observeYoutubeResponse() {
        return youtubeResponse;
    }

    LiveData<Boolean> getError() {
        return loginError;
    }

    LiveData<Boolean> getLoading() {
        return loginLoading;
    }

    public void callNewsFeedApi(String url) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.getNewsFeed(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<Feed>>(){
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<Feed> responseOfFeedFromServer) {
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

    public void callFetchYoutubeVideosApi(YoutubeChannelRequest request) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.getYoutubeVideo(request).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<YoutubeChannelResponse>>(){
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<YoutubeChannelResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        youtubeResponse.setValue(responseOfFeedFromServer);
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
