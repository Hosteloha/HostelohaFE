package com.hosteloha.app.ui.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    List<ProductObject> mArraylist;
    private OnItemClickListener mItemClickListener;

    public RecyclerAdapter(List arrayList) {
        mArraylist = arrayList;
    }

    public void setArrayList(List<ProductObject> arrayList) {
        mArraylist = arrayList;
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

        viewHolder.mProductTitle.setText(mArraylist.get(position).getTitle());
        viewHolder.mProductCost.setText(mArraylist.get(position).getSellingPrice()+"");
    }

    @Override
    public int getItemCount() {
        return mArraylist.size();
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
