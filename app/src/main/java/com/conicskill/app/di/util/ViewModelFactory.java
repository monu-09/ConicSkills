package com.conicskill.app.di.util;


import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends AndroidViewModel>, Provider<AndroidViewModel>> creators;

    @Inject
    public ViewModelFactory(Map<Class<? extends AndroidViewModel>, Provider<AndroidViewModel>> creators) {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends AndroidViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends AndroidViewModel>, Provider<AndroidViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}