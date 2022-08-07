package com.conicskill.app.ui.exam;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.airbnb.lottie.LottieAnimationView;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.SelectableOption;
import com.conicskill.app.data.model.exam.Languages;
import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.di.util.ViewModelFactory;
import com.google.android.material.button.MaterialButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;

public class SolutionsFragment extends BaseFragment implements SelectableViewHolder.OnItemSelectedListener {

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
    String language = "en", currentModuleID = "1", authToken;
    List<Modules> modulesList = new ArrayList<>();
    List<String> selectedOption = new ArrayList<>();
    ModulesArrayAdapter adapter;
    LanguagesArrayAdapter languagesArrayAdapter;
    private ExamViewModel mExamViewModel;
    private ProgressDialog progressOverlay;
    private HashMap<String, HashMap<String, ArrayList<Questions>>> moduleLanguageQuestionsMap = new HashMap<>();
    private List<Languages> languagesList = new ArrayList<>();

    public static SolutionsFragment newInstance() {
        return new SolutionsFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.exam_swipeable_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mExamViewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(ExamViewModel.class);
//        observeViewModel();
        mExamViewModel.setExamTitle(mExamViewModel.getExamTitle());
        modulesList = mExamViewModel.getModulesList();

        textViewTitle.setText(mExamViewModel.getExamTitle());
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
        textViewTimer.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.GONE);
        adapter = new ModulesArrayAdapter(getContext(), R.layout.dropdown_menu_popup_item, modulesList);

        modulesTextView.setAdapter(adapter);
        modulesTextView.setOnItemClickListener((AdapterView<?> parent, View view2, int position, long id) -> {
            modulesTextView.setText(modulesList.get(position).getModuleName());
            currentModuleID = modulesList.get(position).getModuleId();
            oneAdapter.setItems(Objects.requireNonNull(mExamViewModel.getModulesData().get(currentModuleID).get(language)));
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
        currentModuleID = modulesList.get(0).getModuleId();
        oneAdapter.setItems(Objects.requireNonNull(mExamViewModel.getModulesData().get(currentModuleID).get(language)));
    }

    @Override
    public void onItemSelected(SelectableOption item) {
        selectedOption.clear();
        selectedOption.add(item.getKey());
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
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
                CardView cardViewFooter = viewBinder.findViewById(R.id.cardViewFooter);
                AppCompatButton buttonSave = viewBinder.findViewById(R.id.buttonSave);
                AppCompatButton buttonMark = viewBinder.findViewById(R.id.buttonMark);
                AppCompatButton buttonClearResponse = viewBinder.findViewById(R.id.buttonClearResponse);
                AppCompatButton buttonViewQuestions = viewBinder.findViewById(R.id.buttonViewQuestions);
                AppCompatTextView textViewPositiveMarks = viewBinder.findViewById(R.id.textViewPositiveMarks);
                AppCompatTextView textViewNegativeMarks = viewBinder.findViewById(R.id.textViewNegativeMarks);
                RecyclerView recyclerViewOptions = viewBinder.findViewById(R.id.recyclerViewOptions);
                AppCompatTextView textViewQuestionNumber = viewBinder.findViewById(R.id.textViewQuestionNumber);
                LinearLayout linearLayoutMarked = viewBinder.findViewById(R.id.linearLayoutMarked);
                LottieAnimationView animationView = viewBinder.findViewById(R.id.animationView);
                CardView cardViewSolution = viewBinder.findViewById(R.id.cardViewSolution);
                TextView answerText = viewBinder.findViewById(R.id.answerText);
                WebView webViewSolutions = viewBinder.findViewById(R.id.webViewSolutions);
                cardViewSolution.setVisibility(View.VISIBLE);
                cardViewFooter.setVisibility(View.GONE);
                SelectableAdapter adapter = new SelectableAdapter(selectableOption->{

                }, item.getModel().getOptionsList(), item.getModel().getSelectedOptions(),
                        false, item.getModel().getCorrectAnswer());

                languagesArrayAdapter = new LanguagesArrayAdapter(getContext(), R.layout.dropdown_menu_popup_item, languagesList);
                languagesTextView.setAdapter(languagesArrayAdapter);
                languagesTextView.setOnItemClickListener((AdapterView<?> parent, View view2, int position, long id) -> {
                    languagesTextView.setText(languagesList.get(position).getLanguage());
                    language = languagesList.get(position).getLanguageId();
                    oneAdapter.setItems(mExamViewModel.getModulesData().get(currentModuleID).get(language));
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

                String questionNumber = "Q: " + (mExamViewModel.moduleQuestionIdMapping.get(currentModuleID) + (item.getMetadata().getPosition() + 1));
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

                // Options recycler view in the question
                recyclerViewOptions.setNestedScrollingEnabled(false);
                recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
                recyclerViewOptions.setAdapter(adapter);
                recyclerViewOptions.setEnabled(false);
                recyclerViewOptions.setClickable(false);
                RecyclerView.OnItemTouchListener disabler = new RecyclerViewDisabler();
                recyclerViewOptions.addOnItemTouchListener(disabler);

                webViewSolutions.loadDataWithBaseURL(null, item.getModel().getSolution(),
                        "text/html", "UTF-8", null);
                if(item.getModel().getSelectedOptions().size() != 0) {
                    if (checkCorrectAnswer(item.getModel().getSelectedOptions(), item.getModel().getCorrectAnswer())) {
                        animationView.setAnimation(R.raw.success);
                        animationView.setRepeatCount(2);
                        animationView.playAnimation();
                        answerText.setText(R.string.success_question_of_day);
                    } else {
                        animationView.setAnimation(R.raw.failed);
                        animationView.setRepeatCount(2);
                        animationView.playAnimation();
                        answerText.setText(R.string.error_question_of_day);
                    }
                } else {
                    animationView.setAnimation(R.raw.empty);
                    animationView.setRepeatCount(2);
                    animationView.playAnimation();
                    answerText.setText(R.string.unattempted_question);
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

    private Languages getIndexOfLanguage(String id) {
        for (int i = 0; i < languagesList.size(); i++) {
            if (id.equalsIgnoreCase(languagesList.get(i).getLanguageId())) {
                return languagesList.get(i);
            }
        }
        return languagesList.get(0);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link } of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        /*if (mainExamTimer == null) {
            mainExamTimer = new MainExamTimer(0, progressMax, 1000);
            mainExamTimer.start();
        }*/
        super.onResume();
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link } of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        /*if (mainExamTimer != null) {
            mainExamTimer.cancel();
            mainExamTimer = null;
        }*/
        super.onPause();
    }

    private boolean checkCorrectAnswer(List<String> selectedOption, String correctAnswer) {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        List<String> animals = new Gson().fromJson(correctAnswer, token.getType());
        return animals.containsAll(selectedOption);
    }


}
