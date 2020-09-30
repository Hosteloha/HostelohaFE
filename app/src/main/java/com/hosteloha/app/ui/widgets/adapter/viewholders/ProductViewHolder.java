package com.hosteloha.app.ui.widgets.adapter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.ui.widgets.adapter.RecyclerAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView mProductImage;
    public TextView mProductTitle;
    public TextView mProductCost;
    public TextView mActualCost;
    public TextView mDiscount;

    public ProductObject mItem;
    public RecyclerAdapter.OnItemClickListener mItemClickListener;

    public ProductViewHolder(View view) {
        super(view);
        mProductImage = view.findViewById(R.id.product_image);
        mProductTitle = view.findViewById(R.id.product_title);
        mProductCost = view.findViewById(R.id.product_cost);
        mActualCost = view.findViewById(R.id.product_actual_cost);
        mDiscount = view.findViewById(R.id.product_discount);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null && mItem != null) {
            mItemClickListener.onItemClick(itemView, mItem.getProductId());
        }
    }
}
