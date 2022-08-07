package com.conicskill.app.data.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class SupportModel {

    private int id;
    @StringRes
    private int heading;
    @StringRes
    private int description;
    @StringRes
    private int subDescription;
    @DrawableRes
    private int icon;

    public int getId() {
        return id;
    }

    public SupportModel setId(int id) {
        this.id = id;
        return this;
    }

    public int getHeading() {
        return heading;
    }

    public SupportModel setHeading(@StringRes int heading) {
        this.heading = heading;
        return this;
    }

    public int getDescription() {
        return description;
    }

    public SupportModel setDescription(@StringRes int description) {
        this.description = description;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public SupportModel setIcon(@DrawableRes int icon) {
        this.icon = icon;
        return this;
    }

    public int getSubDescription() {
        return subDescription;
    }

    public SupportModel setSubDescription(int subDescription) {
        this.subDescription = subDescription;
        return this;
    }
}