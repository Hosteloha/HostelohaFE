package com.hosteloha.app.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.service.HostelohaService;
import com.hosteloha.app.utils.AppSharedPrefs;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HostelohaMain extends Fragment {

    private HostelohaService mHostelohaService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHostelohaService = HostelohaUtils.getHostelohaService(getContext());
        if (mHostelohaService != null)
            mHostelohaService.getSplashData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                if (AppSharedPrefs.getUserLoginInfo(getContext())) {
                    if (Define.isPreviousViewCheckEnabled) {
                        String viewType = AppSharedPrefs.getPreviousViewType(getContext());
                        boolean isPreviousViewBuyer = (viewType.equals(Define.VIEW_BUYER));
                        if (isPreviousViewBuyer) {
                            navController.navigate(HostelohaMainDirections.actionHostelOhaMainToNavBuyer());
                        } else {
                            navController.navigate(HostelohaMainDirections.actionHostelOhaMainToNavSeller());
                        }
                    } else {
                        navController.navigate(HostelohaMainDirections.actionHostelOhaMainToNavHome());
                    }
                } else {
                    navController.navigate(HostelohaMainDirections.actionHostelOhaMainToMainLoginFragment());
                }
            }
        }, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hosteloha_main, container, false);
    }

}
