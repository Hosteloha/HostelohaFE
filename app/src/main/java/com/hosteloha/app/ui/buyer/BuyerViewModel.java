package com.hosteloha.app.ui.buyer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BuyerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BuyerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is buyer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}