package com.hosteloha.app.datarepository;

import com.hosteloha.app.datarepository.beans.PagedCategoryListModel;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.beans.WishListRequest;
import com.hosteloha.app.datarepository.retroapi.ApiUtil;
import com.hosteloha.app.datarepository.retroapi.CallbackWithRetry;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.AppFireDataBase;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class RetroClientApi {

    private static RetroClientApi mRetroClientApi = new RetroClientApi();

    private String mCategoryID;
    private int mAllProductsPageNumber;
    private int mPageNumber;
    private String mSortBy;
    private String mSortOrder;
    private MutableLiveData<List<ProductObject>> mAllProductsListLiveData;
    private MutableLiveData<List<ProductObject>> mCategoryProductsListLiveData;
    private MutableLiveData<ProductObject> mProductObjectLiveData;
    private MutableLiveData<Boolean> mIsRequestInProgress;
    private int mProductId;

    private RetroClientApi() {
        mAllProductsListLiveData = new MutableLiveData<>();
        mCategoryProductsListLiveData = new MutableLiveData<>();
        mProductObjectLiveData = new MutableLiveData<>();
        mIsRequestInProgress = new MutableLiveData<>();
    }

    public static RetroClientApi getInstance() {
        return mRetroClientApi;
    }

    public MutableLiveData<Boolean> getIsRequestInProgressLiveData() {
        return mIsRequestInProgress;
    }

    public LiveData<List<ProductObject>> getAllProductsLiveData() {
        return mAllProductsListLiveData;
    }

    public LiveData<ProductObject> getProductLiveData() {
        return mProductObjectLiveData;
    }

    public LiveData<List<ProductObject>> getCategoryProductsLiveData() {
        return mCategoryProductsListLiveData;
    }

    public synchronized void req_getAllProductsByPages(int pageNumber, String sortBy, String sortingOrder) {
        if (mAllProductsPageNumber == pageNumber && mSortBy == sortBy && mSortOrder == sortingOrder && mAllProductsListLiveData.getValue() != null && !mAllProductsListLiveData.getValue().isEmpty())
            return;
        if (pageNumber == 0)
            mAllProductsListLiveData.postValue(null);
        mAllProductsPageNumber = pageNumber;
        mSortBy = sortBy;
        mSortOrder = sortingOrder;
        ApiUtil.getServiceClass().getAllProductsByPages(HostelohaUtils.getAuthenticationToken(), pageNumber, 20, mSortBy, mSortOrder).enqueue(new CallbackWithRetry<PagedCategoryListModel>() {
            @Override
            public void onResponse(Call<PagedCategoryListModel> call, Response<PagedCategoryListModel> response) {
                HostelohaLog.debugOut("[REQ] getAllProductsByPages  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    PagedCategoryListModel pagedCategoryListModel = response.body();
                    HostelohaLog.debugOut("[REQ] getAllProductsByPages ===== >count ::  ---> " + pagedCategoryListModel.getProductObjects().size() + " page Number :: " + pagedCategoryListModel.getCurrentPageNumber());

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

                    if (mAllProductsPageNumber == 0)
                        mAllProductsListLiveData.postValue(pagedCategoryListModel.getProductObjects());
                    else {
                        List<ProductObject> currentList = mAllProductsListLiveData.getValue();
                        currentList.addAll(pagedCategoryListModel.getProductObjects());
                        mAllProductsListLiveData.postValue(currentList);
                    }
                    mIsRequestInProgress.postValue(false);
                }
            }
        });
    }


    public synchronized void req_getCategoryProducts(int pageNumber, String categoryId, String sortBy, String sortingOrder) {
        if (mPageNumber == pageNumber && mCategoryID == categoryId && mSortBy == sortBy && mSortOrder == sortingOrder)
            return;
        if (pageNumber == 0 && categoryId != mCategoryID)
            mCategoryProductsListLiveData.postValue(null);

        mIsRequestInProgress.postValue(true);
        mPageNumber = pageNumber;
        mCategoryID = categoryId;
        mSortBy = sortBy;
        mSortOrder = sortingOrder;

        ApiUtil.getServiceClass().getAllProductsByCategoryPages(HostelohaUtils.getAuthenticationToken(), categoryId, pageNumber,
                20, sortBy, sortingOrder).enqueue(new CallbackWithRetry<PagedCategoryListModel>() {
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
                        mCategoryProductsListLiveData.postValue(pagedCategoryListModel.getProductObjects());
                    else {
                        List<ProductObject> currentList = mCategoryProductsListLiveData.getValue();
                        currentList.addAll(pagedCategoryListModel.getProductObjects());
                        mCategoryProductsListLiveData.postValue(currentList);
                    }
                    mIsRequestInProgress.postValue(false);
                }
            }
        });
    }

    public MutableLiveData<ProductObject> req_ProductObjectByID(int productId) {
        if (mProductId == productId)
            return mProductObjectLiveData;
        mProductId = productId;

        ApiUtil.getServiceClass().getProductById(HostelohaUtils.getAuthenticationToken(), productId).enqueue(new CallbackWithRetry<ProductObject>() {
            @Override
            public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                if (response.isSuccessful()) {
                    ProductObject productObject = response.body();
                    if (productObject != null)
                        mProductObjectLiveData.setValue(productObject);
                }
            }
        });
        return mProductObjectLiveData;
    }

    public void addWishList(int productId) {
        HostelohaLog.debugOut(" productId :: " + productId + "  userID :: " + HostelohaUtils.getUserId());
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
