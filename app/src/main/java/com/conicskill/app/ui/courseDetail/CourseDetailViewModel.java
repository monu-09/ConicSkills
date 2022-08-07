package com.conicskill.app.ui.courseDetail;

import android.app.Application;

import com.conicskill.app.data.model.notes.NotesListRequest;
import com.conicskill.app.data.model.notes.NotesListResponse;
import com.conicskill.app.data.model.playlist.PlaylistResponse;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.videoCouses.UpcomingCourseResponse;
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

public class CourseDetailViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<UpcomingCourseResponse>> upcomingResponse = new MutableLiveData<>();
    MutableLiveData<Response<NotesListResponse>> notesListResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<PlaylistResponse>> playListResponse = new MutableLiveData<>();
    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;

    @Inject
    public CourseDetailViewModel(@NonNull Application application, RepoRepository repoRepository, TinyDB tinyDB) {
        super(application);
        this.repoRepository = repoRepository;
        this.tinyDB = tinyDB;
        this.disposable = new CompositeDisposable();
    }

    public LiveData<Response<UpcomingCourseResponse>> observeCourseAPI() {
        return upcomingResponse;
    }
    public LiveData<Response<PlaylistResponse>> observeCourseCategoriesAPI() {
        return playListResponse;
    }



    LiveData<Boolean> getError() {
        return loginError;
    }

    LiveData<Boolean> getLoading() {
        return loginLoading;
    }

    public void callFetchCourseCategories(CurrentAffairsRequest currentAffairsRequest)
    {  loginLoading.setValue(true);
        disposable.add(repoRepository.callGetCourseCategories(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<PlaylistResponse>>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Response<PlaylistResponse> playlistResponseResponse) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);

                        playListResponse.setValue(playlistResponseResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
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


    public void callFetchCourseNotes(NotesListRequest currentAffairsRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.callGetNotesList(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<NotesListResponse>>(){
                    @Override
                    public void onSuccess(Response<NotesListResponse> upcomingCourseResponseResponse) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        notesListResponse.setValue(upcomingCourseResponseResponse);
                    }

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
