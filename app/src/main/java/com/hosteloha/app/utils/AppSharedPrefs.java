package com.hosteloha.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPrefs {

    // PRIVATE should be accessed using getters
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor prefsEditor;


    public static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }

    public static void storeUserLoginInfo(Context context, boolean isLoggedIn, String authenticationToken) {
        sharedPreferences = getSharedPreferences(context);
        prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(Define.KEY_USER_LOGIN_STATUS, isLoggedIn);
        prefsEditor.putString(Define.KEY_USER_AUTH_TOKEN, authenticationToken);
        prefsEditor.apply();
    }

    public static boolean getUserLoginInfo(Context context) {
        sharedPreferences = getSharedPreferences(context);
        prefsEditor = sharedPreferences.edit();
        boolean isUserLoggedIn = sharedPreferences.getBoolean(Define.KEY_USER_LOGIN_STATUS, false);
        if (isUserLoggedIn) {
            HostelohaUtils.AUTHENTICATION_TOKEN = sharedPreferences.getString(Define.KEY_USER_AUTH_TOKEN, null);
        }
        return isUserLoggedIn;
    }

    public static void storeCurrentViewTypeInPrefs(Context context, String viewType) {
        sharedPreferences = getSharedPreferences(context);
        prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(Define.KEY_CURRENT_VIEW_TYPE, viewType);
        prefsEditor.apply();
    }

    public static String getPreviousViewType(Context context) {
        sharedPreferences = getSharedPreferences(context);
        String viewType = sharedPreferences.getString(Define.KEY_CURRENT_VIEW_TYPE, Define.VIEW_BUYER);
        return viewType;
    }

    public static void clearSharedPrefsOnLogout(Activity activity) {
        sharedPreferences = getSharedPreferences(activity);
        sharedPreferences.edit().clear().apply();
    }
}
