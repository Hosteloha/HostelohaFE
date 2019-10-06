package com.hosteloha.app.ui.seller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SellerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SellerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is seller fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}