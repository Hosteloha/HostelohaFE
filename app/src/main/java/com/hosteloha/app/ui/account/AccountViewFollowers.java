package com.hosteloha.app.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.beans.UserFollowers;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.ui.account.adapter.AccountViewFollowersAdapter;
import com.hosteloha.app.utils.AppProgressBar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a list of Items.
 */
public class AccountViewFollowers extends Fragment {

    private AccountViewModel accountViewModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AccountViewFollowers() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AccountViewFollowers newInstance(int columnCount) {
        AccountViewFollowers fragment = new AccountViewFollowers();
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
    }

    public View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accview_followers_list, container, false);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        registerObservers();
        AppProgressBar.showDefaultProgress(getActivity());

        return view;
    }

    public void registerObservers() {
        accountViewModel.getUserFollowersLive().observe(getViewLifecycleOwner(), new Observer<List<UserFollowers>>() {
            @Override
            public void onChanged(List<UserFollowers> userFollowers) {
                HostelohaLog.debugOut(" observer :: " + userFollowers.size());
                if(userFollowers.isEmpty()){
                    AppProgressBar.hideWithSnackBarMessage(getActivity(),"No followers found");
                }else{
                    AppProgressBar.hide();
                }
                // Set the adapter
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }
                    recyclerView.setAdapter(new AccountViewFollowersAdapter(userFollowers, accountViewModel));
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (accountViewModel != null) {
            accountViewModel.getUserFollowingsData();
            accountViewModel.getUserFollowersData();
        }
    }
}