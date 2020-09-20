package com.hosteloha.app.datarepository;

import com.hosteloha.app.datarepository.beans.PagedCategoryListModel;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.retroapi.ApiUtil;
import com.hosteloha.app.datarepository.retroapi.CallbackWithRetry;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.AppFireDataBase;
import com.hosteloha.app.utils.HostelohaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class RetroClientApi {

    private static RetroClientApi mRetroClientApi = new RetroClientApi();

    private String mCategoryID;
    private int mPageNumber;
    private MutableLiveData<List<ProductObject>> mAllProductsList;
    private MutableLiveData<List<ProductObject>> mCategoryProductsList;

    private RetroClientApi() {
        mAllProductsList = new MutableLiveData<>();
        mCategoryProductsList = new MutableLiveData<>();
        req_getAllProducts();
    }

    public static RetroClientApi getInstance() {
        return mRetroClientApi;
    }

    public LiveData<List<ProductObject>> getAllProductsLiveData() {
        return mAllProductsList;
    }

    public LiveData<List<ProductObject>> getCategoryProductsLiveData() {
        return mCategoryProductsList;
    }

    public void req_getAllProducts() {

        ApiUtil.getServiceClass().getAllProducts(HostelohaUtils.getAuthenticationToken()).enqueue(new CallbackWithRetry<List<ProductObject>>() {
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
                    mAllProductsList.setValue(mArrayList);
                } else {
                    try {
                        HostelohaLog.debugOut("[REQ] getAllProducts  =====> failed " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable throwable) {
                HostelohaLog.debugOut("[REQ] getAllProducts ===>  onFailure" + throwable.getLocalizedMessage());
                super.onFailure(call, throwable);
            }
        });
    }


    public synchronized void req_getCategoryProducts(int pageNumber, String categoryId) {
        if (mPageNumber == pageNumber && mCategoryID == categoryId)
            return;
        if (pageNumber == 0 && categoryId != mCategoryID)
            mCategoryProductsList.postValue(new ArrayList<ProductObject>());
        mPageNumber = pageNumber;
        mCategoryID = categoryId;

        ApiUtil.getServiceClass().getAllProductsByCategoryPages(HostelohaUtils.getAuthenticationToken(), categoryId, pageNumber,
                20, "quantity", "ASC").enqueue(new CallbackWithRetry<PagedCategoryListModel>() {
            @Override
            public void onResponse(Call<PagedCategoryListModel> call, Response<PagedCategoryListModel> response) {
                HostelohaLog.debugOut("[REQ] getAllProductsByCategoryPages  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    PagedCategoryListModel pagedCategoryListModel = response.body();
                    HostelohaLog.debugOut("[REQ] getAllProductsByCategoryPages CategoryList ===== >count ::  ---> " + pagedCategoryListModel.getProductObjects().size() + " page Number :: " + pagedCategoryListModel.getCurrentPageNumber());

                    // Getting from firebase - just temporary code to set image gallery
                    Map<String, ArrayList<String>> productImagesList = AppFireDataBase.getProductImagesMap();
                    for (ProductObject product : pagedCategoryListModel.getProductObjects()) {
                        String productID = String.valueOf(product.getProductId());
                        if (productImagesList.containsKey(productID)) {
                            ArrayList<String> productImages = productImagesList.get(productID);
                            product.setProduct_images(productImages);
                        } else {
                            //Default URL or glide will take care with placeholder
                            product.setProduct_images(new ArrayList<String>());
                        }
                    }

                    if (mPageNumber == 0)
                        mCategoryProductsList.postValue(pagedCategoryListModel.getProductObjects());
                    else {
                        List<ProductObject> currentList = mCategoryProductsList.getValue();
                        currentList.addAll(pagedCategoryListModel.getProductObjects());
                        mCategoryProductsList.postValue(currentList);
                    }
                }
            }
        });
    }

}
