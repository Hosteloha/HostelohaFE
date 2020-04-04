package com.hosteloha.app.ui.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    List<ProductObject> mAllProducts;
    private OnItemClickListener mItemClickListener;

    public RecyclerAdapter(List arrayList) {
        mAllProducts = arrayList;
    }

    public void setArrayList(List<ProductObject> arrayList) {
        mAllProducts = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_recyclerview, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int position) {

        viewHolder.mProductTitle.setText(mAllProducts.get(position).getTitle());
        viewHolder.mProductCost.setText("RS " + mAllProducts.get(position).getSellingPrice());
        viewHolder.mActualCost.setText("RS " + mAllProducts.get(position).getCostPrice());
        if (mAllProducts.get(position).getSellingPrice() < mAllProducts.get(position).getCostPrice() && mAllProducts.get(position).getCostPrice() != 0) {

            int disocunt = 100 * (mAllProducts.get(position).getCostPrice() - mAllProducts.get(position).getSellingPrice()) / mAllProducts.get(position).getCostPrice();
            if (disocunt != 0)
                viewHolder.mDiscount.setText(disocunt + " %");
            else
                viewHolder.mDiscount.setVisibility(View.GONE);
        } else {
            viewHolder.mDiscount.setVisibility(View.GONE);
            viewHolder.mActualCost.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAllProducts.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mProductImage;
        TextView mProductTitle;
        TextView mProductCost;
        TextView mActualCost;
        TextView mDiscount;

        public RecyclerViewHolder(View view) {
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
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, position);
            }
        }
    }
}
