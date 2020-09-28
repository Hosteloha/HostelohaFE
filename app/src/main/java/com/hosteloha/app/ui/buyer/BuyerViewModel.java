package com.hosteloha.app.ui.buyer;

import com.hosteloha.app.datarepository.Repository;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.log.HostelohaLog;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BuyerViewModel extends ViewModel {

    private String mCategory_ID = "NONE";

    private Repository mRepository;

    public BuyerViewModel() {
        mRepository = Repository.getInstance();
    }

    public LiveData<List<ProductObject>> getAllProducts() {
        return mRepository.getAllProductsLiveData();
    }

    public LiveData<List<ProductObject>> getCategoryProducts() {
        return mRepository.getCategoryProductsLiveData();
    }

    public LiveData<List<ProductObject>> getProductsLiveData() {
        if (mCategory_ID.equals("NONE"))
            return getAllProducts();
        else
            return getCategoryProducts();
    }

    public void setCategoryId(String category_id) {
        if (category_id != mCategory_ID) {
            mCategory_ID = category_id;
        }
        HostelohaLog.debugOut(" mCategory_ID " + mCategory_ID);
        if (!mCategory_ID.equals("NONE"))
            mRepository.req_getCategoryProducts(0, mCategory_ID);
    }

    public void requestNextPageData() {
        if (mCategory_ID == null || mCategory_ID.equals("NONE"))
            mRepository.req_allProductsNextPageData();
        else
            mRepository.req_CategoryProductsNextPageData();
    }

    public LiveData<ProductObject> getProductObject(int productId) {
        return mRepository.getProductObject(productId);
    }

    public void addWishList(int productId) {
        mRepository.addWishList(productId);
    }

    public void removeWishList(int productId) {
        mRepository.removeWishList(productId);
    }
}