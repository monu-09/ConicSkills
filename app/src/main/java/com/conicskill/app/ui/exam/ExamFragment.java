package com.conicskill.app.ui.exam;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.SelectableOption;
import com.conicskill.app.data.model.exam.ModuleBodyWrapper;
import com.conicskill.app.data.model.exam.ModuleRequest;
import com.conicskill.app.data.model.exam.ModuleRequestData;
import com.conicskill.app.data.model.exam.Options;
import com.conicskill.app.data.model.exam.Questions;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class ExamFragment extends BaseFragment implements SelectableViewHolder.OnItemSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.textLayoutModules)
    TextInputLayout textLayoutModules;
    @BindView(R.id.modulesTextView)
    AutoCompleteTextView modulesTextView;
    @BindView(R.id.webViewDirections)
    WebView webViewDirections;
    @BindView(R.id.webViewQuestions)
    WebView webViewQuestions;
    @BindView(R.id.recyclerViewOptions)
    RecyclerView recyclerViewOptions;
    @BindView(R.id.buttonSave)
    AppCompatButton buttonSave;

    @BindView(R.id.textViewQuestionNumber)
    AppCompatTextView textViewQuestionNumber;

    @BindView(R.id.textViewTimer)
    AppCompatTextView textViewTimer;

    @BindView(R.id.textViewPositiveMarks)
    AppCompatTextView textViewPositiveMarks;

    @BindView(R.id.textViewNegativeMarks)
    AppCompatTextView textViewNegativeMarks;

    @BindView(R.id.buttonMark)
    AppCompatButton buttonMark;

    @BindView(R.id.buttonClearResponse)
    AppCompatButton buttonClearResponse;

    @BindView(R.id.buttonViewQuestions)
    AppCompatButton buttonViewQuestions;

    int current = 0;
    String language = "en", currentModuleID = "1";
    List<String> moduleId = new ArrayList<>();
    private int progressMax = 1800000;
    private ExamViewModel mExamViewModel;
    private ProgressDialog progressOverlay;
    private HashMap<String, HashMap<String, ArrayList<Questions>>> moduleLanguageQuestionsMap = new HashMap<>();
    private MainExamTimer mainExamTimer;
    List<String> selectedOption = new ArrayList<>();

    public static ExamFragment newInstance() {
        return new ExamFragment();
    }

    public static String getAssetJsonData(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("response.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.e("data", json);
        return json;
    }

    @Override
    protected int layoutRes() {
        return R.layout.exam_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mExamViewModel = new ViewModelProvider(this, viewModelFactory).get(ExamViewModel.class);
        webViewDirections.getSettings().setJavaScriptEnabled(true);

        String[] COUNTRIES = new String[]{"Basic Maths"};

        observeViewModel();

        moduleId = getActivity().getIntent().getStringArrayListExtra("modules");
        mExamViewModel.fetchModules(moduleId);
        currentModuleID = moduleId.get(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item,
                COUNTRIES);

        modulesTextView.setAdapter(adapter);
        modulesTextView.setOnItemClickListener((AdapterView<?> parent, View view2, int position, long id) -> {
            textLayoutModules.clearFocus();
        });
        modulesTextView.setText(COUNTRIES[0]);

        buttonSave.setOnClickListener(l -> {
            if (moduleLanguageQuestionsMap.get(currentModuleID).get(language).size() != 0 && current < moduleLanguageQuestionsMap.get(currentModuleID).get(language).size()-1) {
                current++;
                moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(current).setSelectedOptions(selectedOption);
                selectedOption.clear();
                loadQuestions(moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(current));
            }
        });

        buttonMark.setOnClickListener(l -> {
            if (moduleLanguageQuestionsMap.get(currentModuleID).get(language).size() != 0
                    && current < moduleLanguageQuestionsMap.get(currentModuleID).get(language).size()) {
                if(selectedOption.size() != 0) {
                    moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(current).setMarkedForReview(true);
                    Toasty.success(getContext(), "Question Marked For Review", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(getContext(), "Select an option first", Toasty.LENGTH_SHORT).show();
                }
            }
        });

        buttonClearResponse.setOnClickListener(l -> {
//            if (moduleLanguageQuestionsMap.get(currentModuleID).get(language).size() != 0 && current < moduleLanguageQuestionsMap.get(currentModuleID).get(language).size()) {
                selectedOption.clear();
                recyclerViewOptions.setAdapter(new SelectableAdapter(this, moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(current).getOptionsList(),
                        moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(current).getSelectedOptions(),false, ""));
                Toasty.info(getContext(),"Response Cleared", Toasty.LENGTH_SHORT).show();
//            }
        });

        mainExamTimer = new MainExamTimer(0, progressMax, 1000);
        mainExamTimer.start();
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
            for (int i = 0; i < modules.size(); i++) {
                JsonElement jsonElement = new Gson().fromJson(modules.get(i).getQuestionsData(), JsonElement.class);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String moduleIDLocal = modules.get(i).getModuleId();
                HashMap<String, ArrayList<Questions>> languageQuestions = new HashMap<>();
                // run loops for languages
                if (jsonObject.has("en")) {
                    Type listType = new TypeToken<ArrayList<Questions>>() {
                    }.getType();
                    ArrayList<Questions> questions = new Gson().fromJson(jsonObject.getAsJsonArray("en"), listType);


                    for (int j = 0; j < questions.size(); j++) {
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
            currentModuleID = modules.get(0).getModuleId();
            loadQuestions(moduleLanguageQuestionsMap.get(currentModuleID).get(language).get(current));
        });

        mExamViewModel.returnLoginResponse().observe(getViewLifecycleOwner(), loginResponseResponse -> {
            if (loginResponseResponse != null) {
                if (loginResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                    if (loginResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                        String allowedModules = loginResponseResponse.body().getData().getTestData().getAllowedModules();
                        String[] arrayModules = allowedModules.split(",");

                        moduleId = Arrays.asList(arrayModules);
                        ModuleRequestData moduleRequestData = new ModuleRequestData();
                        moduleRequestData.setModuleId(moduleId);
                        moduleRequestData.setToken(loginResponseResponse.body().getData().getAuthToken());

                        ModuleRequest moduleRequest = new ModuleRequest();
                        moduleRequest.setModuleRequestData(moduleRequestData);
                        moduleRequest.setToken(loginResponseResponse.body().getData().getAuthToken());

                        ModuleBodyWrapper moduleBodyWrapper = new ModuleBodyWrapper();
                        moduleBodyWrapper.setModuleRequest(moduleRequest);
                        mExamViewModel.fetchModuleQuestions(moduleRequest);
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

    private void loadQuestions(Questions question) {
        webViewDirections.loadDataWithBaseURL("http://127.0.0.1/", question.getInstructions() + question.getQuestionStatement(),
                "text/html", "UTF-8", null);

        JsonElement jelem = new Gson().fromJson(question.getQuestionMarks(), JsonElement.class);
        JsonObject jobj = jelem.getAsJsonObject();
        if (jobj.has("positiveMarks")) {
            textViewPositiveMarks.setText("+" + jobj.get("positiveMarks").getAsString() + " ,");
        }

        if (jobj.has("negativeMarks")) {
            textViewNegativeMarks.setText(jobj.get("negativeMarks").getAsString());
        }

        textViewQuestionNumber.setText("Question : " + (current + 1));
        for(int i = 0; i< question.getOptionsList().size();i++) {
            for(int j =i; j<question.getSelectedOptions().size();j++) {
                if(question.getOptionsList().get(i).getKey().equalsIgnoreCase(question.getSelectedOptions().get(j))){
                    question.getOptionsList().get(i).setSelected(true);
                }
            }
        }

        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
        recyclerViewOptions.setAdapter(new SelectableAdapter(this::onItemSelected, question.getOptionsList(), question.getSelectedOptions(),false, ""));
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
                progressMax--;
                int minutes = ((progressMax/1000) % 3600) / 60;
                int seconds = progressMax % 60;
                Log.e("Timer", "onTick: " + progressMax + " -> " + minutes + ":" + seconds);
                textViewTimer.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
            });
        }

        @Override
        public void onFinish() {

        }
    }
}
