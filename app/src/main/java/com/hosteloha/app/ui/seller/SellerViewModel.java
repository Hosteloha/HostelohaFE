package com.hosteloha.app.ui.seller;

import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.retroapi.ApiUtil;
import com.hosteloha.app.datarepository.retroapi.CallbackWithRetry;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.Map;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Response;

public class SellerViewModel extends ViewModel {

    private MutableLiveData<ProductObject> mProduct;
    private MutableLiveData<Map<String, Set<String>>> mProductMap;

    public SellerViewModel() {
        mProductMap = new MutableLiveData<>();
        req_getCategoryMapList();
    }

    public LiveData<ProductObject> getProductObject() {
        return mProduct;
    }

    public LiveData<Map<String, Set<String>>> getCategoriesListLiveData() {
        return mProductMap;
    }

    public void req_getCategoryMapList() {
        ApiUtil.getServiceClass().getCategoryMapList(HostelohaUtils.getDefaultAuthenticationToken()).enqueue(new CallbackWithRetry<Map<String, Set<String>>>() {
            @Override
            public void onResponse(Call<Map<String, Set<String>>> call, Response<Map<String, Set<String>>> response) {
                HostelohaLog.debugOut("[REQ] getCategoryMapList  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Map<String, Set<String>> categoriesMap = response.body();
                    mProductMap.setValue(categoriesMap);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable throwable) {
                super.onFailure(call, throwable);
                HostelohaLog.debugOut("[REQ] getCategoryMapList ===>  onFailure");
            }
        });
    }
}