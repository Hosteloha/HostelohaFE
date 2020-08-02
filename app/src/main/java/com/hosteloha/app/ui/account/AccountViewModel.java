package com.hosteloha.app.ui.account;

import android.app.Activity;
import android.content.Context;
import android.location.Address;

import com.google.gson.Gson;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.AppLocation;
import com.hosteloha.app.utils.HostelohaUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AccountViewModel extends ViewModel implements ViewModelProvider.Factory {

    private static Context mContext = null;
    private static Activity mActivity;
    private static AppLocation appLocation = null;
    private static List<ProductObject> mWishListProducts;
    private static List<Integer> mWishListProductIDs = new ArrayList<>();

    public static MutableLiveData<String> mText;
    public static MutableLiveData<List<Address>> mAddressList;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user account fragment");
        getUserWishList();
    }

    public AccountViewModel(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mText = new MutableLiveData<>();
        mAddressList = new MutableLiveData<>();
        mText.setValue("//TODO Previous address");
        getUserWishList();
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AccountViewModel(mContext, mActivity);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Address>> getAddressList() {
        return mAddressList;
    }

    public static List<Integer> getWishListProductsIDs() {
        getUserWishList();
        return mWishListProductIDs;
    }

    public static void requestLocationData() {
        if (appLocation == null) {
            appLocation = new AppLocation(mContext, mActivity);
        }
        appLocation.getLastKnownLocation(mText, mAddressList);
    }

    public static void getUserWishList() {
        HostelohaLog.debugOut(" userId :: " + HostelohaUtils.getUserId());
        ApiUtil.getServiceClass().getWishList(HostelohaUtils.getAuthenticationToken(), HostelohaUtils.getUserId()).enqueue(new CallbackWithRetry<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HostelohaLog.debugOut(" getUserWishList -- isSuccessful  :: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray jsonArray = json.getJSONArray("products");
                        ProductObject[] productObjects = (new Gson()).fromJson(jsonArray.toString(), ProductObject[].class);
                        mWishListProducts = Arrays.asList(productObjects);
                        mWishListProductIDs.clear();
                        for (ProductObject obj : mWishListProducts) {
                            mWishListProductIDs.add(obj.getProductId());
                        }
                        HostelohaLog.debugOut("  getUserWishList  " + mWishListProductIDs.toString());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                } else
                    mWishListProductIDs.clear();
            }
        });
    }
}