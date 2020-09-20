package com.hosteloha.app.datarepository;

import android.content.Context;

import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.define.SortBy;
import com.hosteloha.app.define.SortingOrder;
import com.hosteloha.app.log.HostelohaLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

@Singleton
public class Repository {

    private Context mContext;
    private static Repository mRepository;
    private RetroClientApi mRetroClientApi;
    private String mCategoryID;
    private int mPageNumber;

    private MediatorLiveData<List<ProductObject>> mAllProductsList;
    private SortBy mSortByEnum = SortBy.DEFAULT;
    private SortingOrder mSortingOrderEnum = SortingOrder.DESCENDING;

    private Map<Integer, ProductObject> mProductMap = new HashMap<>();

    public synchronized static Repository getInstance() {
        if (mRepository == null)
            mRepository = new Repository();
        return mRepository;
    }

    private Repository() {
        mAllProductsList = new MediatorLiveData<>();

        mRetroClientApi = RetroClientApi.getInstance();
        initObservables();
    }

    private void initObservables() {
        LiveData<List<ProductObject>> allProductsLiveData = mRetroClientApi.getAllProductsLiveData();
        mAllProductsList.addSource(allProductsLiveData, new Observer<List<ProductObject>>() {
            @Override
            public void onChanged(@Nullable List<ProductObject> productObjects) {
                mAllProductsList.setValue(productObjects);
                updateProductsMap((ArrayList<ProductObject>) productObjects);
            }
        });
    }

    public LiveData<List<ProductObject>> getAllProductsLiveData() {
        if (mAllProductsList == null || mAllProductsList.getValue() == null || mAllProductsList.getValue().isEmpty())
            mRetroClientApi.req_getAllProducts();
        return mAllProductsList;
    }

    public LiveData<List<ProductObject>> getCategoryProductsLiveData() {
        return mRetroClientApi.getCategoryProductsLiveData();
    }

    public SortBy getSortByEnum() {
        return mSortByEnum;
    }

    public void setSortByEnum(SortBy sortByEnum) {
        this.mSortByEnum = sortByEnum;
        req_firstPagedata();
    }

    public SortingOrder getSortingTypeEnum() {
        return mSortingOrderEnum;
    }

    public void setSortingTypeEnum(SortingOrder sortingTypeEnum) {
        this.mSortingOrderEnum = sortingTypeEnum;
        req_firstPagedata();
    }

    public void req_getCategoryProducts(int pageNumber, String categoryId) {
        mPageNumber = pageNumber;
        mCategoryID = categoryId;
        mRetroClientApi.req_getCategoryProducts(pageNumber, categoryId, mSortByEnum.getValue(), mSortingOrderEnum.getValue());
    }

    public void req_nextPagedata() {
        req_getCategoryProducts(mPageNumber + 1, mCategoryID);
    }

    public void req_firstPagedata() {
        req_getCategoryProducts(0, mCategoryID);
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

    public ProductObject getProductObject(int productId) {
        return mProductMap.get(productId);
    }
}
