package com.conicskill.app.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class FontHelper {

    public static final HashMap<String, Font> fontsHashMap = new HashMap<String, Font>();
    public static final ArrayList<Font> fontsArrayList = new ArrayList<Font>();
    private static FontHelper sInstance;
    private static Context mContext;


    public FontHelper(Context context) {
        mContext = context;
    }

    public static synchronized FontHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FontHelper(context);
            initFonts();
        }
        return sInstance;
    }

    private static void initFonts() {

        fontsHashMap.clear();
        fontsArrayList.clear();

        JSONObject jsonObject = loadJSONFromAsset(mContext,Constants.ASSETS_FILE_FONTS);
        JSONArray jsonFontsArray = null;
        try {
            jsonFontsArray = jsonObject.getJSONArray("fonts");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonFontsArray.length(); i++) {
            try {

                JSONObject jsonFontObject = jsonFontsArray.getJSONObject(i);

                Font font = new Font();
                font.setFontName(jsonFontObject.getString("fontName"));
                font.setFontFileName(jsonFontObject.getString("fileName"));
                font.setFontTypeface(getTypeface(mContext, font.getFontFileName()));

                fontsHashMap.put(font.getFontName(), font);
                fontsArrayList.add(font);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Typeface getTypeface(Context context, String fileName) {
        return Typeface.createFromAsset(context.getAssets(), Constants.ASSETS_DIR_FONTS.concat(fileName));
    }

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     */
    public void overrideFont() {

        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(mContext.getAssets(), Constants.ASSETS_DIR_FONTS.concat(Constants.APP_CUSTOM_FONT_FILE_NAME));

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(Constants.DEFAULT_FONT_NAME_TO_OVERRIDE);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
//            Log.e("Font error", "Can not set custom font " + Constants.APP_CUSTOM_FONT_FILE_NAME + " instead of " + Constants.DEFAULT_FONT_NAME_TO_OVERRIDE);
        }
    }

    public static SpannableString getCustomTypefaceTitle(String titleString) {
        SpannableString title = new SpannableString(titleString);
        // Update the action bar title with the TypefaceSpan instance
        title.setSpan(new TypefaceSpan(Constants.ASSETS_DIR_FONTS + Constants.APP_CUSTOM_FONT_FILE_NAME), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return title;
    }

    public static JSONObject loadJSONFromAsset(Context mContext, String fontsJsonFileName) {

        String json = null;
        try {
            InputStream is = mContext.getAssets().open(fontsJsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponseObject = null;
        try {
            jsonResponseObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponseObject;
    }


    public Typeface getAppCustomTypeface() {
        return Typeface.createFromAsset(mContext.getAssets(), Constants.ASSETS_DIR_FONTS.concat(Constants.APP_CUSTOM_FONT_FILE_NAME));
    }

    public Typeface getAppCustomLightTypeface() {
        return Typeface.createFromAsset(mContext.getAssets(), Constants.ASSETS_DIR_FONTS.concat(Constants.APP_CUSTOM_LIGHT_FONT_FILE_NAME));
    }

    public Typeface getAppCustomMediumTypeface() {
        return Typeface.createFromAsset(mContext.getAssets(), Constants.ASSETS_DIR_FONTS.concat(Constants.APP_CUSTOM_MEDIUM_FILE_NAME));
    }

    public Typeface getAppCustomRegularTypeface() {
        return Typeface.createFromAsset(mContext.getAssets(), Constants.ASSETS_DIR_FONTS.concat(Constants.APP_CUSTOM_REGULAR_FONT_FILE_NAME));
    }

    public Typeface getAppCustomBoldTypeface() {
        return Typeface.createFromAsset(mContext.getAssets(), Constants.ASSETS_DIR_FONTS.concat(Constants.APP_CUSTOM_BOLD_FONT_FILE_NAME));
    }

    public HashMap<String, Font> getFontsHashMap() {
        return fontsHashMap;
    }

    public ArrayList<Font> getFontsArrayList() {
        return fontsArrayList;
    }
}