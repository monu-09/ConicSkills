package com.conicskill.app.util;

import android.graphics.Typeface;

public class Font {

    private String fontName;
    private String fontFileName;
    private Typeface fontTypeface;
    private Boolean selected = false;

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontFileName() {
        return fontFileName;
    }

    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }

    public Typeface getFontTypeface() {
        return fontTypeface;
    }

    public void setFontTypeface(Typeface fontTypeface) {
        this.fontTypeface = fontTypeface;
    }

    public Boolean isSelected() {
        return this.selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}