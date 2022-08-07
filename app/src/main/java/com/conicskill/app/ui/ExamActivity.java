package com.conicskill.app.ui;

import android.os.Bundle;
import android.view.WindowManager;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.exam.ExamFragment;
import com.conicskill.app.ui.exam.ExamSwipeableFragment;
import com.conicskill.app.ui.exam.ExamViewModel;
import com.conicskill.app.ui.exam.ResultFragment;
import com.conicskill.app.ui.exam.SolutionsFragment;
import com.conicskill.app.util.Constants;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;

public class ExamActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    private ExamViewModel mExamViewModel;

    @Override
    protected int layoutRes() {
        return R.layout.exam_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExamViewModel = new ViewModelProvider(this, viewModelFactory).get(ExamViewModel.class);
        mExamViewModel.setExamTitle(getIntent().getStringExtra("examTitle"));
        mExamViewModel.fetchModules(getIntent().getStringArrayListExtra("modules"));
        if (savedInstanceState == null) {
            if(getIntent().getStringExtra("redirectTo").equalsIgnoreCase(Constants.EXAM_INSTRUCTIONS)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ExamSwipeableFragment.newInstance())
                        .commitNow();
            } else if (getIntent().getStringExtra("redirectTo").equalsIgnoreCase(Constants.EXAM_SOLUTIONS)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ResultFragment.newInstance(getIntent().getStringExtra("authToken")))
                        .commitNow();
            }
        }
    }
}
