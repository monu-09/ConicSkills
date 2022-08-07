package com.conicskill.app.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;


public class PrivacyPolicyFragment extends BaseFragment {


    public PrivacyPolicyFragment() {
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_privacy_policy;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}