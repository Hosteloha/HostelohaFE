package com.hosteloha.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.databinding.FragmentHomeBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding mFgmtHomeBinding = null;
    private static NavController navController = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mFgmtHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        initListener();
        return mFgmtHomeBinding.getRoot();
    }

    private void initListener() {
        mFgmtHomeBinding.btnChangeLocation.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mFgmtHomeBinding.btnChangeLocation.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToAccountEditAddress());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        HostelohaLog.debugOut(" FragmentHome -> onResume");
    }
}