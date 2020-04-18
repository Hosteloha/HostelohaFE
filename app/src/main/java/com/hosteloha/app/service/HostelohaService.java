package com.hosteloha.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.data.AllProductsSubject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        getSplashData();
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
                if (response.isSuccessful()) {
                    AuthenticationTokenJWT authenticationTokenJWT = response.body();
                    mDefaultUserJWT = "Bearer " + authenticationTokenJWT.getJwt();
                    getSplashData();
                    req_getAllProducts();
                    HostelohaLog.debugOut("user id :  " + authenticationTokenJWT.getUserId() + "  JWT " + authenticationTokenJWT.getJwt());
                } else {
                    HostelohaLog.debugOut(" ===>  Default user response not Successful");
                }
            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable t) {
                HostelohaLog.debugOut(" ===> autenticateDefaultUser response Failed");
            }
        });
    }

    public void getSplashData() {
        req_getCategoryMapList();
    }

    public void req_getCategoryMapList() {
        ApiUtil.getServiceClass().getCategoryMapList(mDefaultUserJWT).enqueue(new CallbackWithRetry<Map<String, Set<String>>>() {
            @Override
            public void onResponse(Call<Map<String, Set<String>>> call, Response<Map<String, Set<String>>> response) {
                HostelohaLog.debugOut("  categoriesMap  " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Map<String, Set<String>> categoriesMap = response.body();
                    if (categoriesMap != null)
                        HostelohaUtils.setAllCategoriesMap(categoriesMap);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable t) {
                HostelohaLog.debugOut(" getSplashdata ===>  onFailure  ");
            }
        });
    }

    public void req_getAllProducts() {
        ApiUtil.getServiceClass().getAllProcducts(mDefaultUserJWT).enqueue(new CallbackWithRetry<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                HostelohaLog.debugOut(" getAllProducts  =====> ......isSuccessful  " + response.isSuccessful());
                if (response.isSuccessful()) {
                    List<ProductObject> mArrayList = response.body();
                    HostelohaLog.debugOut("response products list  count " + mArrayList.size());
                    if (mArrayList != null) {
                        AllProductsSubject.getAllProductsSubject().setProductsList(mArrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable t) {
                HostelohaLog.debugOut("error loading allProducts from server");
            }
        });
    }
}
