package com.hosteloha.app.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.beans.UserDetails;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.AppProgressBar;

public class AccountEditDetails extends Fragment {

    private AccountViewModel accountViewModel;

    public AccountEditDetails() {}

    public static AccountEditDetails newInstance(String param1, String param2) {
        AccountEditDetails fragment = new AccountEditDetails();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        registerObservers();
        AppProgressBar.showDefaultProgress(getActivity());
        return inflater.inflate(R.layout.fragment_accedit_details, container, false);
    }

    public void registerObservers() {
        accountViewModel.getUserDetailsLive().observe(getViewLifecycleOwner(), new Observer<UserDetails>() {
            @Override
            public void onChanged(UserDetails userDetails) {
                HostelohaLog.debugOut(" fetched userdetails  :: " + (userDetails!=null));
                if(userDetails == null){
                    AppProgressBar.hideWithSnackBarMessage(getActivity(),"Unable to fetch details");
                }else{
                    AppProgressBar.hide();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(accountViewModel!=null){
            accountViewModel.getUserDetailsData();
        }
    }
}
