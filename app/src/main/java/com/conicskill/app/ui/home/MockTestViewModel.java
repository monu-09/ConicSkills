package com.conicskill.app.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.examlisting.ExamListRequest;
import com.conicskill.app.data.model.examlisting.ExamListResponse;
import com.conicskill.app.data.model.notes.NotesResponse;
import com.conicskill.app.data.model.playlist.VideoCourseListingRequest;
import com.conicskill.app.data.model.testListing.TestListResponse;
import com.conicskill.app.data.model.videoCouses.VideoCoursesResponse;
import com.conicskill.app.data.model.videoCouses.VideoListResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MockTestViewModel extends AndroidViewModel {

    private RepoRepository repoRepository;
    private CompositeDisposable disposable;
    public TinyDB tinyDB;
    private final MutableLiveData<Boolean> apiError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> apiLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<ExamListResponse>> examListResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<TestListResponse>> testListResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<VideoCoursesResponse>> videoCoursesResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<VideoListResponse>> videoListResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<NotesResponse>> notesResponse= new MutableLiveData<>();

    @Inject
    public MockTestViewModel(@NonNull Application application, RepoRepository repoRepository, TinyDB tinyDB) {
        super(application);
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
        this.tinyDB = tinyDB;
    }

    public LiveData<Boolean> getError() {
        return apiError;
    }

    public LiveData<Boolean> getLoading() {
        return apiLoading;
    }

    public LiveData<Response<ExamListResponse>> getExamList() {
        return examListResponse;
    }

    public LiveData<Response<TestListResponse>> getTestList() {
        return testListResponse;
    }

    public LiveData<Response<VideoCoursesResponse>> observeFetchVideoCourses() {
        return videoCoursesResponse;
    }

    public LiveData<Response<VideoListResponse>> observeFetchVideoListByCourse() {
        return videoListResponse;
    }
    public LiveData<Response<NotesResponse>> observeGetNotes()
    {
        return  notesResponse;
    }
    public void fetchExamList(ExamListRequest examListRequest) {
        apiLoading.setValue(true);
        apiLoading.setValue(true);
        disposable.add(repoRepository.getTestSeries(examListRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<ExamListResponse>>() {
                    @Override
                    public void onSuccess(Response<ExamListResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        examListResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void fetchTestSeries(ExamListRequest examListRequest) {
        apiLoading.setValue(true);
        apiLoading.setValue(true);
        disposable.add(repoRepository.getTestList(examListRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<TestListResponse>>() {
                    @Override
                    public void onSuccess(Response<TestListResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        testListResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void fetchVideoCourses(CurrentAffairsRequest currentAffairsRequest) {
        apiLoading.setValue(true);
        apiLoading.setValue(true);
        disposable.add(repoRepository.callGetVideoCourses(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<VideoCoursesResponse>>() {
                    @Override
                    public void onSuccess(Response<VideoCoursesResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        videoCoursesResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void fetchVideoListByCourse(VideoCourseListingRequest currentAffairsRequest){
        apiLoading.setValue(true);
        apiLoading.setValue(true);
        disposable.add(repoRepository.callGetVideoListForCourse(currentAffairsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<VideoListResponse>>() {
                    @Override
                    public void onSuccess(Response<VideoListResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        videoListResponse.setValue(value);
                        videoListResponse.setValue(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }
    public void fetchNotesByCourse(VideoCourseListingRequest videoCourseListingRequest){
        apiLoading.setValue(true);
        apiLoading.setValue(true);
        disposable.add(repoRepository.callgetNotes(videoCourseListingRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<NotesResponse>>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Response<NotesResponse> notesResponseResponse) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        notesResponse.setValue(notesResponseResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                    }
                }));




    }

}
