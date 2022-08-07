package com.conicskill.app.ui.exam;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.conicskill.app.data.model.exam.ExamResponse;
import com.conicskill.app.data.model.exam.ModuleRequest;
import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.exam.Options;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.data.model.login.LoginRequest;
import com.conicskill.app.data.model.login.LoginResponse;
import com.conicskill.app.data.model.saveTest.SaveTestRequest;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisRequest;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisResponse;
import com.conicskill.app.data.rest.RepoRepository;
import com.conicskill.app.database.AppDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ExamViewModel extends AndroidViewModel {

    AppDatabase mAppDatabase;
    @Inject
    public ExamViewModel(@NonNull Application application, RepoRepository repoRepository, AppDatabase appDatabase) {
        super(application);
        this.repoRepository = repoRepository;
        this.disposable = new CompositeDisposable();
        this.mAppDatabase = appDatabase;
    }

    private RepoRepository repoRepository;
    private CompositeDisposable disposable;
    private final MutableLiveData<Boolean> apiError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> apiLoading = new MutableLiveData<>();
    private final MutableLiveData<Response<ExamResponse>> examResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<LoginResponse>> loginResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<SaveTestRequest>> saveTestResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<TestAnalysisResponse>> testAnalysisResponse = new MutableLiveData<>();
    private final MutableLiveData<List<Modules>> modulesData = new MutableLiveData<>();
    private HashMap<String, HashMap<String, ArrayList<Questions>>> moduleLanguageQuestionsMap = new HashMap<>();
    public HashMap<String, Integer> moduleQuestionIdMapping = new HashMap<>();
    private List<Modules> modulesList = new ArrayList<>();
    private String examTitle = "";
    public LiveData<Boolean> getError() {
        return apiError;
    }

    public LiveData<Boolean> getLoading() {
        return apiLoading;
    }

    public LiveData<Response<ExamResponse>> returnFetchedQuestions() {
        return examResponse;
    }

    public LiveData<Response<LoginResponse>> returnLoginResponse() {
        return loginResponse;
    }

    public LiveData<List<Modules>> observeModulesFromDB() {
        return modulesData;
    }

    public LiveData<Response<SaveTestRequest>> observeSaveTestRequest() { return saveTestResponse; }
    public LiveData<Response<TestAnalysisResponse>> observeTestAnalysisRequest() { return testAnalysisResponse; }

    public void fetchModuleQuestions(ModuleRequest moduleRequest) {
        apiLoading.setValue(true);
        disposable.add(repoRepository.fetchModuleQuestions(moduleRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<ExamResponse>>() {
                    @Override
                    public void onSuccess(Response<ExamResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        examResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void doTestLogin(LoginRequest loginRequest) {
        apiLoading.setValue(true);
        disposable.add(repoRepository.testLogin(loginRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<LoginResponse>>() {
                    @Override
                    public void onSuccess(Response<LoginResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        loginResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void insertModules(List<Modules> modules) {
        mAppDatabase.moduleDao().insert(modules).observeOn(Schedulers.io())
                .subscribe(new DisposableMaybeObserver<List<Long>>() {
                    @Override
                    public void onSuccess(List<Long> value) {
                        Log.e("DB", "onSuccess: " + value.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DB", "onError: " +e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("DB", "Completed: ");
                    }
                });

    }

    public void fetchModules(List<String> moduleId) {
        apiLoading.setValue(true);
        mAppDatabase.moduleDao().fetchModulesFromDB(moduleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Modules>>() {
                    @Override
                    public void onSuccess(List<Modules> modules) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                        modulesList.clear();
                        modulesList.addAll(modules);
                        for (int i = 0, k =0; i < modules.size(); i++) {
                            if(modules.get(i).getAttemptedQuestionsData() != null) {
                                JsonElement jsonElement = new Gson().fromJson(modules.get(i).getAttemptedQuestionsData(), JsonElement.class);
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                String moduleIDLocal = modules.get(i).getModuleId();
                                HashMap<String, ArrayList<Questions>> languageQuestions = new HashMap<>();
                                // run loops for languages
                                if (jsonObject.has("en")) {
                                    Type listType = new TypeToken<ArrayList<Questions>>() {
                                    }.getType();
                                    ArrayList<Questions> questions = new Gson().fromJson(jsonObject.getAsJsonArray("en"), listType);
                                    if (i == 0) {
                                        moduleQuestionIdMapping.put(modules.get(i).getModuleId(), 0);
                                    } else {
                                        moduleQuestionIdMapping.put(modules.get(i).getModuleId(), k);
                                    }
                                    for (int j = 0; j < questions.size(); j++) {
                                        k++;
                                        questions.get(j).setOptionsList(customOptions(questions.get(j).getOptions()));
                                    }
                                    languageQuestions.put("en", questions);
                                }

                                if (jsonObject.has("hi")) {
                                    Type listType = new TypeToken<ArrayList<Questions>>() {
                                    }.getType();
                                    ArrayList<Questions> questions = new Gson().fromJson(jsonObject.getAsJsonArray("hi"), listType);


                                    for (int j = 0; j < questions.size(); j++) {
                                        questions.get(j).setOptionsList(customOptions(questions.get(j).getOptions()));
                                    }
                                    languageQuestions.put("hi", questions);
                                }
                                moduleLanguageQuestionsMap.put(moduleIDLocal, languageQuestions);
                            }
                        }
                        modulesData.setValue(modules);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                    }
                });
    }

    /**
     * This function is used in the application for saving all the request for the saving of the test
     * object in the application
     * @param saveTestRequest - The saveTestRequest contains all the request for the saving the test
     *                        object
     */
    public void callSaveTestObjectAPI(SaveTestRequest saveTestRequest) {
        apiLoading.setValue(true);
        disposable.add(repoRepository.callSaveTestObject(saveTestRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<SaveTestRequest>>() {
                    @Override
                    public void onSuccess(Response<SaveTestRequest> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        saveTestResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void callTestAnalysisAPI(TestAnalysisRequest testAnalysisRequest) {
        apiLoading.setValue(true);
        disposable.add(repoRepository.callTestAnalysisAPI(testAnalysisRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<TestAnalysisResponse>>() {
                    @Override
                    public void onSuccess(Response<TestAnalysisResponse> value) {
                        apiError.setValue(false);
                        apiLoading.setValue(false);
                        testAnalysisResponse.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiLoading.setValue(false);
                        apiError.setValue(false);
                    }
                }));
    }

    public void setModulesData(HashMap<String, HashMap<String, ArrayList<Questions>>> moduleLanguageQuestionsMapData,
                               List<Modules> modulesListThere) {
        moduleLanguageQuestionsMap = moduleLanguageQuestionsMapData;
        modulesList = modulesListThere;
    }

    public HashMap<String, HashMap<String, ArrayList<Questions>>> getModulesData() {
        return moduleLanguageQuestionsMap;
    }

    public List<Modules> getModulesList() {
        return modulesList;
    }

    public void setExamTitle(String title) {
        examTitle = title;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void saveValues(String data, String moduleId) {
        mAppDatabase.moduleDao().updateQuestionData(data, moduleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Log.e("This", integer + "");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private List<Options> customOptions(JsonObject object) {
        List<Options> optionsArrayList = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            Options options = new Options();
            if (entry.getValue() != null && entry.getKey() != null) {
                options.setKey(entry.getKey());
                options.setValue(entry.getValue().getAsString());
                optionsArrayList.add(options);
            }
        }
        return optionsArrayList;
    }

    public void saveModuleQuestions(HashMap<String, Integer> moduleNumbersQuestions) {
        moduleQuestionIdMapping = moduleNumbersQuestions;
    }
}
