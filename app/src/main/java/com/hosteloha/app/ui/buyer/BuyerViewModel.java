package com.hosteloha.app.ui.buyer;

import com.hosteloha.app.beans.PagedCategoryListModel;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.WishListRequest;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.AppFireDataBase;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class BuyerViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<ProductObject>> mAllProductsList;
    private MutableLiveData<List<ProductObject>> mCategoryProductsList;
    private Map<Integer, ProductObject> mProductMap = new HashMap<>();
    private PagedCategoryListModel mPagedCategoryListModel;
    private String mCategory_ID = "NONE";

    public BuyerViewModel() {
        mText = new MutableLiveData<>();
        mAllProductsList = new MutableLiveData<>();
        mCategoryProductsList = new MutableLiveData<>();
        mText.setValue("This is buyer fragment");
        req_getAllProducts();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<ProductObject>> getAllProducts() {
        return mAllProductsList;
    }

    public LiveData<List<ProductObject>> getCategoryProducts() {
        return mCategoryProductsList;
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
            mCategoryProductsList.postValue(new ArrayList<ProductObject>());
            mPagedCategoryListModel = null;
        }
        HostelohaLog.debugOut(" mCategory_ID " + mCategory_ID);
        if (mCategory_ID.equals("NONE"))
            req_getAllProducts();
        else
            req_getCategoryProducts(0);
    }

    public void req_getAllProducts() {

        ApiUtil.getServiceClass().getAllProducts(HostelohaUtils.getDefaultAuthenticationToken()).enqueue(new CallbackWithRetry<List<ProductObject>>() {
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

    public void req_getCategoryProducts(int pageNumber) {
        if (mPagedCategoryListModel != null && pageNumber >= mPagedCategoryListModel.getTotalPages())
            return;

        ApiUtil.getServiceClass().getAllProductsByCategoryPages(HostelohaUtils.getAuthenticationToken(), mCategory_ID, pageNumber,
                10, "quantity", "ASC").enqueue(new CallbackWithRetry<PagedCategoryListModel>() {
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
                    if (mPagedCategoryListModel == null || !mPagedCategoryListModel.appendData(pagedCategoryListModel))
                        mPagedCategoryListModel = pagedCategoryListModel;

                    mCategoryProductsList.postValue(mPagedCategoryListModel.getProductObjects());
                }
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