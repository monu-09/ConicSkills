package com.conicskill.app.ui.home;

import android.app.Application;

import com.conicskill.app.data.model.carousel.HomeCarouselRequest;
import com.conicskill.app.data.model.carousel.HomeCarouselResponse;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.home.HomeLayoutRequest;
import com.conicskill.app.data.model.home.HomeLayoutResponse;
import com.conicskill.app.data.model.videoCouses.UpcomingCourseResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<HomeCarouselResponse>> homeCarouselResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<UpcomingCourseResponse>> upcomingResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<HomeLayoutResponse>> homeCoursesLayoutResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;

    @Inject
    public HomeViewModel(@NonNull Application application, TinyDB tinyDB, RepoRepository repoRepository) {
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

    public LiveData<Response<HomeLayoutResponse>> getHomeLayoutResponse() {
        return homeCoursesLayoutResponse;
    }

    public LiveData<Response<UpcomingCourseResponse>> observeCourseAPI() {
        return upcomingResponse;
    }

    public void callFetchHomeCarousel(HomeCarouselRequest homeCarouselRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.fetchHomeCarousel(homeCarouselRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<HomeCarouselResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param carouselResponseWrapper the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<HomeCarouselResponse> carouselResponseWrapper) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        homeCarouselResponse.setValue(carouselResponseWrapper);
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

    /**
     * @date 27-May-2018
     * @use Call login API in the application in order to make the user login
     */
    public void callFetchCourseVideo(CurrentAffairsRequest currentAffairsRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callGetUpcomingClasses(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<UpcomingCourseResponse>>(){
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param upcomingCourseResponseResponse the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<UpcomingCourseResponse> upcomingCourseResponseResponse) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        upcomingResponse.setValue(upcomingCourseResponseResponse);
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

    public void callHomeCoursesApi(HomeLayoutRequest homeLayoutRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callGetHomeCoursesApi(homeLayoutRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<HomeLayoutResponse>>(){
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param upcomingCourseResponseResponse the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(@NotNull Response<HomeLayoutResponse> upcomingCourseResponseResponse) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        homeCoursesLayoutResponse.setValue(upcomingCourseResponseResponse);
                        homeCoursesLayoutResponse.setValue(null);
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

}
