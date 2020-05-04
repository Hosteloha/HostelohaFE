package com.hosteloha.app.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.list.data.AllProductsSubject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.AppFireDataBase;
import com.hosteloha.app.utils.AppFireStorage;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                HostelohaLog.debugOut("[REQ] getAuthenticationToken  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    AuthenticationTokenJWT authenticationTokenJWT = response.body();
                    mDefaultUserJWT = "Bearer " + authenticationTokenJWT.getJwt();
                    getSplashData();
                    req_getAllProducts();
                }
            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable throwable) {
                super.onFailure(call, throwable);
                HostelohaLog.debugOut("[REQ] getAuthenticationToken ===>  onFailure");
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
                HostelohaLog.debugOut("[REQ] getCategoryMapList  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Map<String, Set<String>> categoriesMap = response.body();
                    if (categoriesMap != null)
                        HostelohaUtils.setAllCategoriesMap(categoriesMap);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable throwable) {
                super.onFailure(call, throwable);
                HostelohaLog.debugOut("[REQ] getCategoryMapList ===>  onFailure");
            }
        });
    }

    public void req_getAllProducts() {
        ApiUtil.getServiceClass().getAllProducts(mDefaultUserJWT).enqueue(new CallbackWithRetry<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                HostelohaLog.debugOut("[REQ] getAllProducts  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    List<ProductObject> mArrayList = response.body();
                    HostelohaLog.debugOut("[REQ] products_list size ::  " + mArrayList.size());
                    HostelohaLog.debugOut("[REQ] products_list ---> " + mArrayList.get(0).toString());

                    // Getting from firebase - just temporary code to set image gallery
                    Map<String, ArrayList<String>> productImagesList = AppFireDataBase.getProductImagesMap();
                    for (ProductObject product : mArrayList) {
                        String productID = String.valueOf(product.getProductId());
                        if (productImagesList.containsKey(productID)) {
                            ArrayList<String> productImages = productImagesList.get(productID);
                            product.setProduct_images(productImages);
                        } else {
                            //Default URL or glide will take care with placeholder
                            product.setProduct_images(new ArrayList<String>());
                        }
                    }

                    AllProductsSubject.getAllProductsSubject().setProductsList(mArrayList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable throwable) {
                HostelohaLog.debugOut("[REQ] getAllProducts ===>  onFailure");
                super.onFailure(call, throwable);
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