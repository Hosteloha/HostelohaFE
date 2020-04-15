package com.hosteloha.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.data.AllProductsSubject;
import com.hosteloha.app.log.AppLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostelOhaService extends Service {

    private static HostelOhaService mHostelOhaService = null;
    private String mDefaultUserJWT = null;

    public static HostelOhaService getService() {
        return mHostelOhaService;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHostelOhaService = this;
        AppLog.debugOut("  service onCreate");
        autenticateDefaultUser();
        getSplashdata();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.debugOut("  service onDestroy");
    }


    private void autenticateDefaultUser() {
        UserAuthentication defaultUser = new UserAuthentication("testuser@gmail.com", "password");
        ApiUtil.getServiceClass().getAuthenticationToken(defaultUser).enqueue(new Callback<AuthenticationTokenJWT>() {
            @Override
            public void onResponse(Call<AuthenticationTokenJWT> call, Response<AuthenticationTokenJWT> response) {
                if (response.isSuccessful()) {
                    AuthenticationTokenJWT authenticationTokenJWT = response.body();
                    mDefaultUserJWT = "Bearer " + authenticationTokenJWT.getJwt();
                    getSplashdata();
                    getAllProducts();
                    AppLog.debugOut("user id :  " + authenticationTokenJWT.getUserId() + "  JWT " + authenticationTokenJWT.getJwt());
                } else {
                    AppLog.debugOut(" ===>  Default user response not Successful");
                }
            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable t) {
                AppLog.debugOut(" ===> autenticateDefaultUser response Failed");
            }
        });
    }

    public void getSplashdata() {
        ApiUtil.getServiceClass().getCategoryMapList(mDefaultUserJWT).enqueue(new Callback<Map<String, Set<String>>>() {
            @Override
            public void onResponse(Call<Map<String, Set<String>>> call, Response<Map<String, Set<String>>> response) {

                AppLog.debugOut("  categoriesMap  " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Map<String, Set<String>> categoriesMap = response.body();
                    if (categoriesMap != null)
                        HostelohaUtils.setAllCategoriesMap(categoriesMap);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable t) {
                AppLog.debugOut(" getSplashdata ===>  onFailure  ");
                getSplashdata();
            }
        });
    }

    public void getAllProducts() {
        ApiUtil.getServiceClass().getAllProcducts(mDefaultUserJWT).enqueue(new Callback<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                AppLog.debugOut(" getAllProducts  =====> ......isSuccessful  " + response.isSuccessful());
                if (response.isSuccessful()) {
                    List<ProductObject> mArrayList = response.body();
                    AppLog.debugOut("response products list  count " + mArrayList.size());
                    if (mArrayList != null) {
                        AllProductsSubject.getAllProductsSubject().setProductsList(mArrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable t) {
                AppLog.debugOut("error loading allProducts from server");
                getAllProducts();
            }

        });

    }
}
