package com.hosteloha.app.ui.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hosteloha.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SellerFragment extends Fragment {

    private SellerViewModel sellerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sellerViewModel =
                ViewModelProviders.of(this).get(SellerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_seller, container, false);
        final TextView textView = root.findViewById(R.id.text_seller);
        sellerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}