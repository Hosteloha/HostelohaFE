package com.hosteloha.app.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AboutViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is about app fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}