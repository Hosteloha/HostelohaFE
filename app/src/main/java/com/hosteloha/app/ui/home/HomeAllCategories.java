package com.hosteloha.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.utils.Define;
import com.hosteloha.databinding.FragmentHomeviewAllcategoriesBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeAllCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeAllCategories extends Fragment {

    private FragmentHomeviewAllcategoriesBinding mFgmtAllCatBinding = null;
    private static NavController navController = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeAllCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeAllCategories.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeAllCategories newInstance(String param1, String param2) {
        HomeAllCategories fragment = new HomeAllCategories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        // Inflate the layout for this fragment
        mFgmtAllCatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_homeview_allcategories, container, false);
        initListener();
        return mFgmtAllCatBinding.getRoot();
    }

    private void initListener() {
        mFgmtAllCatBinding.allCatItem.btnElectronics.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnBeauty.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnBooks.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnFashion.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnMusic.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnProject.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnRentgear.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnSports.setOnClickListener(mOnClickListener);
        mFgmtAllCatBinding.allCatItem.btnTransport.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mFgmtAllCatBinding.allCatItem.btnElectronics.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_ELECTRONICS));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnBeauty.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_BEAUTY));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnBooks.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_BOOKS));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnFashion.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_FASHION));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnMusic.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_MUSIC));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnProject.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_PROJECT));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnRentgear.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_RENT));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnSports.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_SPORTS));
            } else if (view.getId() == mFgmtAllCatBinding.allCatItem.btnTransport.getId()) {
                navController.navigate(HomeAllCategoriesDirections.
                        actionHomeAllCategoriesToNavBuyer(Define.CAT_TRANSPORT));
            }
        }
    };
}