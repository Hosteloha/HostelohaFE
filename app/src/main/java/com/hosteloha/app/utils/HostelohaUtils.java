package com.hosteloha.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.service.HostelohaService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HostelohaUtils {

    // PRIVATE should be accessed using getters
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor prefsEditor;
    private static GoogleSignInClient mGoogleSignInClient = null;
    //firebase objects
    private static StorageReference mFireStorageReference = null;
    private static DatabaseReference mFireDatabaseReference = null;

    static Map<String, Set<String>> mAllCategoriesMap = null;

    public static String AUTHENTICATION_TOKEN = "";

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

    /**
     * To store the product images.
     *
     * @return Firebase Storage Reference
     */
    public static StorageReference getFirebaseStorage() {
        if (mFireStorageReference == null) {
            mFireStorageReference = FirebaseStorage.getInstance().getReference();
        }
        return mFireStorageReference;
    }

    /**
     * To store the image urls and product id's
     *
     * @return
     */
    public static DatabaseReference getFirebaseDatabase(Activity activity) {
        if (mFireDatabaseReference == null) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                mFireDatabaseReference = FirebaseDatabase.getInstance().getReference(Define.DATABASE_PATH_UPLOADS);
            } else {
                mAuth.signInAnonymously().addOnSuccessListener(activity, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        HostelohaLog.debugOut("signInAnonymously:SUCCESS");
                    }
                }).addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        HostelohaLog.debugOut("signInAnonymously:FAILURE" + exception.getLocalizedMessage());
                    }
                });
            }
        }
        return mFireDatabaseReference;
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

    public static HostelohaService getHostelohaService(Context context) {
        HostelohaService service = HostelohaService.getService();
        if (service == null) {
            HostelohaLog.debugOut(" Service found NULL :: getHostelohaService :: starting");
            context.startService(new Intent(context, HostelohaService.class));
        }
        return service;
    }
}
