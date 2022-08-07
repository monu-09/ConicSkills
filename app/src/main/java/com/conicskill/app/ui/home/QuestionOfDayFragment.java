package com.conicskill.app.ui.home;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.airbnb.lottie.LottieAnimationView;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.SelectableOption;
import com.conicskill.app.data.model.carousel.QuestionData;
import com.conicskill.app.data.model.exam.Languages;
import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.exam.Options;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.exam.ExamSelectableAdapter;
import com.conicskill.app.ui.exam.ExamViewModel;
import com.conicskill.app.ui.exam.LanguagesArrayAdapter;
import com.conicskill.app.ui.exam.ModulesArrayAdapter;
import com.conicskill.app.ui.exam.RecyclerViewDisabler;
import com.conicskill.app.ui.exam.SelectableAdapter;
import com.conicskill.app.ui.exam.SelectableViewHolder;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class QuestionOfDayFragment extends BaseFragment implements SelectableViewHolder.OnItemSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.textLayoutModules)
    TextInputLayout textLayoutModules;
    @BindView(R.id.modulesTextView)
    AutoCompleteTextView modulesTextView;
    @BindView(R.id.cardviewHeader)
    CardView cardviewHeader;
    @Nullable
    @BindView(R.id.cardViewFooter)
    CardView cardViewFooter;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.cardViewSolution)
    CardView cardViewSolution;

    @BindView(R.id.animationView)
    LottieAnimationView animationView;

    @BindView(R.id.answerText)
    AppCompatTextView answerText;

    @BindView(R.id.webViewSolutions)
    WebView webViewSolutions;

    @BindView(R.id.textViewTitle)
    AppCompatTextView textViewTitle;

    @BindView(R.id.textViewTimer)
    AppCompatTextView textViewTimer;

    @BindView(R.id.recyclerViewQuestions)
    RecyclerView recyclerViewQuestions;

    @BindView(R.id.buttonSubmit)
    MaterialButton buttonSubmit;

    RecyclerView recyclerViewOptions;

    OneAdapter oneAdapter;

    int current = 0;
    String language = "en", currentModuleID = "1", examTitle = "SSC";
    List<String> moduleId = new ArrayList<>();
    List<Modules> modulesList = new ArrayList<>();
    List<String> selectedOption = new ArrayList<>();
    ModulesArrayAdapter adapter;
    LanguagesArrayAdapter languagesArrayAdapter;
    int totalQuestions = 0;
    double totalMarks = 0.00, totalMarksObtained = 0.00;
    int totalAttemptedQuestions = 0;
    int totalUnAttempetedQuestions = 0;
    int totalCorrectAnswers = 0;
    int totalWrongAnswers = 0;
    private int progressMax = 0;
    private ExamViewModel mExamViewModel;
    private ProgressDialog progressOverlay;
    private HashMap<String, HashMap<String, ArrayList<Questions>>> moduleLanguageQuestionsMap = new HashMap<>();
    private MainExamTimer mainExamTimer;
    private List<Languages> languagesList = new ArrayList<>();
    private QuestionData questionData;
    List<String> selection = new ArrayList<>();


    public static QuestionOfDayFragment newInstance(QuestionData questionData) {
        if(questionData != null) {
            QuestionOfDayFragment questionOfDayFragment = new QuestionOfDayFragment();
            Bundle args = new Bundle();
            args.putSerializable("question", questionData);
            questionOfDayFragment.setArguments(args);
            return questionOfDayFragment;
        } else {
            return new QuestionOfDayFragment();
        }
    }

    public static String getAssetJsonData(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("response.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.e("data", json);
        return json;
    }

    @Override
    protected int layoutRes() {
        return R.layout.question_day_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mExamViewModel = new ViewModelProvider(this, viewModelFactory).get(ExamViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.getSerializable("question") != null) {
                questionData = (QuestionData) bundle.getSerializable("question");
            }
        }

//        observeViewModel();
        textLayoutModules.setVisibility(View.GONE);
        textViewTimer.setVisibility(View.GONE);
        examTitle = "Question of the Day";
        textViewTitle.setText(examTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        recyclerViewQuestions.setNestedScrollingEnabled(false);
        recyclerViewQuestions.setHasFixedSize(true);
        recyclerViewQuestions.setItemViewCacheSize(200);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewQuestions);

        oneAdapter = new OneAdapter(recyclerViewQuestions)
                .attachItemModule(featureItems());

        Languages languages = new Languages();
        languages.setLanguageId("en");
        languages.setLanguage("English");

        Languages languages1 = new Languages();
        languages1.setLanguageId("hi");
        languages1.setLanguage("Hindi");

        languagesList.add(languages);
        languagesList.add(languages1);

        HashMap<String, ArrayList<Questions>> languageQuestions = new HashMap<>();
        // run loops for languages
        JsonObject english = questionData.getJsonMember1().getEn().get(0).getOptions();
        JsonObject hindi = questionData.getJsonMember1().getHi().get(0).getOptions();
        questionData.getJsonMember1().getEn().get(0).setOptionsList(customOptions(english));
        questionData.getJsonMember1().getHi().get(0).setOptionsList(customOptions(hindi));
        languageQuestions.put("en", (ArrayList<Questions>) questionData.getJsonMember1().getEn());
        languageQuestions.put("hi", (ArrayList<Questions>) questionData.getJsonMember1().getHi());
        moduleLanguageQuestionsMap.put("1", languageQuestions);
        oneAdapter.setItems(Objects.requireNonNull(moduleLanguageQuestionsMap.get(currentModuleID).get(language)));

        buttonSubmit.setOnClickListener(l -> {
            if(selection.isEmpty()) {
                Toasty.error(requireContext(), "Please select an option", Toasty.LENGTH_SHORT).show();
            } else {
                buttonSubmit.setClickable(false);
                submitPopup();
            }
        });
    }

    private void observeViewModel() {
        mExamViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(getActivity(), "Sending OTP");
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

        mExamViewModel.observeModulesFromDB().observe(getViewLifecycleOwner(), modules -> {
            modulesList.addAll(modules);
            adapter = new ModulesArrayAdapter(getContext(), R.layout.dropdown_menu_popup_item, modulesList);

            modulesTextView.setAdapter(adapter);
            modulesTextView.setOnItemClickListener((AdapterView<?> parent, View view2, int position, long id) -> {
                modulesTextView.setText(modulesList.get(position).getModuleName());
                currentModuleID = modulesList.get(position).getModuleId();
                oneAdapter.setItems(moduleLanguageQuestionsMap.get(currentModuleID).get(language));
                textLayoutModules.clearFocus();
            });
            modulesTextView.setText(modulesList.get(0).getModuleName());

        });
    }

    @Override
    public void onItemSelected(SelectableOption item) {
        selectedOption.clear();
        selectedOption.add(item.getKey());
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

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        if (mainExamTimer != null) {
            mainExamTimer.cancel();
        }
        super.onDestroy();
    }

    @NotNull
    private ItemModule<Questions> featureItems() {
        return new ItemModule<Questions>() {
            private int lastPosition = -1;

            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.question_item;
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
            public void onBind(@NotNull Item<Questions> item, @NotNull ViewBinder viewBinder) {
                AutoCompleteTextView languagesTextView = viewBinder.findViewById(R.id.languagesTextView);
                CardView cardViewFooter = viewBinder.findViewById(R.id.cardViewFooter);
                cardViewFooter.setVisibility(View.GONE);
                TextInputLayout textLayoutLanguages = viewBinder.findViewById(R.id.textLayoutLanguages);
                WebView webViewDirections = viewBinder.findViewById(R.id.webViewDirections);
                AppCompatButton buttonSave = viewBinder.findViewById(R.id.buttonSave);
                buttonSave.setVisibility(View.GONE);
                AppCompatButton buttonMark = viewBinder.findViewById(R.id.buttonMark);
                buttonMark.setVisibility(View.GONE);
                AppCompatButton buttonClearResponse = viewBinder.findViewById(R.id.buttonClearResponse);
                buttonClearResponse.setVisibility(View.GONE);
                AppCompatButton buttonViewQuestions = viewBinder.findViewById(R.id.buttonViewQuestions);
                AppCompatTextView textViewPositiveMarks = viewBinder.findViewById(R.id.textViewPositiveMarks);
                AppCompatTextView textViewNegativeMarks = viewBinder.findViewById(R.id.textViewNegativeMarks);
                recyclerViewOptions = viewBinder.findViewById(R.id.recyclerViewOptions);
                AppCompatTextView textViewQuestionNumber = viewBinder.findViewById(R.id.textViewQuestionNumber);
                LinearLayout linearLayoutMarked = viewBinder.findViewById(R.id.linearLayoutMarked);
                if(selection.isEmpty()) {
                    ExamSelectableAdapter adapter = new ExamSelectableAdapter(selectedOption -> {
                        selection.clear();
                        selection.add(selectedOption.getKey());
                        saveQuestionOptions(item.getMetadata().getPosition(), selection);
                    }, item.getModel().getOptionsList(), item.getModel().getSelectedOptions(), false, null);
                    // Options recycler view in the question
                    recyclerViewOptions.setNestedScrollingEnabled(false);
                    recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
                    recyclerViewOptions.setAdapter(adapter);
                } else {
                    SelectableAdapter adapter = new SelectableAdapter(selectedOption -> {
                        selection.clear();
                        selection.add(selectedOption.getKey());
                        saveQuestionOptions(item.getMetadata().getPosition(), selection);
                    }, item.getModel().getOptionsList(), item.getModel().getSelectedOptions(), false, item.getModel().getCorrectAnswer());
                    // Options recycler view in the question
                    recyclerViewOptions.setNestedScrollingEnabled(false);
                    recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
                    recyclerViewOptions.setAdapter(adapter);
                    recyclerViewOptions.setEnabled(false);
                    recyclerViewOptions.setClickable(false);
                    RecyclerView.OnItemTouchListener disabler = new RecyclerViewDisabler();
                    recyclerViewOptions.addOnItemTouchListener(disabler);
                }
                languagesArrayAdapter = new LanguagesArrayAdapter(getContext(), R.layout.dropdown_menu_popup_item, languagesList);
                languagesTextView.setAdapter(languagesArrayAdapter);
                languagesTextView.setOnItemClickListener((AdapterView<?> parent, View view2, int position, long id) -> {
                    languagesTextView.setText(languagesList.get(position).getLanguage());
                    language = languagesList.get(position).getLanguageId();
                    oneAdapter.setItems(moduleLanguageQuestionsMap.get(currentModuleID).get(language));
                    textLayoutLanguages.clearFocus();
                });
                languagesTextView.setText(getIndexOfLanguage(language).getLanguage());
                languagesTextView.setSelection(languagesList.indexOf(getIndexOfLanguage(language)));
                webViewDirections.getSettings().setJavaScriptEnabled(true);
                webViewSolutions.getSettings().setJavaScriptEnabled(true);
                if (item.getModel().getInstructions() != null && !item.getModel().getInstructions().isEmpty()) {
                    webViewDirections.loadDataWithBaseURL("http://127.0.0.1/", item.getModel().getInstructions() + "</br></br>" + item.getModel().getQuestionStatement(),
                            "text/html", "UTF-8", null);
                } else {
                    webViewDirections.loadDataWithBaseURL("http://127.0.0.1/", item.getModel().getInstructions() + item.getModel().getQuestionStatement(),
                            "text/html", "UTF-8", null);
                }

                JsonElement jelem = new Gson().fromJson(item.getModel().getQuestionMarks(), JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();
                if (jobj.has("positiveMarks")) {
                    textViewPositiveMarks.setText("+" + jobj.get("positiveMarks").getAsString() + " ,");
                    textViewPositiveMarks.setVisibility(View.GONE);
                }

                if (jobj.has("negativeMarks")) {
                    textViewNegativeMarks.setText("-" + jobj.get("negativeMarks").getAsString());
                    textViewNegativeMarks.setVisibility(View.GONE);
                }

                String questionNumber = "Question : " + (item.getMetadata().getPosition() + 1);
                textViewQuestionNumber.setText(questionNumber);
                for (int i = 0; i < item.getModel().getOptionsList().size(); i++) {
                    for (int j = i; j < item.getModel().getSelectedOptions().size(); j++) {
                        if (item.getModel().getOptionsList().get(i).getKey().equalsIgnoreCase(item.getModel().getSelectedOptions().get(j))) {
                            item.getModel().getOptionsList().get(i).setSelected(true);
                        }
                    }
                }

                if (item.getModel().isMarkedForReview()) {
                    linearLayoutMarked.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutMarked.setVisibility(View.GONE);
                }

            }

            private void setAnimation(View viewToAnimate, int position) {
                // If the bound view wasn't previously displayed on screen, it's animated
                if (position > lastPosition) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
                    viewToAnimate.startAnimation(animation);
                    lastPosition = position;
                }
            }

        };
    }

    private String millisecondsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }

        return minutes + ":" + secs;
    }

    private void markQuestionForReview(int position, boolean isMarked) {
        for (Languages lang : languagesList) {
            moduleLanguageQuestionsMap.get(currentModuleID).get(lang.getLanguageId()).get(position).setMarkedForReview(isMarked);
        }
    }

    private void saveQuestionOptions(int position, List<String> selectedOption) {
        for (Languages lang : languagesList) {
            moduleLanguageQuestionsMap.get(currentModuleID).get(lang.getLanguageId()).get(position).setSelectedOptions(selectedOption);
        }
    }

    private Languages getIndexOfLanguage(String id) {
        for (int i = 0; i < languagesList.size(); i++) {
            if (id.equalsIgnoreCase(languagesList.get(i).getLanguageId())) {
                return languagesList.get(i);
            }
        }
        return languagesList.get(0);
    }

    private void submitPopup() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(),
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton_AlertDialog);

        View view = getLayoutInflater().inflate(R.layout.popup_submit, null);
        AppCompatTextView textViewTotalQuestions = view.findViewById(R.id.textViewTotalQuestions);
        AppCompatTextView textViewAttemptedQuestions = view.findViewById(R.id.textViewAttemptedQuestions);
        AppCompatTextView textViewUnansweredQuestions = view.findViewById(R.id.textViewUnansweredQuestions);

//        checkQuestions();
        textViewTotalQuestions.setText(String.valueOf(totalQuestions));
        textViewAttemptedQuestions.setText(String.valueOf(totalAttemptedQuestions));
        textViewUnansweredQuestions.setText(String.valueOf(totalUnAttempetedQuestions));

        view.setVisibility(View.GONE);

        builder.setTitle("Check Answer")
                .setMessage("Do you really want to check the answer ?")
                .setCancelable(false)
                .setPositiveButton("Yes", ((dialog, which) -> {
                    dialog.dismiss();
                    oneAdapter.clear();
                    oneAdapter.setItems(Objects.requireNonNull(moduleLanguageQuestionsMap.get(currentModuleID).get(language)));
                    cardViewSolution.setVisibility(View.VISIBLE);
                    recyclerViewQuestions.setClickable(false);
                    String solution = moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(0).getSolution();
                    webViewSolutions.loadDataWithBaseURL("http://127.0.0.1/", solution,
                            "text/html", "UTF-8", null);
                    if(checkCorrectAnswer(questionData.getJsonMember1().getEn().get(0).getSelectedOptions(),
                            questionData.getJsonMember1().getEn().get(0).getCorrectAnswer())) {
                        animationView.setAnimation(R.raw.success);
                        animationView.playAnimation();
                        answerText.setText(R.string.success_question_of_day);
                    } else {
                        animationView.setAnimation(R.raw.failed);
                        animationView.playAnimation();
                        answerText.setText(R.string.error_question_of_day);
                    }
                    scrollProgrammatically();
                }))
                .setNegativeButton("No", ((dialog, which) -> {
                    dialog.dismiss();
                    buttonSubmit.setClickable(true);
                }));

        builder.create().show();
    }

    private void marksPopup() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(),
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton_AlertDialog);


        builder.setTitle("")
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("Submit", ((dialog, which) -> {

                }))
                .setNegativeButton("Cancel", ((dialog, which) -> {

                }));

        builder.create().show();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link } of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        if (mainExamTimer == null) {
            mainExamTimer = new MainExamTimer(0, progressMax, 1000);
            mainExamTimer.start();
        }
        super.onResume();
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link } of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        if (mainExamTimer != null) {
            mainExamTimer.cancel();
            mainExamTimer = null;
        }
        super.onPause();
    }

    /**
     * @details MainExamTimer() class is used to perform the following tasks:
     */
    private class MainExamTimer extends CountDownTimer {

        public MainExamTimer(int type, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            millisInFuture = progressMax;
        }

        @Override
        public void onTick(final long l) {
            getActivity().runOnUiThread(() -> {
                progressMax = (int) l;
                textViewTimer.setText(millisecondsToTime(l));
            });
        }

        @Override
        public void onFinish() {

        }
    }

    private boolean checkCorrectAnswer(List<String> selectedOption, String correctAnswer) {
        String correctAnswers = correctAnswer.substring(2,3);
        if(correctAnswers.equalsIgnoreCase("A") || correctAnswers.equalsIgnoreCase("1")) {
            if(selectedOption.contains("A")) {
                return true;
            } else {
                return false;
            }
        } else if(correctAnswers.equalsIgnoreCase("B") || correctAnswers.equalsIgnoreCase("2")) {
            if(selectedOption.contains("B")) {
                return true;
            } else {
                return false;
            }
        } else if(correctAnswers.equalsIgnoreCase("C") || correctAnswers.equalsIgnoreCase("3")) {
            if (selectedOption.contains("C")) {
                return true;
            } else {
                return false;
            }
        } else if(correctAnswers.equalsIgnoreCase("D") || correctAnswers.equalsIgnoreCase("4")) {
            if (selectedOption.contains("D")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void checkQuestions() {
        totalUnAttempetedQuestions = 0;
        totalAttemptedQuestions = 0;
        totalMarksObtained = 0.0;
        totalQuestions = 0;
        totalMarks = 0.0;
        for (int i = 0; i < modulesList.size(); i++) {
            totalQuestions += moduleLanguageQuestionsMap.get(modulesList.get(i).getModuleId()).get(languagesList.get(0).getLanguageId()).size();
            ArrayList<Questions> questionsArrayList = moduleLanguageQuestionsMap.get(modulesList.get(i).getModuleId()).get(languagesList.get(0).getLanguageId());
            for (int j = 0; j < questionsArrayList.size(); j++) {
                JsonElement jelem = new Gson().fromJson(questionsArrayList.get(j).getQuestionMarks(), JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();
                if (jobj.has("positiveMarks")) {
                    totalMarks += Double.valueOf(jobj.get("positiveMarks").getAsString());
                }

                if (questionsArrayList.get(j).getSelectedOptions().size() != 0) {
                    totalAttemptedQuestions++;
                    /*if (checkCorrectAnswer(questionsArrayList.get(j).getSelectedOptions(), questionsArrayList.get(j).getCorrectAnswer())) {
                        totalCorrectAnswers++;
                        totalMarksObtained += Double.valueOf(jobj.get("positiveMarks").getAsString());
                    } else {
                        totalWrongAnswers++;
                        totalMarksObtained -= Double.valueOf(jobj.get("negativeMarks").getAsString());
                    }*/
                } else {
                    totalUnAttempetedQuestions++;
                }
            }
        }
    }

    private void scrollProgrammatically() {
        nestedScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int scrollViewHeight = nestedScrollView.getHeight();
                if (scrollViewHeight > 0) {
                    nestedScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    final View lastView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                    final int lastViewBottom = lastView.getBottom() + nestedScrollView.getPaddingBottom();
                    final int deltaScrollY = lastViewBottom - scrollViewHeight - nestedScrollView.getScrollY();
                    /* If you want to see the scroll animation, call this. */
                    nestedScrollView.smoothScrollBy(0, deltaScrollY);
                    /* If you don't want, call this. */
                    nestedScrollView.scrollBy(0, deltaScrollY);
                }
            }
        });
    }

    private void disableTouch() {
        recyclerViewOptions.setNestedScrollingEnabled(false);
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
        recyclerViewOptions.setEnabled(false);
        recyclerViewOptions.setClickable(false);
        RecyclerView.OnItemTouchListener disabler = new RecyclerViewDisabler();
        recyclerViewOptions.addOnItemTouchListener(disabler);
    }

}
