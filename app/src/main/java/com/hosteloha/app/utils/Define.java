package com.hosteloha.app.utils;

public class Define {

    // To enable disable the previous view check configuration
    public static boolean isPreviousViewCheckEnabled = false;
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 100;
    public static int REQUEST_CODE_READ_STORAGE_PERMISSION = REQUEST_CODE_CAMERA_PERMISSION + 1;
    public static int REQUEST_CODE_ACTION_IMAGE_CAPTURE = REQUEST_CODE_READ_STORAGE_PERMISSION + 1;
    public static int REQUEST_CODE_ACTION_GET_CONTENT = REQUEST_CODE_ACTION_IMAGE_CAPTURE + 1;
    public static final int NOTIFICATION_PRODUCT_IMAGE = REQUEST_CODE_ACTION_GET_CONTENT + 1;

    public static final String KEY_USER_LOGIN_STATUS = "USER_LOGIN_STATUS";
    public static final String KEY_USER_AUTH_TOKEN = "USER_AUTH_TOKEN";
    public static final String KEY_USER_LOGIN_NAME = "USER_LOGIN_NAME";
    public static final String KEY_USER_LOGIN_ID = "USER_LOGIN_ID";

    public static final String KEY_CURRENT_VIEW_TYPE = "CURRENT_VIEW_TYPE";
    public static final String VIEW_BUYER = "VIEW_BUYER";
    public static final String VIEW_SELLER = "VIEW_SELLER";

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "products";

    // NOTIFICATION CHANNEL ID
    public static final String NOTIFICATION_CHANNEL_FILE_ID = "CHANNEL_FILES";
    // Places API Key
    //TODO :: Secure API Key
    public static final String PLACES_API_KEY = "AIzaSyDHMur-FuEJmrdHtx9IXHeP42L4UxvT2zA";

    //TODO :: Finalize category names since, we are not using integers
    public static final String CAT_NONE = "NONE";
    public static final String CAT_BEAUTY = "Beauty";
    public static final String CAT_ELECTRONICS = "Electronics";
    public static final String CAT_FASHION = "Fashion";
    public static final String CAT_BOOKS = "Books";
    public static final String CAT_MUSIC = "Entertainment"; // Just for test
    public static final String CAT_PROJECT = "Home & Kitchen"; // Just for test
    public static final String CAT_RENT = "Beauty";
    public static final String CAT_TRANSPORT = "Transport";
    public static final String CAT_SPORTS = "Sports";

}
