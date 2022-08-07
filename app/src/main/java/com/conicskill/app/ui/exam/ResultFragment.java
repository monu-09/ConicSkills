package com.conicskill.app.ui.exam;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.data.model.testAnalysis.ModuleDataItem;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisRequest;
import com.conicskill.app.data.model.testAnalysis.TestAnalysisRequestData;
import com.conicskill.app.data.model.testAnalysis.TestData;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.ExamActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;

public class ResultFragment extends BaseFragment {

    private static String authToken = "";

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewModuleAnalysis)
    RecyclerView recyclerViewModuleAnalysis;
    @BindView(R.id.textViewScore)
    AppCompatTextView textViewScore;
    @BindView(R.id.textViewTotalMarks)
    AppCompatTextView textViewTotalMarks;
    @BindView(R.id.textViewCorrectAnswers)
    AppCompatTextView textViewCorrectAnswers;
    @BindView(R.id.textViewWrongAnswers)
    AppCompatTextView textViewWrongAnswers;
    @BindView(R.id.textViewUnanswered)
    AppCompatTextView textViewUnanswered;
    @BindView(R.id.pieChart)
    PieChart pieChart;
    @BindView(R.id.chipGroupQuestions)
    ChipGroup chipGroupQuestions;
    @BindView(R.id.textViewCorrectPercentage)
    AppCompatTextView textViewCorrectPercentage;
    @BindView(R.id.headerTestName)
    AppCompatTextView headerTestName;
    @BindView(R.id.buttonViewSolutions)
    AppCompatButton buttonViewSolutions;
    @BindView(R.id.linearLayoutContent)
    LinearLayout linearLayoutContent;
    @BindView(R.id.relativeLayoutLoading)
    RelativeLayout relativeLayoutLoading;
    @BindView(R.id.loadingAnimation)
    LottieAnimationView loadingAnimation;
    private OneAdapter oneAdapter;
    private ExamViewModel mExamViewModel;

    public static ResultFragment newInstance(String auth) {
        authToken = auth;
        return new ResultFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.result_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mExamViewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(ExamViewModel.class);
        ((ExamActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((ExamActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(((ExamActivity) getActivity()).getSupportActionBar()).setHomeButtonEnabled(true);
        Objects.requireNonNull(((ExamActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((ExamActivity) getActivity()).getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
        recyclerViewModuleAnalysis.setLayoutManager(gridLayoutManager);

        recyclerViewModuleAnalysis.setItemAnimator(null);
        oneAdapter = new OneAdapter(recyclerViewModuleAnalysis).attachItemModule(moduleResults());
        headerTestName.setText(mExamViewModel.getExamTitle());
        buttonViewSolutions.setOnClickListener(l -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SolutionsFragment.newInstance())
                    .addToBackStack("result")
                    .commit();
        });
        observeViewModel();
        loadingAnimation.setRepeatMode(LottieDrawable.RESTART);

        TestAnalysisRequestData testAnalysisRequestData = new TestAnalysisRequestData();
        testAnalysisRequestData.setSendModuleArrayValues("true");

        TestAnalysisRequest testAnalysisRequest = new TestAnalysisRequest();
        testAnalysisRequest.setToken(authToken);
        testAnalysisRequest.setData(testAnalysisRequestData);

        mExamViewModel.callTestAnalysisAPI(testAnalysisRequest);
    }

    @SuppressLint("SetTextI18n")
    public void observeViewModel() {
        mExamViewModel.observeTestAnalysisRequest().observe(getViewLifecycleOwner(), testAnalysisResponseResponse -> {
            if (testAnalysisResponseResponse.code() == HttpURLConnection.HTTP_OK) {
                if (testAnalysisResponseResponse.body() != null) {
                    if (testAnalysisResponseResponse.body().getCode() == HttpURLConnection.HTTP_OK) {
                        TestData testData = testAnalysisResponseResponse.body().getData().getTestData();
                        textViewScore.setText(String.valueOf(testData.getScore()));
                        textViewTotalMarks.setText(String.valueOf(testData.getMaxScore()));
                        textViewCorrectAnswers.setText(String.valueOf(testData.getCorrectAnswers()));
                        textViewWrongAnswers.setText(String.valueOf(testData.getAttemptedQuestions() - testData.getCorrectAnswers()));
                        textViewUnanswered.setText(String.valueOf(testData.getTotalQuestions() - testData.getAttemptedQuestions()));
                        float correct = (float) testData.getCorrectAnswers() / testData.getTotalQuestions();
                        float unset = (float) (testData.getTotalQuestions() - testData.getAttemptedQuestions()) / testData.getTotalQuestions();
                        float wrong = ((float) (testData.getAttemptedQuestions() - testData.getCorrectAnswers()) / testData.getTotalQuestions());
                        textViewCorrectPercentage.setText((correct * 100) + " %");

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry((int) (correct * 100), ""));
                        pieEntries.add(new PieEntry((int) (wrong * 100), ""));
                        pieEntries.add(new PieEntry((int) (unset * 100), ""));

                        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                        List<Integer> colorsList = new ArrayList<>();
                        colorsList.add(ContextCompat.getColor(getContext(), R.color.persian_green));
                        colorsList.add(ContextCompat.getColor(getContext(), R.color.persian_red));
                        colorsList.add(ContextCompat.getColor(getContext(), R.color.bg_like));

                        pieDataSet.setColors(colorsList);
                        pieDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.md_white_1000));
                        pieDataSet.setSliceSpace(4f);
                        pieDataSet.setValueTextSize(0f);

                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.getLegend().setEnabled(false);
                        pieChart.animate();
                        pieChart.invalidate();
                        oneAdapter.setItems(testAnalysisResponseResponse.body().getData().getModuleData());
                        questionListing();
                        linearLayoutContent.setVisibility(View.VISIBLE);
                        relativeLayoutLoading.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @NotNull
    private ItemModule<ModuleDataItem> moduleResults() {
        return new ItemModule<ModuleDataItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_module_result;
                    }

                    @Nullable
                    @Override
                    public Animator withFirstBindAnimation() {
                        // can be implemented by inflating Animator Xml
                        return null;
                    }
                };
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBind(@NotNull Item<ModuleDataItem> item, @NotNull ViewBinder viewBinder) {
                PieChart pieChart = viewBinder.findViewById(R.id.pieChart);
                AppCompatTextView textViewModuleName = viewBinder.findViewById(R.id.textViewModuleName);
                AppCompatTextView textViewModuleTotalMarks = viewBinder.findViewById(R.id.textViewModuleTotalMarks);
                AppCompatTextView textViewModuleScore = viewBinder.findViewById(R.id.textViewModuleScore);
                AppCompatTextView textViewCorrectQuestions = viewBinder.findViewById(R.id.textViewCorrectQuestions);
                AppCompatTextView textViewInCorrectQuestions = viewBinder.findViewById(R.id.textViewInCorrectQuestions);
                AppCompatTextView textViewUnAttemptedQuestions = viewBinder.findViewById(R.id.textViewUnAttemptedQuestions);
                AppCompatTextView textViewCorrectPercentage = viewBinder.findViewById(R.id.textViewCorrectPercentage);
                ModuleDataItem testData = item.getModel();

                textViewModuleName.setText(item.getModel().getModuleName());
                textViewModuleTotalMarks.setText(String.valueOf(item.getModel().getMaxScore()));
                textViewModuleScore.setText(String.valueOf(item.getModel().getScore()));
                textViewCorrectQuestions.setText(String.valueOf(item.getModel().getCorrectAnswers()));
                textViewInCorrectQuestions.setText(String.valueOf(item.getModel().getAttemptedQuestions() - item.getModel().getCorrectAnswers()));
                textViewUnAttemptedQuestions.setText(String.valueOf(item.getModel().getTotalQuestions() - item.getModel().getAttemptedQuestions()));

                float correct = (float) testData.getCorrectAnswers() / testData.getTotalQuestions();
                float unset = (float) (testData.getTotalQuestions() - testData.getAttemptedQuestions()) / testData.getTotalQuestions();
                float wrong = ((float) (testData.getAttemptedQuestions() - testData.getCorrectAnswers()) / testData.getTotalQuestions());
                textViewCorrectPercentage.setText((correct * 100) + " %");
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry((int) (correct * 100), ""));
                pieEntries.add(new PieEntry((int) (wrong * 100), ""));
                pieEntries.add(new PieEntry((int) (unset * 100), ""));

                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                List<Integer> colorsList = new ArrayList<>();
                colorsList.add(ContextCompat.getColor(getContext(), R.color.persian_green));
                colorsList.add(ContextCompat.getColor(getContext(), R.color.persian_red));
                colorsList.add(ContextCompat.getColor(getContext(), R.color.bg_like));

                pieDataSet.setColors(colorsList);
                pieDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.md_white_1000));
                pieDataSet.setSliceSpace(4f);
                pieDataSet.setValueTextSize(0f);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.getLegend().setEnabled(false);
                pieChart.animate();
                pieChart.invalidate();
            }

        };
    }

    public void questionListing() {
        if (chipGroupQuestions.getChildCount() == 0) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0, k = 1; i < mExamViewModel.getModulesList().size(); i++) {
                ArrayList<Questions> questionsList = mExamViewModel.getModulesData().get(mExamViewModel.getModulesList().get(i).getModuleId()).get("en");
                Chip chip;
                for (int j = 0; j < questionsList.size(); j++, k++) {
                    if (questionsList.get(j).getSelectedOptions().size() == 0) {
                        chip = (Chip) layoutInflater.inflate(R.layout.layout_unattempted_question, null);
                    } else {
                        if (checkCorrectAnswer(questionsList.get(j).getSelectedOptions(), questionsList.get(j).getCorrectAnswer())) {
                            chip = (Chip) layoutInflater.inflate(R.layout.layout_correct_answer, null);
                        } else {
                            chip = (Chip) layoutInflater.inflate(R.layout.layout_wrong_answer, null);
                        }
                    }
                    chip.setId(Integer.parseInt(questionsList.get(j).getQuestionId()));
                    if (k <= 9) {
                        chip.setText("0" + k);
                    } else {
                        chip.setText(String.valueOf(k));
                    }
                    chipGroupQuestions.addView(chip);
                }
            }
        }
    }

    private boolean checkCorrectAnswer(List<String> selectedOption, String correctAnswer) {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {
        };
        List<String> answers = new Gson().fromJson(correctAnswer, token.getType());
        return answers.containsAll(selectedOption);
    }
}
