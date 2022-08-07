package com.conicskill.app.ui.pdf;

import android.app.Application;
import android.util.Log;

import com.conicskill.app.data.model.chat.ChatRequest;
import com.conicskill.app.data.model.chat.GetChatResponse;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.SubCategoryItem;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.database.AppDatabase;
import com.conicskill.app.util.TinyDB;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PdfViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<GetChatResponse>> chatResponse = new MutableLiveData<>();
    private final MutableLiveData<List<CategoryListItem>> categoriesList = new MutableLiveData<>();
    private final MutableLiveData<List<SubCategoryItem>> subCategoriesList = new MutableLiveData<>();

    public TinyDB tinyDB;
    private RepoRepository repoRepository;
    private CompositeDisposable disposable;

    public AppDatabase mAppDatabase;

    @Inject
    public PdfViewModel(@NonNull Application application, TinyDB tinyDB, RepoRepository repoRepository,
                        AppDatabase appDatabase) {
        super(application);
        this.tinyDB = tinyDB;
        this.repoRepository = repoRepository;
        this.mAppDatabase = appDatabase;
        this.disposable = new CompositeDisposable();
    }

    public LiveData<Response<GetChatResponse>> getChatResponseLiveData() {
        return chatResponse;
    }

    public void callGetChatResponse(ChatRequest chatRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.getChatMessage(chatRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<GetChatResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<GetChatResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        chatResponse.setValue(responseOfFeedFromServer);
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

    public void sendChatMessage(ChatRequest chatRequest) {
        loginLoading.setValue(true);
        disposable.add(repoRepository.sendChatMessage(chatRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<GetChatResponse>>() {
                    /**
                     * Notifies the SingleObserver with a single item and that the {@link } has finished sending
                     * push-based notifications.
                     * <p>
                     * The {@link =} will not call this method if it calls {@link #onError}.
                     *
                     * @param responseOfFeedFromServer the item emitted by the Single
                     */
                    @Override
                    public void onSuccess(Response<GetChatResponse> responseOfFeedFromServer) {
                        loginLoading.setValue(false);
                        loginError.setValue(false);
                        chatResponse.setValue(responseOfFeedFromServer);
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

    public void getAllCourses() {
        mAppDatabase.moduleDao().getAllCourses().observeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<CourseListItem>>() {
                    @Override
                    public void onSuccess(@NonNull List<CourseListItem> courseListItems) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void getCourseById(CourseListItem courseListItem, String courseId, CategoryListItem
                              categoryListItem, SubCategoryItem subCategoryItem, String pdfUrl) {
        mAppDatabase.moduleDao().getCourseList(courseId).observeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<CourseListItem>>() {
                    @Override
                    public void onSuccess(@NonNull List<CourseListItem> courseListItems) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void getCategoriesByCourseId(CourseListItem courseListItem, CategoryListItem categoryListItem,
                              SubCategoryItem subCategoryItem, String pdfUrl){
        mAppDatabase.moduleDao().getCategoryList(courseListItem.getCourseId()).observeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<CategoryListItem>>() {
                    @Override
                    public void onSuccess(@NonNull List<CategoryListItem> categoryListItems) {
                        categoriesList.setValue(categoryListItems);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void getSubCategoriesByCategoryId(CategoryListItem categoryListItem,
                                             SubCategoryItem subCategoryItem,
                                             String pdfUrl) {
        mAppDatabase.moduleDao().getSubCategoryList(categoryListItem.getId()).observeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<SubCategoryItem>>() {
                    @Override
                    public void onSuccess(@NonNull List<SubCategoryItem> subCategoryItems) {
                        subCategoriesList.setValue(subCategoryItems);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
