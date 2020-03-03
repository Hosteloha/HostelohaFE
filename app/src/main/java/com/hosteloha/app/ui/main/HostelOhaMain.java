package com.hosteloha.app.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HostelOhaMain extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                if (HostelohaUtils.getUserLoginInfo(getContext())) {
                    String viewType = HostelohaUtils.getPreviousViewType(getContext());
                    boolean isPreviousViewBuyer = (viewType.equals(Define.VIEW_BUYER));
                    if (isPreviousViewBuyer) {
                        navController.navigate(R.id.action_hostelOhaMain_to_nav_buyer);
                    } else {
                        navController.navigate(R.id.action_hostelOhaMain_to_nav_seller);
                    }
                } else {
                    navController.navigate(R.id.action_hostelOhaMain_to_mainLoginFragment);
                }
            }
        }, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hostel_oha_main, container, false);
    }

}
