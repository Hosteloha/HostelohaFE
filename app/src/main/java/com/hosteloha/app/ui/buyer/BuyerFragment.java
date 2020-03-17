package com.hosteloha.app.ui.buyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.ui.buyer.adapter.RecyclerAdapter;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerFragment extends Fragment {

    private static final String TAG = BuyerFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    NavController mNavController = null;
    private BuyerViewModel buyerViewModel;
    private ArrayList<String> mArrayList = new ArrayList<String>();
    private RecyclerAdapter.OnItemClickListener mOnItemClickListener = new RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {

            Log.d("HostelOha", " main product view onItemClick  " + position);
            if (mNavController != null) {
                mNavController.navigate(R.id.action_nav_buyer_to_buyerProductFragment);
            }

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HostelohaUtils.storeCurrentViewTypeInPrefs(getContext(), Define.VIEW_BUYER);
        buyerViewModel =
                ViewModelProviders.of(this).get(BuyerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_buyer, container, false);
        final TextView textView = root.findViewById(R.id.text_buyer);
        buyerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mRecyclerView = root.findViewById(R.id.buyer_recyclerView);
        mRecyclerAdapter = new RecyclerAdapter(mArrayList);
        mRecyclerAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);


        // To dismiss the dialog


        ApiUtil.getServiceClass().getAllPost(HostelohaUtils.AUTHENTICATION_TOKEN).enqueue(new Callback<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                if (response.isSuccessful()) {
                    List<ProductObject> postList = response.body();
                    Log.d(TAG, "Returned count " + postList.size());
                    mArrayList.clear();
                    for (ProductObject item : postList) {
                        mArrayList.add(item.getDescription());
                        CharSequence previousText = textView.getText();
                        textView.setText(previousText + "\n ID :: " + item.getSubtitle() +
                                "Title : " + item.getDescription() + "\n");
                    }
                    mRecyclerAdapter.setArrayList(mArrayList);

                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable t) {
                //showErrorMessage();
                String message = "Failed to fetch data";
                HostelohaUtils.showSnackBarNotification(Objects.requireNonNull(getActivity()), message);
                Log.d(TAG, "error loading from API");
            }

        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        HostelohaUtils.storeCurrentViewTypeInPrefs(getContext(), Define.VIEW_BUYER);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);
    }

}