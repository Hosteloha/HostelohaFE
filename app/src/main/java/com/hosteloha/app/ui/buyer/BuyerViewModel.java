package com.hosteloha.app.ui.buyer;

import com.hosteloha.app.datarepository.Repository;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.beans.WishListRequest;
import com.hosteloha.app.datarepository.retroapi.ApiUtil;
import com.hosteloha.app.datarepository.retroapi.CallbackWithRetry;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

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
        mRepository.req_nextPagedata();
    }

    public LiveData<ProductObject> getProductObject(int productId) {
        return mRepository.getProductObject(productId);
    }

    public void addWishList(int productId) {
        HostelohaLog.debugOut(" productId :: " + productId + "  userID :: " + HostelohaUtils.getUserId() + "  --- " + HostelohaUtils.getAuthenticationToken());
        WishListRequest wishListRequest = new WishListRequest(HostelohaUtils.getUserId(), productId);
        ApiUtil.getServiceClass().addToWishList(HostelohaUtils.getAuthenticationToken(), wishListRequest).enqueue(new CallbackWithRetry<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HostelohaLog.debugOut("[REQ] addWishList  isSuccessful  :: " + response.isSuccessful());
            }
        });
    }

    public void removeWishList(int productId) {
        HostelohaLog.debugOut(" productId :: " + productId + "  userID :: " + HostelohaUtils.getUserId());
        ApiUtil.getServiceClass().removeFromWishlist(HostelohaUtils.getAuthenticationToken(), HostelohaUtils.getUserId(), productId).enqueue(new CallbackWithRetry<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HostelohaLog.debugOut("[REQ] removeWishList  isSuccessful  :: " + response.isSuccessful());
            }
        });
    }
}