package com.conicskill.app.util;

import java.util.Objects;

public class Constants {
    public static final String ASSETS_DIR_FONTS = "fonts/";
    public static final String ASSETS_DIR_JSON = "json/";
    public static final String ASSETS_FILE_FONTS = ASSETS_DIR_JSON + "fonts.json";

    public static final String APP_CUSTOM_LIGHT_FONT_FILE_NAME = "DIN-Light.otf";
    public static final String APP_CUSTOM_MEDIUM_FILE_NAME = "DIN-Medium.otf";
    public static final String APP_CUSTOM_REGULAR_FONT_FILE_NAME = "DIN-Regular.otf";
    public static final String APP_CUSTOM_BOLD_FONT_FILE_NAME = "DIN-Bold.otf";
    public static final String APP_CUSTOM_COCOGOOSE = "Sen.otf";
    public static final String APP_CUSTOM_FONT_FILE_NAME = APP_CUSTOM_REGULAR_FONT_FILE_NAME;
    public static final String DEFAULT_FONT_NAME_TO_OVERRIDE = "SERIF";

    public static final boolean IS_FIRST = false;
    public static final String ALERT_DISABLED = "alertdisabled";
    public static final String USER_NAME = "user name";
    public static final String USER_PASSWORD = "user password";
    public static final String IS_REMEMBER_ME = "isRememberMe";
    public static final String HEADER_USER_NAME = "header user name";
    public static final String HEADER_USER_PASSWORD = "header user password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String CANDIDATE_ID = "candidate_id";
    public static final String PHONE_NUMBER = "phone_number";
    //    public static final String CHAT_SERVER_URL = "http://161.97.79.118:3000";
    public static final String CHAT_SERVER_URL = "http://185.217.125.229:3000";

    public static int COUNTRY_REQUEST = 3;

    public static final String EXAM_INSTRUCTIONS = "instructions";
    public static final String EXAM_SOLUTIONS = "solutions";
    public static final String EXAM_RESULTS = "results";

    public static final String EXTRA_VIDEO_YOUTUBE_ID = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".extra_video_youtube_id";
    public static final String EXTRA_VIDEO_YOUTUBE_NAME = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".extra_video_youtube_NAME";
    public static final String EXTRA_USE_CUSTOM_THEME = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".extra_use_custom_theme";
    public static final String EXTRA_USE_LIVE_CLASS_ID = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".extra_use_liveclass_id";
    public static final String EXTRA_USE_LIVE_CLASS_BOLLEAN = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".extra_use_liveclass_bollen";
    public static final String OFFLINE = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".offline";
    public static final String JSON = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".json";
    public static final String AUDIO = Objects.requireNonNull(Constants.class.getPackage()).getName() + ".audio";
}
