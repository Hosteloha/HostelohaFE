package com.hosteloha.app.ui.payments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaymentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PaymentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is payments fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}