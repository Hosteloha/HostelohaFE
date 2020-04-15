package com.hosteloha.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.service.HostelOhaService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class HostelohaUtils {

    static SharedPreferences sharedPreferences = null;
    static SharedPreferences.Editor prefsEditor;
    public static String AUTHENTICATION_TOKEN = "";
    static GoogleSignInClient mGoogleSignInClient = null;

    static Map<String, Set<String>> mAllCategoriesMap = null;

    public static String getCurrentDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return String.valueOf(currentTime);
    }

    /**
     * @param activity
     */
    public static void logOutUser(Activity activity) {
        //Clear shared Prefs
        sharedPreferences = getSharedPreferences(activity);
        sharedPreferences.edit().clear().apply();

        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.revokeAccess();
            mGoogleSignInClient.signOut();
        }

        //signing out even the phone numbers
        FirebaseAuth.getInstance().signOut();

        activity.finish();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static String getCurrentTimePin() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static void setAuthenticationToken(String token) {
        AUTHENTICATION_TOKEN = "Bearer " + token;
    }

    public static void showSnackBarNotification(Activity mActivity, String message) {
        if (mActivity != null) {
            Snackbar snackBar = Snackbar.make(mActivity.findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_LONG);
            snackBar.show();
        }
    }

    public static GoogleSignInClient getGoogleSignInClient(Activity mActivity) {
        if (mGoogleSignInClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
        }
        return mGoogleSignInClient;
    }

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

    public static Map<String, Set<String>> getAllCategoriesMap() {
        return mAllCategoriesMap;
    }

    public static void setAllCategoriesMap(Map<String, Set<String>> allCategoriesMap) {
        mAllCategoriesMap = allCategoriesMap;
    }

    public static HostelOhaService getHostelOhaService(Context context) {
        HostelOhaService service = HostelOhaService.getService();
        if (service == null) {
            Intent intent = new Intent();
            intent.setAction("com.hosteloha.app.service.HostelOhaService");
            intent.setPackage("com.hosteloha");
            context.startService(intent);
        }

        return service;
    }
}
