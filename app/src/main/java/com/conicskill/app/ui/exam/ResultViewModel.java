package com.conicskill.app.ui.exam;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import javax.inject.Inject;

public class ResultViewModel extends AndroidViewModel {

    @Inject
    public ResultViewModel(@NonNull Application application) {
        super(application);
    }
}
