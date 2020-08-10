package com.hosteloha.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.Define;
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
        mFgmtHomeBinding.btnShowAllCat.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnElectronics.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnBeauty.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnBooks.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnFashion.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnMusic.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnProject.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnRentgear.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnSports.setOnClickListener(mOnClickListener);
        mFgmtHomeBinding.layoutHomeCategories.btnTransport.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mFgmtHomeBinding.btnChangeLocation.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToAccountEditAddress());
            } else if (view.getId() == mFgmtHomeBinding.btnShowAllCat.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToHomeAllCategories());
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnElectronics.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_ELECTRONICS));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnBeauty.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_BEAUTY));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnBooks.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_BOOKS));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnFashion.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_FASHION));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnMusic.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_MUSIC));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnProject.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_PROJECT));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnRentgear.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_RENT));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnSports.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_SPORTS));
            } else if (view.getId() == mFgmtHomeBinding.layoutHomeCategories.btnTransport.getId()) {
                navController.navigate(HomeFragmentDirections.actionNavHomeToNavBuyer(Define.CAT_TRANSPORT));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        HostelohaLog.debugOut(" FragmentHome -> onResume");
    }
}