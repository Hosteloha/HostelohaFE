package com.hosteloha.app.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.databinding.FragmentAccountBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding mFgmtAccBinding = null;
    private static NavController navController = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mFgmtAccBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        initListener();
        return mFgmtAccBinding.getRoot();
    }

    private void initListener() {
        mFgmtAccBinding.editAccountDetails.setOnClickListener(mOnClickListener);
        mFgmtAccBinding.addressLayout.setOnClickListener(mOnClickListener);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mFgmtAccBinding.editAccountDetails.getId()) {
                navController.navigate(AccountFragmentDirections.actionNavAccountToAccountEditDetails());
            } else if (view.getId() == mFgmtAccBinding.addressLayout.getId()) {
                navController.navigate(AccountFragmentDirections.actionNavAccountToAccountEditAddress());
            }
        }
    };
}