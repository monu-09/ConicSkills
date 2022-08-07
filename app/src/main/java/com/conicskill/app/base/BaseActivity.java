package com.conicskill.app.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * @author vikas@cprep.in
 *
 * @use This class is used for creating the base Activity which extends the DaggerAppCompatActivity
 * in order to the create the dependency injection based on the general dependencies that we have to
 * inject in each and every Activity that is created in the application
 *
 */
public abstract class BaseActivity extends DaggerAppCompatActivity {

    @LayoutRes
    protected abstract int layoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
        ButterKnife.bind(this);
    }
}