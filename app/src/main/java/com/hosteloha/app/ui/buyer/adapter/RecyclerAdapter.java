package com.hosteloha.app.ui.buyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.ProductObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PRODUCT_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;

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
        if (mAllProducts != null)
            mAllProducts.clear();
        if (arrayList == null)
            mAllProducts = new ArrayList<>();
        else
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType) {
            case PRODUCT_TYPE:
                view = layoutInflater.inflate(R.layout.item_recyclerview, viewGroup, false);
                return new RecyclerViewHolder(view);

            case LOADING_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            default:
                view = layoutInflater.inflate(R.layout.item_recyclerview, viewGroup, false);
                return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        switch (itemViewType) {
            case PRODUCT_TYPE:
                RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
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
    }

    @Override
    public int getItemCount() {
        return mAllProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mAllProducts.get(position).getTitle() == "Loading")
            return LOADING_TYPE;
        else
            return PRODUCT_TYPE;
    }

    public boolean isLoading() {
        if (mAllProducts != null) {
            if (mAllProducts.size() > 0) {
                return mAllProducts.get(mAllProducts.size() - 1).getTitle() != null && mAllProducts.get(mAllProducts.size() - 1).getTitle().equals("Loading");
            }
        }
        return false;
    }

    public void showLoading() {
        if (!isLoading()) {
            if (mAllProducts == null)
                mAllProducts = new ArrayList<>();
            ProductObject productObject = new ProductObject();
            productObject.setTitle("Loading");
            mAllProducts.add(productObject);
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if (isLoading()) {
            for (int i = mAllProducts.size() - 1; i >= 0; i--) {
                if (mAllProducts.get(i).getTitle().equals("LOADING...")) {
                    mAllProducts.remove(mAllProducts.get(i));
                }
            }
            notifyDataSetChanged();
        }
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

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View view) {
            super(view);
        }
    }

}
