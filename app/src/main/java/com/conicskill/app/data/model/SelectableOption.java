package com.conicskill.app.data.model;

import com.conicskill.app.data.model.exam.Options;

public class SelectableOption extends Options {
    private boolean isSelected = false;
    private boolean isCorrect = false;
    private Options item = new Options();

    public SelectableOption(){

    }

    public SelectableOption(Options item, boolean isSelected, boolean isCorrect) {
        super(item);
        this.item = item;
        this.isSelected = isSelected;
        this.isCorrect = isCorrect;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Options getOption() {
        return item;
    }
}