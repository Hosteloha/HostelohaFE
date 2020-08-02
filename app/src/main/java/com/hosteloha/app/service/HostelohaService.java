package com.hosteloha.app.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.AppFireStorage;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Response;

public class HostelohaService extends Service {

    private static HostelohaService mHostelohaService = null;
    private String mDefaultUserJWT = null;

    public static HostelohaService getService() {
        return mHostelohaService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHostelohaService = this;
        HostelohaLog.debugOut("  service onCreate");
        req_authenticateDefaultUser();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HostelohaLog.debugOut("  service onDestroy");
    }
    
    private void req_authenticateDefaultUser() {
        UserAuthentication defaultUser = new UserAuthentication("testuser@gmail.com", "password");
        ApiUtil.getServiceClass().getAuthenticationToken(defaultUser).enqueue(new CallbackWithRetry<AuthenticationTokenJWT>() {
            @Override
            public void onResponse(Call<AuthenticationTokenJWT> call, Response<AuthenticationTokenJWT> response) {
                HostelohaLog.debugOut("[REQ] getAuthenticationToken  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    AuthenticationTokenJWT authenticationTokenJWT = response.body();
                    mDefaultUserJWT = "Bearer " + authenticationTokenJWT.getJwt();
                    HostelohaUtils.setDefaultAuthenticationToken(mDefaultUserJWT);
                }
            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable throwable) {
                super.onFailure(call, throwable);
                HostelohaLog.debugOut("[REQ] getAuthenticationToken ===>  onFailure");
            }
        });
    }


    /**
     * To upload the product gallery to the server.
     *
     * @param filesURIList list of file URI to upload.
     * @param productID    product ID, to link the file URI
     */
    public void uploadProductImagesToFire(final List<Uri> filesURIList, final int productID) {
        ArrayList<String> generatedURlList = AppFireStorage.uploadFileToFirebase(filesURIList, productID, getApplicationContext());
//        AppFireDataBase.addUrlList(String.valueOf(productID), generatedURlList);
    }

}