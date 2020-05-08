package com.hosteloha.app.ui.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.data.AllProductsSubject;
import com.hosteloha.databinding.FragmentBuyerProductBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class BuyerProductFragment extends Fragment {

    FragmentBuyerProductBinding mBuyerProductBinding;
    int mProductPosition = -1;
    ProductObject mProductObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductPosition = getArguments().getInt("product_position");
        if (AllProductsSubject.getAllProductsSubject().getProductsList() != null && AllProductsSubject.getAllProductsSubject().getProductsList().size() >= mProductPosition)
            mProductObject = AllProductsSubject.getAllProductsSubject().getProductsList().get(mProductPosition);

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

        List<String> productImages = mProductObject.getProduct_images();
        ImageView imageView = mBuyerProductBinding.include.productImages;
        String thumbImageURL = null;
        if (productImages.size() > 0) {
            thumbImageURL = productImages.get(0);
        }
        Glide.with(this).load(thumbImageURL).placeholder(R.drawable.ic_image_dt).into(imageView);
        mBuyerProductBinding.include.productTitle.setText(mProductObject.getTitle());
        mBuyerProductBinding.include.subTitle.setText(mProductObject.getSubtitle());
        // Copied from list
        int productSellingPrice = mProductObject.getSellingPrice();
        String formattedSP = this.getResources().getString(R.string.product_sp_value, productSellingPrice);
        mBuyerProductBinding.include.costPrice.setText(formattedSP);

        int productCostPrice = mProductObject.getCostPrice();
        String formattedCP = this.getResources().getString(R.string.product_cp_value, productCostPrice);
        mBuyerProductBinding.include.mActualCost.setText(formattedCP);

        if (mProductObject.getSellingPrice() < mProductObject.getCostPrice() && mProductObject.getCostPrice() != 0) {
            int discount = 100 * (mProductObject.getCostPrice() - mProductObject.getSellingPrice()) / mProductObject.getCostPrice();
            if (discount != 0) {
                String formattedDiscount = this.getResources().getString(R.string.product_discount_value, discount);
                mBuyerProductBinding.include.productDiscount.setText(formattedDiscount);
            } else {
                mBuyerProductBinding.include.productDiscount.setVisibility(View.GONE);
            }
        } else {
            mBuyerProductBinding.include.productDiscount.setVisibility(View.GONE);
            mBuyerProductBinding.include.mActualCost.setVisibility(View.GONE);
        }
        //COpy OVer
//        mBuyerProductBinding.include.costPrice.setText(mProductObject.getSellingPrice() + "");
        mBuyerProductBinding.include.description.setText(mProductObject.getDescription());
        mBuyerProductBinding.include.productCondition.setText(mProductObject.getCondition_id() + "");
        mBuyerProductBinding.include.productTitle.setText(mProductObject.getTitle());
        mBuyerProductBinding.include.productSpecifics.setVisibility(View.VISIBLE);


    }
}
