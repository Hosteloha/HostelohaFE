package com.hosteloha.app.ui.buyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.beans.ApiObject;
import com.hosteloha.app.retroapi.ApiUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerFragment extends Fragment {

    private static final String TAG = BuyerFragment.class.getSimpleName();
    private BuyerViewModel buyerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        ApiUtil.getServiceClass().getAllPost().enqueue(new Callback<List<ApiObject>>() {
            @Override
            public void onResponse(Call<List<ApiObject>> call, Response<List<ApiObject>> response) {
                if (response.isSuccessful()) {
                    List<ApiObject> postList = response.body();
                    Log.d(TAG, "Returned count " + postList.size());
                    for (ApiObject item : postList
                    ) {
                        CharSequence previousText = textView.getText();
                        textView.setText(previousText + "\n Title :: " + item.getTitle() +
                                "\n Des : " + item.getDescription() + "\n");
                    }
//                    NewAdapter adapter = new NewAdapter(getApplicationContext(), postList);
//                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ApiObject>> call, Throwable t) {
                //showErrorMessage();
                Log.d(TAG, "error loading from API");
            }

        });
        return root;
    }
}