package com.hosteloha.app.ui.buyer;

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

public class BuyerFragment extends Fragment {

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
        return root;
    }
}