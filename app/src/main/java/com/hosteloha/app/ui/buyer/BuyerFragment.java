package com.hosteloha.app.ui.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.list.data.AllProductsSubject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.service.HostelohaService;
import com.hosteloha.app.ui.buyer.adapter.RecyclerAdapter;
import com.hosteloha.app.utils.AppSharedPrefs;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;
import com.hosteloha.databinding.FragmentBuyerBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BuyerFragment extends Fragment {

    private HostelohaService mHostelohaService = null;
    FragmentBuyerBinding mBuyerBinding;
    private static final String TAG = BuyerFragment.class.getSimpleName();
    RecyclerAdapter mRecyclerAdapter;
    NavController mNavController = null;
    private BuyerViewModel buyerViewModel;
    private List<ProductObject> mArrayList = new ArrayList<ProductObject>();
    private RecyclerAdapter.OnItemClickListener mOnItemClickListener = new RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int productId) {
            HostelohaLog.debugOut(" main product view onItemClick  " + productId);
            if (mNavController != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", productId);
                mNavController.navigate(R.id.action_nav_buyer_to_buyerProductFragment, bundle);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBuyerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buyer, container, false);


        AppSharedPrefs.storeCurrentViewTypeInPrefs(getContext(), Define.VIEW_BUYER);
        buyerViewModel = new ViewModelProvider(this).get(BuyerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_buyer, container, false);
        buyerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mBuyerBinding.textBuyer.setText(s);
            }
        });

        List<ProductObject> productList = AllProductsSubject.getAllProductsSubject().getProductsList();

        mRecyclerAdapter = new RecyclerAdapter(getContext(), productList, mBuyerBinding.buyerRecyclerView);
        mRecyclerAdapter.setOnItemClickListener(mOnItemClickListener);
        mBuyerBinding.buyerRecyclerView.setAdapter(mRecyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mBuyerBinding.buyerRecyclerView.setLayoutManager(layoutManager);


        mHostelohaService = HostelohaUtils.getHostelohaService(getContext());
        if (mHostelohaService != null) {
            mHostelohaService.req_getAllProducts();
        }
        return mBuyerBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        AppSharedPrefs.storeCurrentViewTypeInPrefs(getContext(), Define.VIEW_BUYER);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);
    }


}