package com.conicskill.app.data.model;

import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

public class FeatureModel implements Diffable {

    private int id;

    @DrawableRes
    private int backgroundDrawable;

    @StringRes
    private int featureName;

    @DrawableRes
    private int featureIcon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(int backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

    public int getFeatureName() {
        return featureName;
    }

    public void setFeatureName(int featureName) {
        this.featureName = featureName;
    }

    public int getFeatureIcon() {
        return featureIcon;
    }

    public void setFeatureIcon(int featureIcon) {
        this.featureIcon = featureIcon;
    }

    @Override
    public boolean areContentTheSame(@NotNull Object o) {
        return false;
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }
}
