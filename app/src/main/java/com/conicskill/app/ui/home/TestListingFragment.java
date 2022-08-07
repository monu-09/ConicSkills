package com.conicskill.app.ui.home;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.exam.ModuleBodyWrapper;
import com.conicskill.app.data.model.exam.ModuleRequest;
import com.conicskill.app.data.model.exam.ModuleRequestData;
import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.data.model.examlisting.Data;
import com.conicskill.app.data.model.examlisting.ExamListRequest;
import com.conicskill.app.data.model.examlisting.Filter;
import com.conicskill.app.data.model.login.LoginRequest;
import com.conicskill.app.data.model.login.LoginRequestData;
import com.conicskill.app.data.model.login.LoginWrapper;
import com.conicskill.app.data.model.testListing.TestListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.ExamActivity;
import com.conicskill.app.ui.exam.ExamViewModel;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.ProgressOverlay;
import com.conicskill.app.util.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import es.dmoral.toasty.Toasty;

import static com.conicskill.app.util.Constants.EXAM_INSTRUCTIONS;

public class TestListingFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewTestList)
    RecyclerView recyclerViewTestList;
    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<String> moduleId = new ArrayList<>();
    private MockTestViewModel mockTestViewModel;
    private ExamViewModel mExamViewModel;
    private OneAdapter oneAdapter;
    private String authToken;
    private String testDuration = "0", examTitle = "SSC";
    private ProgressDialog progressOverlay;
    private static String imageUrl = "";
    private static String testSeriesId = "1";

    public static TestListingFragment newInstance(String id, String url) {
        imageUrl = url;
        testSeriesId = id;
        return new TestListingFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.mock_test_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mockTestViewModel = new ViewModelProvider(this, viewModelFactory).get(MockTestViewModel.class);
        mExamViewModel = new ViewModelProvider(this, viewModelFactory).get(ExamViewModel.class);
        observeViewModel();

        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewTestList.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewTestList.setItemAnimator(null);

        oneAdapter = new OneAdapter(recyclerViewTestList)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));

        ExamListRequest examListRequest = new ExamListRequest();
        Filter filter = new Filter();
        filter.setTestsettingsid(testSeriesId);
        Data data = new Data();
        data.setFilter(filter);
        examListRequest.setData(data);
        examListRequest.setToken(mockTestViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        mockTestViewModel.fetchTestSeries(examListRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void observeViewModel() {
        mockTestViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
            } else {
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        mockTestViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    Toasty.error(getContext(), "Failed to load test. Please try again.", Toasty.LENGTH_LONG, false).show();
                }
            }
        });

        mExamViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(getActivity(), "Fetching Exam List");
            } else {
                progressOverlay.dismiss();
            }
        });

        mExamViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error) {
                    progressOverlay.dismiss();
                    Toasty.error(getContext(), "Failed to register. Please try again.", Toasty.LENGTH_LONG, false).show();
                }
            }
        });

        mockTestViewModel.getTestList().observe(getViewLifecycleOwner(), examListResponseResponse -> {
            if (examListResponseResponse != null) {
                if (examListResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                    if (examListResponseResponse.body().isError()) {

                    } else {
                        if (examListResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                            if (examListResponseResponse.body().getData().getTestList().size() != 0) {
                                oneAdapter.setItems(examListResponseResponse.body().getData().getTestList());
                                oneAdapter.getItemCount();
                                recyclerViewTestList.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });

        mExamViewModel.observeModulesFromDB().observe(getViewLifecycleOwner(), modules -> {
            if (modules.size() == moduleId.size()) {
                Intent intent = new Intent(getActivity(), ExamActivity.class);
                intent.putStringArrayListExtra("modules", moduleId);
                intent.putExtra("duration", testDuration);
                intent.putExtra("examTitle", examTitle);
                intent.putExtra("authToken", authToken);
                intent.putExtra("redirectTo", EXAM_INSTRUCTIONS);
                startActivity(intent);
            } else {
                ModuleRequestData moduleRequestData = new ModuleRequestData();
                moduleRequestData.setModuleId(moduleId);
                moduleRequestData.setToken(authToken);

                ModuleRequest moduleRequest = new ModuleRequest();
                moduleRequest.setModuleRequestData(moduleRequestData);
                moduleRequest.setToken(authToken);

                ModuleBodyWrapper moduleBodyWrapper = new ModuleBodyWrapper();
                moduleBodyWrapper.setModuleRequest(moduleRequest);
                mExamViewModel.fetchModuleQuestions(moduleRequest);
            }
        });


        mExamViewModel.returnFetchedQuestions().observe(getViewLifecycleOwner(), examResponse -> {
            if (examResponse != null) {
                if (examResponse.code() == HttpsURLConnection.HTTP_OK) {
                    if (examResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                        if (examResponse.body().getExamData().getQuestionData() != null) {
                            try {
                                List<Modules> modules = examResponse.body().getExamData().getModuleData();
                                JsonObject jsonObject = examResponse.body().getExamData().getQuestionData();
                                for (int i = 0; i < modules.size(); i++) {
                                    String moduleIDLocal = modules.get(i).getModuleId();
                                    HashMap<String, ArrayList<Questions>> languageQuestions = new HashMap<>();
                                    if (jsonObject.has(moduleIDLocal)) {
                                        // run loops for languages
                                        JsonObject jsonObject1 = jsonObject.getAsJsonObject(moduleIDLocal);
                                        String questionData = jsonObject1.toString();
                                        modules.get(i).setQuestionsData(questionData);
                                    }
                                }
                                mExamViewModel.insertModules(modules);
                                Intent intent = new Intent(getActivity(), ExamActivity.class);
                                intent.putStringArrayListExtra("modules", moduleId);
                                intent.putExtra("duration", testDuration);
                                intent.putExtra("examTitle", examTitle);
                                intent.putExtra("authToken", authToken);
                                intent.putExtra("redirectTo", Constants.EXAM_INSTRUCTIONS);
                                startActivity(intent);
                            } catch (JsonSyntaxException e) {
                                Log.e("", "observeViewModel: " + e.getLocalizedMessage());
                            }
                        }
                    }
                }
            }
        });

        mExamViewModel.returnLoginResponse().observe(getViewLifecycleOwner(), loginResponseResponse -> {
            if (loginResponseResponse != null) {
                if (loginResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                    if (loginResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                        String allowedModules = loginResponseResponse.body().getData().getTestData().getAllowedModules();
                        String[] arrayModules = allowedModules.split(",");

                        List<String> IDs = Arrays.asList(arrayModules);
                        moduleId.clear();
                        moduleId.addAll(IDs);
                        authToken = loginResponseResponse.body().getData().getAuthToken();
                        testDuration = loginResponseResponse.body().getData().getTestData().getTestDuration();
                        mExamViewModel.fetchModules(moduleId);
                    } else if (loginResponseResponse.body().getCode() == 5001) {
                        String allowedModules = loginResponseResponse.body().getData().getTestData().getAllowedModules();
                        String[] arrayModules = allowedModules.split(",");

                        List<String> IDs = Arrays.asList(arrayModules);
                        moduleId.clear();
                        moduleId.addAll(IDs);
                        authToken = loginResponseResponse.body().getData().getAuthToken();
                        authToken = loginResponseResponse.body().getData().getAuthToken();
                        Intent intent = new Intent(getActivity(), ExamActivity.class);
                        intent.putStringArrayListExtra("modules", moduleId);
                        intent.putExtra("duration", testDuration);
                        intent.putExtra("examTitle", examTitle);
                        intent.putExtra("authToken", authToken);
                        intent.putExtra("redirectTo", Constants.EXAM_SOLUTIONS);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @NotNull
    private ItemModule<TestListItem> featureItems() {
        return new ItemModule<TestListItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_series_list;
                    }

                    @Nullable
                    @Override
                    public Animator withFirstBindAnimation() {
                        // can be implemented by inflating Animator Xml
                        return null;
                    }
                };
            }

            @Override
            public void onBind(@NotNull Item<TestListItem> item, @NotNull ViewBinder viewBinder) {
                LinearLayout linearLayoutBackground = viewBinder.findViewById(R.id.linearLayoutBackground);
                AppCompatImageView featureIcon = viewBinder.findViewById(R.id.featureIcon);
                AppCompatImageView imageViewLock = viewBinder.findViewById(R.id.imageViewLock);
                AppCompatTextView featureName = viewBinder.findViewById(R.id.featureName);
                AppCompatTextView textViewTotalMarks = viewBinder.findViewById(R.id.textViewTotalMarks);
                AppCompatTextView textViewTotalQuestions = viewBinder.findViewById(R.id.textViewTotalQuestions);
                AppCompatTextView textViewTestModules = viewBinder.findViewById(R.id.textViewTestModules);
                AppCompatTextView textViewTotalTime = viewBinder.findViewById(R.id.textViewTotalTime);
                AppCompatTextView textViewStartDate = viewBinder.findViewById(R.id.textViewStartDate);
                AppCompatTextView textViewEndDate = viewBinder.findViewById(R.id.textViewEndDate);
                MaterialButton buttonStart = viewBinder.findViewById(R.id.buttonStart);
                LinearLayout linearLayoutSolutions = viewBinder.findViewById(R.id.linearLayoutSolutions);
                LinearLayout linearLayoutButtons = viewBinder.findViewById(R.id.linearLayoutButtons);
//                AppCompatTextView textViewTestLocked = viewBinder.findViewById(R.id.textViewTestLocked);

//                linearLayoutBackground.setBackgroundResource(model.getBackgroundDrawable());
                GlideApp.with(getContext()).load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(featureIcon);
//                featureIcon.setImageResource(R.drawable.ic_backspace_black_24dp);
                featureName.setText(item.getModel().getTestName());

                if(item.getModel().isLocked()) {
                    buttonStart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_grey_500));
                    buttonStart.setIconResource(R.drawable.ic_lock_white_24dp);
                    buttonStart.setText("Test Locked");
                } else {
                    buttonStart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.persian_green));
                    buttonStart.setIconResource(R.drawable.ic_play_circle_outline_white_24dp);
                    buttonStart.setText("Start Test");

                }

                textViewTotalMarks.setText(item.getModel().getTestScore());
                textViewTotalQuestions.setText(item.getModel().getQuestionCount());

                String[] arrayModules = item.getModel().getAllowedModules().split(",");
                List<String> IDs = Arrays.asList(arrayModules);

                textViewTestModules.setText(String.valueOf(IDs.size()));
                textViewTotalTime.setText(item.getModel().getTestDuration());
                textViewStartDate.setText(Utils.getInstance(getContext()).getHumanReadableDate(item.getModel().getStartTime()));
                textViewEndDate.setText(Utils.getInstance(getContext()).getHumanReadableDate(item.getModel().getEndTime()));
                buttonStart.setOnClickListener(l->{
                    if(item.getModel().isLocked()) {
                        Toasty.info(getContext(), "The test will be started on "
                                        + Utils.getInstance(getContext()).getHumanReadableDate(item.getModel().getStartTime()),
                                Toasty.LENGTH_LONG, false).show();
                    } else {
                        LoginRequestData loginRequestData = new LoginRequestData();
                        loginRequestData.setPassword(item.getModel().getPassword());
                        loginRequestData.setUsername(item.getModel().getUserName());

                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setLanguage("en");
                        loginRequest.setLoginRequestData(loginRequestData);

                        LoginWrapper loginWrapper = new LoginWrapper();
                        loginWrapper.setRequest(loginRequest);

                        examTitle = item.getModel().getTestName();
                        mExamViewModel.doTestLogin(loginRequest);
                    }
                });

                if(item.getModel().isCompleted()) {
                    linearLayoutSolutions.setVisibility(View.VISIBLE);
                    linearLayoutButtons.setVisibility(View.GONE);
                } else {
                    linearLayoutSolutions.setVisibility(View.GONE);
                    linearLayoutButtons.setVisibility(View.VISIBLE);
                }
            }
        };
    }



    @NotNull
    private ClickEventHook<TestListItem> clickEventHook() {
        return new ClickEventHook<TestListItem>() {


            @Override
            public void onClick(@NotNull TestListItem model, @NotNull ViewBinder viewBinder) {
                /*LoginRequestData loginRequestData = new LoginRequestData();
                loginRequestData.setPassword(model.getPassword());
                loginRequestData.setUsername(model.getUserName());

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setLanguage("en");
                loginRequest.setLoginRequestData(loginRequestData);

                LoginWrapper loginWrapper = new LoginWrapper();
                loginWrapper.setRequest(loginRequest);

                examTitle = model.getTestName();
                mExamViewModel.doTestLogin(loginRequest);*/
            }
        };
    }

}
