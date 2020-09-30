package com.hosteloha.app.ui.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.ui.widgets.adapter.EndlessRecyclerViewScrollListener;
import com.hosteloha.app.ui.widgets.adapter.RecyclerAdapter;
import com.hosteloha.app.utils.AppSharedPrefs;
import com.hosteloha.app.utils.Define;
import com.hosteloha.databinding.FragmentBuyerBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BuyerFragment extends Fragment {

    FragmentBuyerBinding mBuyerBinding;
    private static final String TAG = BuyerFragment.class.getSimpleName();
    RecyclerAdapter mRecyclerAdapter;
    NavController mNavController = null;
    private BuyerViewModel buyerViewModel;
    String mCategory_ID = "NONE";
    List<ProductObject> productList = new ArrayList<ProductObject>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBuyerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buyer, container, false);
        buyerViewModel = ViewModelProviders.of(getActivity()).get(BuyerViewModel.class);
        if (getArguments() != null)
            mCategory_ID = BuyerFragmentArgs.fromBundle(getArguments()).getCategoryId();

        if (buyerViewModel != null)
            buyerViewModel.setCategoryId(mCategory_ID);
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
        mRecyclerAdapter = new RecyclerAdapter<ProductObject>(getContext(), RecyclerAdapter.ItemType.PRODUCT, mOnItemClickListener);
        mBuyerBinding.buyerRecyclerView.setAdapter(mRecyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mBuyerBinding.buyerRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                HostelohaLog.debugOut(" NextPage :: " + page + "  totalItems in Adapter ::  " + totalItemsCount + "  ::  " + mRecyclerAdapter.isLoading());
                if (buyerViewModel != null) {
                    mRecyclerAdapter.showLoading();
                    buyerViewModel.requestNextPageData();
                }
            }
        });

        mBuyerBinding.buyerRecyclerView.setLayoutManager(layoutManager);

        buyerViewModel.getProductsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ProductObject>>() {
            @Override
            public void onChanged(List<ProductObject> productObjects) {
                mRecyclerAdapter.setArrayList(productObjects);
                if (productObjects == null)
                    mRecyclerAdapter.showLoading();
                else {
                    mRecyclerAdapter.hideLoading();
                }
            }
        });

        AppSharedPrefs.storeCurrentViewTypeInPrefs(getContext(), Define.VIEW_BUYER);
    }

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
}