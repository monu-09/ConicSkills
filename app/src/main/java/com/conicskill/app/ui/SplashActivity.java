package com.conicskill.app.ui;


import android.os.Bundle;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.ui.splash.SplashFragment;

public class SplashActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.splash_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, SplashFragment.newInstance()).commitNow();
        }
    }
}
