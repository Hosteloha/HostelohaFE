package com.hosteloha.app.ui.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.utils.HostelohaUtils;
import com.hosteloha.databinding.FragmentBuyerProductBinding;


public class BuyerProductFragment extends Fragment {

    FragmentBuyerProductBinding mBuyerProductBinding;
    int mProductPosition = -1;
    ProductObject mProductObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductPosition = getArguments().getInt("product_position");
        if (HostelohaUtils.getAllProducts() != null && HostelohaUtils.getAllProducts().size() >= mProductPosition)
            mProductObject = HostelohaUtils.getAllProducts().get(mProductPosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBuyerProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buyer_product, container, false);

        return mBuyerProductBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mBuyerProductBinding == null || mProductObject == null)
            return;

        mBuyerProductBinding.include.productTitle.setText(mProductObject.getTitle());
        mBuyerProductBinding.include.subTitle.setText(mProductObject.getSubtitle());
        mBuyerProductBinding.include.costPrice.setText(mProductObject.getSellingPrice() + "");
        mBuyerProductBinding.include.description.setText(mProductObject.getDescription());
        mBuyerProductBinding.include.productCondition.setText(mProductObject.getCondition_id() + "");
        mBuyerProductBinding.include.productTitle.setText(mProductObject.getTitle());
        mBuyerProductBinding.include.productSpecifics.setVisibility(View.GONE);


    }
}
