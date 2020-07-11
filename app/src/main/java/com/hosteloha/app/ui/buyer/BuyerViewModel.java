package com.hosteloha.app.ui.buyer;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.AppFireDataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Response;

public class BuyerViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<ProductObject>> mProductsList;
    private Map<Integer, ProductObject> mProductMap = new HashMap<>();
    private String mDefaultUserJWT = "";

    public BuyerViewModel() {
        mText = new MutableLiveData<>();
        mProductsList = new MutableLiveData<>();
        mText.setValue("This is buyer fragment");
        if (mDefaultUserJWT == "")
            req_authenticateDefaultUser();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<ProductObject>> getAllProducts() {
        return mProductsList;
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

    public void req_getAllProducts() {
        if (mDefaultUserJWT == "") {
            req_authenticateDefaultUser();
            return;
        }
        ApiUtil.getServiceClass().getAllProducts(mDefaultUserJWT).enqueue(new CallbackWithRetry<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                HostelohaLog.debugOut("[REQ] getAllProducts  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    ArrayList<ProductObject> mArrayList = (ArrayList<ProductObject>) response.body();
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

                    mProductsList.setValue(mArrayList);
                    updateProductsMap(mArrayList);
                }

            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable throwable) {
                HostelohaLog.debugOut("[REQ] getAllProducts ===>  onFailure");
                super.onFailure(call, throwable);
            }
        });
    }

    public ProductObject getProductObject(int productId) {
        return mProductMap.get(productId);
    }

    private void updateProductsMap(ArrayList<ProductObject> arrayList) {
        if (arrayList != null) {
            mProductMap.clear();
            for (ProductObject obj : arrayList) {
                mProductMap.put(obj.getProductId(), obj);
            }
        } else
            HostelohaLog.debugOut("  arrayList is null ");
    }
}