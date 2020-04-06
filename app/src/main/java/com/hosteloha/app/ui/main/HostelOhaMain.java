package com.hosteloha.app.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.hosteloha.R;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostelOhaMain extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestSplashdata();
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

    public void requestSplashdata() {
        ApiUtil.getServiceClass().getCategoryMapList(HostelohaUtils.AUTHENTICATION_TOKEN).enqueue(new Callback<Map<String, Set<String>>>() {
            @Override
            public void onResponse(Call<Map<String, Set<String>>> call, Response<Map<String, Set<String>>> response) {

                Log.d("HostelOhaMain", "  categoriesMap  " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Map<String, Set<String>> categoriesMap = response.body();
                    if (categoriesMap != null)
                        HostelohaUtils.setAllCategoriesMap(categoriesMap);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable t) {
                Log.d("HostelOhaMain", "  onFailure  ");
            }
        });
    }
}
