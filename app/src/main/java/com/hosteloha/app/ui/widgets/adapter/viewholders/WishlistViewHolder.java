package com.hosteloha.app.ui.widgets.adapter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.ui.widgets.adapter.RecyclerAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final ImageView mProductImage;
    public final TextView mIdView;
    public final TextView mProductTitle;
    public final TextView mProductActualCost;
    public final TextView mProductCost;
    public final TextView mProductDiscount;

    public ProductObject mItem;
    public static RecyclerAdapter.OnItemClickListener mItemClickListener;

    public WishlistViewHolder(View view) {
        super(view);
        mIdView = view.findViewById(R.id.item_number);
        mProductImage = view.findViewById(R.id.product_image);
        mProductTitle = view.findViewById(R.id.product_title);
        mProductActualCost = view.findViewById(R.id.product_actual_cost);
        mProductCost = view.findViewById(R.id.product_cost);
        mProductDiscount = view.findViewById(R.id.product_discount);
        view.setOnClickListener(this);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mItem + "'";
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION && mItemClickListener != null) {
            mItemClickListener.onItemClick(itemView, mItem.getProductId());
        }
    }
}
