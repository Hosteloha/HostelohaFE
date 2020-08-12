package com.hosteloha.app.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.ui.account.adapter.AccountViewWatchListAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a list of Items.
 */
public class AccountViewWatchList extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;

    AccountViewModel mAccountViewModel;

    AccountViewWatchListAdapter mWatchListAdapter;
    private NavController mNavController;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AccountViewWatchList() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AccountViewWatchList newInstance(int columnCount) {
        AccountViewWatchList fragment = new AccountViewWatchList();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mAccountViewModel = ViewModelProviders.of(getActivity()).get(AccountViewModel.class);
        AccountViewModel.getUserWishList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accview_watchlist, container, false);
        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mWatchListAdapter = new AccountViewWatchListAdapter(getActivity());
        mWatchListAdapter.setOnItemClickListener(mOnItemClickListener);
        recyclerView.setAdapter(mWatchListAdapter);

        AccountViewModel.getWishListLiveData().observe(getActivity(), new Observer<List<ProductObject>>() {
            @Override
            public void onChanged(List<ProductObject> productObjects) {
                mWatchListAdapter.setWishListData(productObjects);
            }
        });

        return view;
    }

    private AccountViewWatchListAdapter.OnItemClickListener mOnItemClickListener = new AccountViewWatchListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int productId) {
            HostelohaLog.debugOut(" main product view onItemClick  " + productId);
            if (mNavController != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", productId);
                mNavController.navigate(R.id.action_accountViewWatchList_to_buyerProductFragment, bundle);
            }
        }
    };
}