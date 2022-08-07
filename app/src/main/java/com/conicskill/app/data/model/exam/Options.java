package com.conicskill.app.data.model.exam;

public class Options {

    private String key;
    private String value;
    private boolean isSelected = false;

    public Options() {

    }

    public Options(Options item) {
        this.key = item.getKey();
        this.value = item.getValue();
        this.isSelected = item.isSelected();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}