package com.hosteloha.app.ui.account;

import android.app.Activity;
import android.content.Context;
import android.location.Address;

import com.google.gson.Gson;
import com.hosteloha.app.beans.AddFollowerRequest;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserFollowers;
import com.hosteloha.app.beans.UserFollowings;
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
    private static MutableLiveData<List<ProductObject>> mWishListLiveData = null;
    public static MutableLiveData<List<UserFollowers>> mUserFollowersLive = null;
    public static MutableLiveData<List<UserFollowings>> mUserFollowingLive = null;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user account fragment");
        if (mUserFollowersLive == null) {
            mUserFollowersLive = new MutableLiveData<>();
        }
        if (mUserFollowingLive == null) {
            mUserFollowingLive = new MutableLiveData<>();
        }
        if (mWishListLiveData == null)
            mWishListLiveData = new MutableLiveData<>();
    }

    public AccountViewModel(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mText = new MutableLiveData<>();
        if(mUserFollowersLive == null){
            mUserFollowersLive = new MutableLiveData<>();
        }
        if(mUserFollowingLive == null){
            mUserFollowingLive = new MutableLiveData<>();
        }
        mAddressList = new MutableLiveData<>();
        mText.setValue("//TODO Previous address");
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AccountViewModel(mContext, mActivity);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static MutableLiveData<List<ProductObject>> getWishListLiveData() {
        return mWishListLiveData;
    }

    public MutableLiveData<List<UserFollowers>> getUserFollowersLive() {
        return mUserFollowersLive;
    }

    public MutableLiveData<List<UserFollowings>> getUserFollowingLive() {
        return mUserFollowingLive;
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
                        mWishListLiveData.postValue(mWishListProducts);
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

    public void addUserFollowers(int sellerID) {
        HostelohaLog.debugOut(" addUserFollowers :: sellerToBeFollowed " + sellerID + "  userID :: " + HostelohaUtils.getUserId() );
        AddFollowerRequest addFollowerRequest = new AddFollowerRequest(HostelohaUtils.getUserId(), sellerID );
        ApiUtil.getServiceClass().addFollower(HostelohaUtils.getAuthenticationToken(), addFollowerRequest).enqueue(new CallbackWithRetry<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HostelohaLog.debugOut("[REQ] addUserFollowers  isSuccessful  :: " + response.isSuccessful());
                HostelohaUtils.showSnackBarNotification(mActivity, " Add follower : "+(response.isSuccessful()));

            }
        });
    }

    public void removeUserFollower(int sellerID, int followerID){
        HostelohaLog.debugOut(" removeUserFollower :: sellerID :: " + sellerID + "  followerID :: " + followerID);
        ApiUtil.getServiceClass().removeFollower(HostelohaUtils.getAuthenticationToken(), sellerID, followerID).enqueue(new CallbackWithRetry<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HostelohaLog.debugOut("[REQ] removeUserFollower  isSuccessful  :: " + response.isSuccessful());
                HostelohaUtils.showSnackBarNotification(mActivity, " Remove follower : "+(response.isSuccessful()));
            }
        });
    }

    public void getUserFollowersData() {
        HostelohaLog.debugOut(" getUserFollowers :: " + "  userID :: " + HostelohaUtils.getUserId());
        ApiUtil.getServiceClass().getUserFollowers(HostelohaUtils.getAuthenticationToken(), HostelohaUtils.getUserId()).enqueue(new CallbackWithRetry<List<UserFollowers>>() {
            @Override
            public void onResponse(Call<List<UserFollowers>> call, Response<List<UserFollowers>> response) {
                HostelohaLog.debugOut("[REQ] getUserFollowers  isSuccessful  :: " + response.isSuccessful());
                if(response.isSuccessful()){
                    List<UserFollowers> mUserFollowersList = response.body();
                    HostelohaLog.debugOut("[REQ] getUserFollowers  followers  :: " + mUserFollowersList.size());
                    mUserFollowersLive.setValue(mUserFollowersList);
                }else{
                    // TODO :: Error dialogue
                    List<UserFollowers> mUserFollowersList = new ArrayList<>();
                    mUserFollowersLive.setValue(mUserFollowersList);
                }
            }
        });
    }

    public void getUserFollowingsData() {
        HostelohaLog.debugOut(" getUserFollowings :: " + "  userID :: " + HostelohaUtils.getUserId());
        ApiUtil.getServiceClass().getUserFollowings(HostelohaUtils.getAuthenticationToken(), HostelohaUtils.getUserId()).enqueue(new CallbackWithRetry<List<UserFollowings>>() {
            @Override
            public void onResponse(Call<List<UserFollowings>> call, Response<List<UserFollowings>> response) {
                HostelohaLog.debugOut("[REQ] getUserFollowings  isSuccessful  :: " + response.isSuccessful());
                if(response.isSuccessful()){
                    List<UserFollowings> mUserFollowingsList = response.body();
                    HostelohaLog.debugOut("[REQ] getUserFollowings followings  :: " + mUserFollowingsList.size());
                    mUserFollowingLive.setValue(mUserFollowingsList);
                }else{
                    // TODO :: Error dialogue
                    List<UserFollowings> mUserFollowingsList = new ArrayList<>();
                    mUserFollowingLive.setValue(mUserFollowingsList);
                }
            }
        });
    }
}