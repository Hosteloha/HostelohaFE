package com.hosteloha.app.ui.account;

import android.app.Activity;
import android.content.Context;
import android.location.Address;

import com.hosteloha.app.utils.AppLocation;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AccountViewModel extends ViewModel implements ViewModelProvider.Factory {

    private static Context mContext = null;
    private static Activity mActivity;
    private static AppLocation appLocation = null;

    public static MutableLiveData<String> mText;
    public static MutableLiveData<List<Address>> mAddressList;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user account fragment");
    }

    public AccountViewModel(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mText = new MutableLiveData<>();
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

    public LiveData<List<Address>> getAddressList() {
        return mAddressList;
    }

    public static void requestLocationData() {
        if (appLocation == null) {
            appLocation = new AppLocation(mContext, mActivity);
        }
        appLocation.getLastKnownLocation(mText, mAddressList);
    }
}