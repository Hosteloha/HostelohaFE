package com.hosteloha.app.datarepository;

import android.content.Context;

import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.define.SortBy;
import com.hosteloha.app.define.SortingOrder;

import java.util.List;

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
    private int mAllProductsPageNumber;

    private MediatorLiveData<List<ProductObject>> mAllProductsList;
    private SortBy mSortByEnum = SortBy.QUANTITY;
    private SortingOrder mSortingOrderEnum = SortingOrder.DESCENDING;

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
            }
        });
    }

    public LiveData<List<ProductObject>> getAllProductsLiveData() {
        if (mRetroClientApi.getAllProductsLiveData().getValue() == null || mRetroClientApi.getAllProductsLiveData().getValue().isEmpty())
            req_allProductsFirstPageData();
        return mRetroClientApi.getAllProductsLiveData();
    }

    public LiveData<List<ProductObject>> getCategoryProductsLiveData() {
        return mRetroClientApi.getCategoryProductsLiveData();
    }

    public SortBy getSortByEnum() {
        return mSortByEnum;
    }

    public void setSortByEnum(SortBy sortByEnum) {
        this.mSortByEnum = sortByEnum;
        req_CategoryProductsFirstPageData();
    }

    public SortingOrder getSortingTypeEnum() {
        return mSortingOrderEnum;
    }

    public void setSortingTypeEnum(SortingOrder sortingTypeEnum) {
        this.mSortingOrderEnum = sortingTypeEnum;
        req_CategoryProductsFirstPageData();
        req_allProductsFirstPageData();
    }

    public void req_getCategoryProducts(int pageNumber, String categoryId) {
        mPageNumber = pageNumber;
        mCategoryID = categoryId;
        mRetroClientApi.req_getCategoryProducts(pageNumber, categoryId, mSortByEnum.getValue(), mSortingOrderEnum.getValue());
    }

    public void req_CategoryProductsNextPageData() {
        req_getCategoryProducts(mPageNumber + 1, mCategoryID);
    }

    public void req_CategoryProductsFirstPageData() {
        req_getCategoryProducts(0, mCategoryID);
    }

    public void req_getAllProducts(int pageNumber) {
        mAllProductsPageNumber = pageNumber;
        mRetroClientApi.req_getAllProductsByPages(mAllProductsPageNumber, mSortByEnum.getValue(), mSortingOrderEnum.getValue());
    }

    public void req_allProductsNextPageData() {
        req_getAllProducts(mAllProductsPageNumber + 1);
    }

    public void req_allProductsFirstPageData() {
        req_getAllProducts(0);
    }

    public LiveData<ProductObject> getProductObject(int productId) {
        return mRetroClientApi.req_ProductObjectByID(productId);
    }

    public void addWishList(int productId) {
        mRetroClientApi.addWishList(productId);
    }

    public void removeWishList(int productId) {
        mRetroClientApi.removeWishList(productId);
    }
}
