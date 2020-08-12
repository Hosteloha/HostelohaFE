package com.hosteloha.app.ui.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    List<ProductObject> mAllProducts;
    private OnItemClickListener mItemClickListener;
    private Context mContext;
    private RecyclerView mRecyclerView;


    public RecyclerAdapter(Context context, List arrayList, RecyclerView recyclerView) {
        mContext = context;
        mAllProducts = arrayList;
        mRecyclerView = recyclerView;
    }

    public void setArrayList(List<ProductObject> arrayList) {
        mAllProducts.clear();
        mAllProducts = new ArrayList<>(arrayList);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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
        ProductObject product = mAllProducts.get(position);
        // Set only the first image, after wards we can put effects to change images with time.
        List<String> thumbImages = product.getProduct_images();
        String thumbImageURL = null;
        if (thumbImages.size() > 0) {
            thumbImageURL = thumbImages.get(0);
        }
        Glide.with(mContext).load(thumbImageURL).placeholder(R.drawable.ic_image_dt).into(viewHolder.mProductImage);

        //-------
        viewHolder.mProductTitle.setText(product.getTitle());

        int productSellingPrice = product.getSellingPrice();
        String formattedSP = mContext.getResources().getString(R.string.product_sp_value, productSellingPrice);
        viewHolder.mProductCost.setText(formattedSP);

        int productCostPrice = product.getCostPrice();
        String formattedCP = mContext.getResources().getString(R.string.product_cp_value, productCostPrice);
        viewHolder.mActualCost.setText(formattedCP);

        if (product.getSellingPrice() < product.getCostPrice() && product.getCostPrice() != 0) {
            int discount = 100 * (product.getCostPrice() - product.getSellingPrice()) / product.getCostPrice();
            if (discount != 0) {
                String formattedDiscount = mContext.getResources().getString(R.string.product_discount_value, discount);
                viewHolder.mDiscount.setText(formattedDiscount);
            } else {
                viewHolder.mDiscount.setVisibility(View.GONE);
            }
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
        void onItemClick(View itemView, int productId);
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
            if (position != RecyclerView.NO_POSITION && mItemClickListener != null && mAllProducts != null && position <= mAllProducts.size()) {
                mItemClickListener.onItemClick(itemView, mAllProducts.get(position).getProductId());
            }
        }
    }

}
