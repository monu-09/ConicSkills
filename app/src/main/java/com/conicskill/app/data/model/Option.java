package com.conicskill.app.data.model;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * @author vikas@cprep.in
 * @use This is the options that will be inflated in the Selection part of the form for the multiple
 * options that will be required in the application in order to make them selectable and save the same
 * entry in the database.
 */
public class Option implements Serializable {
    private int optionId;

    private String  optionName;

    @Nullable
    @DrawableRes
    private int optionIcon;

    @Nullable
    @StringRes
    private int optionDesc;

    @Nullable
    private ArrayList<Option> optionSelection;

    public Option() {

    }

    public Option(Option item) {
        this.optionId = item.getOptionId();
        this.optionDesc = item.getOptionDesc();
        this.optionIcon = item.getOptionIcon();
        this.optionName = item.getOptionName();
        this.optionSelection = item.getOptionSelection();
    }

//    private boolean isSelected;

    public int getOptionId() {
        return optionId;
    }

    public Option setOptionId(int optionId) {
        this.optionId = optionId;
        return this;
    }

    public String  getOptionName() {
        return optionName;
    }

    public Option setOptionName(String optionName) {
        this.optionName = optionName;
        return this;
    }

    public int getOptionIcon() {
        return optionIcon;
    }

    public Option setOptionIcon(int optionIcon) {
        this.optionIcon = optionIcon;
        return this;
    }

    public int getOptionDesc() {
        return optionDesc;
    }

    public Option setOptionDesc(int optionDesc) {
        this.optionDesc = optionDesc;
        return this;
    }

    @Nullable
    public ArrayList<Option> getOptionSelection() {
        return optionSelection;
    }

    public Option setOptionSelection(@Nullable ArrayList<Option> optionSelection) {
        this.optionSelection = optionSelection;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        Option itemCompare = (Option) obj;
        if(itemCompare.getOptionId() == this.getOptionId())
            return true;

        return false;
    }
}
