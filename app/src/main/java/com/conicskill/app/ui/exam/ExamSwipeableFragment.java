package com.conicskill.app.ui.exam;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.SelectableOption;
import com.conicskill.app.data.model.exam.Languages;
import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.exam.Options;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.data.model.saveTest.CandidateResponseItem;
import com.conicskill.app.data.model.saveTest.QuestionListItem;
import com.conicskill.app.data.model.saveTest.SaveTestData;
import com.conicskill.app.data.model.saveTest.SaveTestRequest;
import com.conicskill.app.data.model.saveTest.TestObject;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class ExamSwipeableFragment extends BaseFragment implements SelectableViewHolder.OnItemSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.textLayoutModules)
    TextInputLayout textLayoutModules;
    @BindView(R.id.modulesTextView)
    AutoCompleteTextView modulesTextView;

    @BindView(R.id.textViewTitle)
    AppCompatTextView textViewTitle;

    @BindView(R.id.textViewTimer)
    AppCompatTextView textViewTimer;

    @BindView(R.id.recyclerViewQuestions)
    RecyclerView recyclerViewQuestions;

    @BindView(R.id.buttonSubmit)
    MaterialButton buttonSubmit;

    OneAdapter oneAdapter;

    int current = 0;
    String language = "en", currentModuleID = "1", examTitle = "SSC", authToken;
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
    private final HashMap<String, HashMap<String, ArrayList<Questions>>> moduleLanguageQuestionsMap = new HashMap<>();
    private MainExamTimer mainExamTimer;
    private final List<Languages> languagesList = new ArrayList<>();
    private BottomSheetDialog bottomSheetViewQuestions;
    private final HashMap<String, Integer> moduleQuestionIdMapping = new HashMap<>();

    public static ExamSwipeableFragment newInstance() {
        return new ExamSwipeableFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.exam_swipeable_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mExamViewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(ExamViewModel.class);

        observeViewModel();
        bottomSheetViewQuestions = new BottomSheetDialog(getActivity());
        moduleId = getActivity().getIntent().getStringArrayListExtra("modules");
        examTitle = getActivity().getIntent().getStringExtra("examTitle");
        mExamViewModel.setExamTitle(examTitle);
        authToken = getActivity().getIntent().getStringExtra("authToken");
        progressMax = Integer.parseInt(requireActivity().getIntent().getStringExtra("duration")) * 60 * 1000;

        textViewTitle.setText(examTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        recyclerViewQuestions.setNestedScrollingEnabled(false);
        recyclerViewQuestions.setHasFixedSize(true);
        recyclerViewQuestions.setItemViewCacheSize(200);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewQuestions);

        oneAdapter = new OneAdapter(recyclerViewQuestions)
                .attachItemModule(featureItems());

        mainExamTimer = new MainExamTimer(0, progressMax, 1000);
        mainExamTimer.start();

        buttonSubmit.setOnClickListener(l -> {
            submitPopup();
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
            Languages languages = new Languages();
            languages.setLanguageId("en");
            languages.setLanguage("English");

            Languages languages1 = new Languages();
            languages1.setLanguageId("hi");
            languages1.setLanguage("Hindi");

            languagesList.add(languages);
            languagesList.add(languages1);

            for (int i = 0, k = 0; i < modules.size(); i++) {
                boolean questionIteratedForOneLanguage = false;
                JsonElement jsonElement = new Gson().fromJson(modules.get(i).getQuestionsData(), JsonElement.class);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String moduleIDLocal = modules.get(i).getModuleId();
                HashMap<String, ArrayList<Questions>> languageQuestions = new HashMap<>();
                if (i == 0) {
                    moduleQuestionIdMapping.put(modules.get(i).getModuleId(), 0);
                } else {
                    moduleQuestionIdMapping.put(modules.get(i).getModuleId(), k);
                }
                // run loops for languages
                if (jsonObject.has("en")) {
                    questionIteratedForOneLanguage = true;
                    Type listType = new TypeToken<ArrayList<Questions>>() {
                    }.getType();
                    ArrayList<Questions> questions = new Gson().fromJson(jsonObject.getAsJsonArray("en"), listType);


                    for (int j = 0; j < questions.size(); j++) {
                        k++;
                        questions.get(j).setOptionsList(customOptions(questions.get(j).getOptions()));
                    }
                    languageQuestions.put("en", questions);
                }

                if (jsonObject.has("hi")) {
                    if (questionIteratedForOneLanguage) {
                        questionIteratedForOneLanguage = false;
                    }
                    Type listType = new TypeToken<ArrayList<Questions>>() {
                    }.getType();
                    ArrayList<Questions> questions = new Gson().fromJson(jsonObject.getAsJsonArray("hi"), listType);


                    for (int j = 0; j < questions.size(); j++) {
                        questions.get(j).setOptionsList(customOptions(questions.get(j).getOptions()));
                    }
                    languageQuestions.put("hi", questions);
                }
                moduleLanguageQuestionsMap.put(moduleIDLocal, languageQuestions);
                questionIteratedForOneLanguage = false;
            }
            currentModuleID = modules.get(0).getModuleId();
            oneAdapter.setItems(moduleLanguageQuestionsMap.get(currentModuleID).get(language));
            mExamViewModel.saveModuleQuestions(moduleQuestionIdMapping);
        });

        mExamViewModel.observeSaveTestRequest().observe(getViewLifecycleOwner(), saveTestRequestResponse -> {
            if (saveTestRequestResponse != null) {
                if (saveTestRequestResponse.body() != null) {
                    if (saveTestRequestResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, ResultFragment.newInstance(authToken))
                                .commitNow();
                    }
                }
            }
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
                TextInputLayout textLayoutLanguages = viewBinder.findViewById(R.id.textLayoutLanguages);
                WebView webViewDirections = viewBinder.findViewById(R.id.webViewDirections);
                AppCompatButton buttonSave = viewBinder.findViewById(R.id.buttonSave);
                AppCompatButton buttonMark = viewBinder.findViewById(R.id.buttonMark);
                AppCompatButton buttonClearResponse = viewBinder.findViewById(R.id.buttonClearResponse);
                AppCompatButton buttonViewQuestions = viewBinder.findViewById(R.id.buttonViewQuestions);
                AppCompatTextView textViewPositiveMarks = viewBinder.findViewById(R.id.textViewPositiveMarks);
                AppCompatTextView textViewNegativeMarks = viewBinder.findViewById(R.id.textViewNegativeMarks);
                RecyclerView recyclerViewOptions = viewBinder.findViewById(R.id.recyclerViewOptions);
                AppCompatTextView textViewQuestionNumber = viewBinder.findViewById(R.id.textViewQuestionNumber);
                LinearLayout linearLayoutMarked = viewBinder.findViewById(R.id.linearLayoutMarked);
                webViewDirections.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                webViewDirections.setLongClickable(false);
                List<String> selection = new ArrayList<>();
                ExamSelectableAdapter adapter = new ExamSelectableAdapter(selectedOption -> {
                    selection.clear();
                    selection.add(selectedOption.getKey());
                    saveQuestionOptions(item.getMetadata().getPosition(), selection);
                }, item.getModel().getOptionsList(), item.getModel().getSelectedOptions(), false, "");


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
                if (item.getModel().getInstructions() != null && !item.getModel().getInstructions().isEmpty()) {
                    webViewDirections.loadDataWithBaseURL(null, item.getModel().getInstructions() + "</br></br>" + item.getModel().getQuestionStatement(),
                            "text/html", "UTF-8", null);
                } else {
                    webViewDirections.loadDataWithBaseURL(null, item.getModel().getInstructions() + item.getModel().getQuestionStatement(),
                            "text/html", "UTF-8", null);
                }

                JsonElement jelem = new Gson().fromJson(item.getModel().getQuestionMarks(), JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();
                if (jobj.has("positiveMarks")) {
                    textViewPositiveMarks.setText("+" + jobj.get("positiveMarks").getAsString() + " ,");
                }

                if (jobj.has("negativeMarks")) {
                    textViewNegativeMarks.setText("" + jobj.get("negativeMarks").getAsString());
                }

                String questionNumber = "Q: " + (moduleQuestionIdMapping.get(currentModuleID) + (item.getMetadata().getPosition() + 1));
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

                buttonMark.setOnClickListener(l -> {
                    if (item.getModel().isMarkedForReview()) {
                        item.getModel().setMarkedForReview(false);
                        saveQuestionOptions(item.getMetadata().getPosition(), item.getModel().getSelectedOptions());
                        markQuestionForReview(item.getMetadata().getPosition(), false);
                        linearLayoutMarked.setVisibility(View.GONE);
                        Toasty.info(getContext(), "Question removed from review", Toasty.LENGTH_SHORT).show();
                    } else {
                        if (adapter.getSelectedKeys().size() == 0) {
                            Toasty.info(getContext(), "Please select an option first", Toasty.LENGTH_SHORT).show();
                        } else {
                            item.getModel().setMarkedForReview(true);
                            saveQuestionOptions(item.getMetadata().getPosition(), item.getModel().getSelectedOptions());
                            markQuestionForReview(item.getMetadata().getPosition(), true);
                            linearLayoutMarked.setVisibility(View.VISIBLE);
                            Toasty.info(getContext(), "Question marked for review", Toasty.LENGTH_SHORT).show();
                        }
                    }
                });

                // Button to clear the response of the questions
                buttonClearResponse.setOnClickListener(l -> {
                    adapter.clearOptions();
                    adapter.notifyDataSetChanged();
                    saveQuestionOptions(item.getMetadata().getPosition(), adapter.getSelectedKeys());
                    Toasty.info(getContext(), "Response Cleared", Toasty.LENGTH_SHORT).show();
                });

                // Button save data and scroll to the next question in the list
                buttonSave.setOnClickListener(l -> {
                    item.getModel().setSelectedOptions(adapter.getSelectedKeys());
                    saveQuestionOptions(item.getMetadata().getPosition(), adapter.getSelectedKeys());
                    if (item.getMetadata().getPosition() < oneAdapter.getItemCount()) {
                        recyclerViewQuestions.scrollToPosition(item.getMetadata().getPosition() + 1);
                    }
                });

                // Button to view questions
                buttonViewQuestions.setOnClickListener(l -> {
                    setViewQuestions();
                });

                // Options recycler view in the question
                recyclerViewOptions.setNestedScrollingEnabled(false);
                recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
                recyclerViewOptions.setAdapter(adapter);

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

        checkQuestions();
        textViewTotalQuestions.setText(String.valueOf(totalQuestions));
        textViewAttemptedQuestions.setText(String.valueOf(totalAttemptedQuestions));
        textViewUnansweredQuestions.setText(String.valueOf(totalUnAttempetedQuestions));

        builder.setTitle("Submit Paper")
                .setMessage("Do you really want to submit the paper ?")
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Submit", ((dialog, which) -> {
                    mExamViewModel.setModulesData(moduleLanguageQuestionsMap, modulesList);
                    callApiToSaveTestObject();
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

    private boolean checkCorrectAnswer(List<String> selectedOption, String correctAnswer) {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {
        };
        List<String> animals = new Gson().fromJson(correctAnswer, token.getType());
        return animals.containsAll(selectedOption);
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
                    totalMarks += Double.parseDouble(jobj.get("positiveMarks").getAsString());
                }

                if (questionsArrayList.get(j).getSelectedOptions().size() != 0) {
                    totalAttemptedQuestions++;
                    if (checkCorrectAnswer(questionsArrayList.get(j).getSelectedOptions(), questionsArrayList.get(j).getCorrectAnswer())) {
                        totalCorrectAnswers++;
                        totalMarksObtained += Double.parseDouble(jobj.get("positiveMarks").getAsString());
                    } else {
                        totalWrongAnswers++;
                        totalMarksObtained -= Double.parseDouble(jobj.get("negativeMarks").getAsString());
                    }
                } else {
                    totalUnAttempetedQuestions++;
                }
            }
        }
    }

    public void callApiToSaveTestObject() {
        ArrayList<CandidateResponseItem> candidateResponseItems = new ArrayList<>();
        for (int i = 0; i < modulesList.size(); i++) {
            CandidateResponseItem candidateResponseItem = new CandidateResponseItem();
            candidateResponseItem.setModuleId(modulesList.get(i).getModuleId());
            candidateResponseItem.setModuleName(modulesList.get(i).getModuleName());
            ArrayList<QuestionListItem> questionListItems = new ArrayList<>();
            ArrayList<Questions> questionsList = moduleLanguageQuestionsMap.get(modulesList.get(i).getModuleId()).get(languagesList.get(0).getLanguageId());
            HashMap<String, ArrayList<Questions>> questions = moduleLanguageQuestionsMap.get(modulesList.get(i).getModuleId());
            String questionData = new Gson().toJson(questions);
            mExamViewModel.saveValues(questionData, modulesList.get(i).getModuleId());
            for (int j = 0; j < questionsList.size(); j++) {
                QuestionListItem questionListItem = new QuestionListItem();
                questionListItem.setAnswerMarked(questionsList.get(j).getSelectedOptions());
                questionListItem.setQuestionId(questionsList.get(j).getQuestionId());
                questionListItems.add(questionListItem);
            }

            candidateResponseItem.setQuestionList(questionListItems);
            candidateResponseItems.add(candidateResponseItem);
        }

        TestObject testObject = new TestObject();
        testObject.setCandidateResponse(candidateResponseItems);
        testObject.setStartTime("yyyy-dd-MM HH:mm:ss");
        testObject.setEndTime("yyyy-dd-MM HH:mm:ss");
        testObject.setFeedbackData(null);
        testObject.setIsCompleted(6);
        testObject.setRegistrationData(null);

        SaveTestData saveTestData = new SaveTestData();
        saveTestData.setTestObject(testObject);
        saveTestData.setTestObjVersion("2");

        SaveTestRequest saveTestRequest = new SaveTestRequest();
        saveTestRequest.setData(saveTestData);
        saveTestRequest.setLanguage(languagesList.get(0).getLanguageId());
        saveTestRequest.setToken(authToken);

        mExamViewModel.callSaveTestObjectAPI(saveTestRequest);
    }

    public void setViewQuestions() {
        bottomSheetViewQuestions.setContentView(R.layout.layout_view_questions);
        bottomSheetViewQuestions.setCanceledOnTouchOutside(false);

        ChipGroup chipGroupQuestions = bottomSheetViewQuestions.findViewById(R.id.chipGroupQuestions);
        AppCompatTextView textViewModule = bottomSheetViewQuestions.findViewById(R.id.textViewModule);
        for (int i = 0; i < modulesList.size(); i++) {
            if (currentModuleID.equalsIgnoreCase(modulesList.get(i).getModuleId())) {
                textViewModule.setText(modulesList.get(i).getModuleName());
            }
        }
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (int i = 0, k = 1; i < modulesList.size(); i++) {
        ArrayList<Questions> questionsList = moduleLanguageQuestionsMap.get(currentModuleID).get(language);
        Chip chip;
        for (int j = 0; j < questionsList.size(); j++/*, k++*/) {
            if (questionsList.get(j).getSelectedOptions().size() == 0) {
                chip = (Chip) layoutInflater.inflate(R.layout.layout_unattempted_view, null);
            } else if (questionsList.get(j).isMarkedForReview()) {
                chip = (Chip) layoutInflater.inflate(R.layout.layout_marked_for_review, null);
            } else {
                chip = (Chip) layoutInflater.inflate(R.layout.layout_attempted_view, null);
            }
            chip.setId(Integer.parseInt(questionsList.get(j).getQuestionId()));
            if (j < 9 && (moduleQuestionIdMapping.get(currentModuleID) == 0)) {
                chip.setText("0" + (moduleQuestionIdMapping.get(currentModuleID) + (j + 1)));
            } else {
                chip.setText(String.valueOf((moduleQuestionIdMapping.get(currentModuleID) + (j + 1))));
            }
            int finalJ = j;
            chip.setOnClickListener(l -> {
//                    if(modulesList.get(finalI).getModuleId().equalsIgnoreCase(currentModuleID)) {
                recyclerViewQuestions.scrollToPosition(finalJ);
                bottomSheetViewQuestions.dismiss();
//                    }
            });
            chipGroupQuestions.addView(chip);
        }
//        }
        bottomSheetViewQuestions.show();
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
            mExamViewModel.setModulesData(moduleLanguageQuestionsMap, modulesList);
            callApiToSaveTestObject();
        }
    }

}
