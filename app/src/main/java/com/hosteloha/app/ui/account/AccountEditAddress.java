package com.hosteloha.app.ui.account;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hosteloha.R;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.AppLocation;
import com.hosteloha.databinding.FragmentAcceditAddressBinding;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class AccountEditAddress extends Fragment {

    private static final int PERMISSION_ID = 44;
    private FragmentAcceditAddressBinding mFgmtBinding = null;
    private static Activity mActivity;
    private static AppLocation appLocation = null;
    private AccountViewModel accountViewModel;
    private Context mContext = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFgmtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_accedit_address, container, false);
        accountViewModel =
                ViewModelProviders.of(this, new AccountViewModel(mContext, getActivity())).get(AccountViewModel.class);

        accountViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mFgmtBinding.fetchedAddress.setText(s);
            }
        });

        accountViewModel.getAddressList().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    mFgmtBinding.houseNum.setText(address);
                    String city = addresses.get(0).getLocality();
                    mFgmtBinding.city.setText(city);
                    String state = addresses.get(0).getAdminArea();
                    mFgmtBinding.state.setText(state);
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    mFgmtBinding.pincode.setText(postalCode);
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    HostelohaLog.debugOut(" address :: " + city + " , " + state +" known "+knownName
                    +" Feature :: "+addresses.get(0).toString() + addresses.get(0) );
                    // Feature is 18
                }
            }
        });
        mFgmtBinding.btnUseLocation.setOnClickListener(mOnClickListener);
        return mFgmtBinding.getRoot();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mFgmtBinding.btnUseLocation.getId()) {
                accountViewModel.requestLocationData();
            }
        }
    };

    public static void onLocationPermissionGranted() {
        HostelohaLog.debugOut(" Fragment onLocationPermissionGranted ");
        AccountViewModel.requestLocationData();
    }

    private void DialogNoAddressFound(){

    }
}
