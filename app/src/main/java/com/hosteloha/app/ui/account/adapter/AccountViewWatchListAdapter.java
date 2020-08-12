package com.hosteloha.app.ui.account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.ui.account.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AccountViewWatchListAdapter extends RecyclerView.Adapter<AccountViewWatchListAdapter.ViewHolder> {

    private Context mContext = null;
    private List<ProductObject> mValues;
    private OnItemClickListener mItemClickListener;

    public AccountViewWatchListAdapter(Context context) {
        mContext = context;
        mValues = new ArrayList<>();
    }

    public void setWishListData(List<ProductObject> productObjects) {
        if (productObjects != null)
            mValues = productObjects;
        else
            mValues = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_accview_watchlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        ProductObject productObject = mValues.get(position);
        viewHolder.mItem = productObject;
        viewHolder.mIdView.setText(position + 1 + "");

        List<String> thumbImages = productObject.getProduct_images();
        String thumbImageURL = null;
        if (thumbImages.size() > 0) {
            thumbImageURL = thumbImages.get(0);
        }
        Glide.with(mContext).load(thumbImageURL).placeholder(R.drawable.ic_image_dt).into(viewHolder.mProductImage);

        //-------
        viewHolder.mProductTitle.setText(productObject.getTitle());

        int productSellingPrice = productObject.getSellingPrice();
        String formattedSP = mContext.getResources().getString(R.string.product_sp_value, productSellingPrice);
        viewHolder.mProductCost.setText(formattedSP);

        int productCostPrice = productObject.getCostPrice();
        String formattedCP = mContext.getResources().getString(R.string.product_cp_value, productCostPrice);
        viewHolder.mProductActualCost.setText(formattedCP);

        if (productObject.getSellingPrice() < productObject.getCostPrice() && productObject.getCostPrice() != 0) {
            int discount = 100 * (productObject.getCostPrice() - productObject.getSellingPrice()) / productObject.getCostPrice();
            if (discount != 0) {
                String formattedDiscount = mContext.getResources().getString(R.string.product_discount_value, discount);
                viewHolder.mProductDiscount.setText(formattedDiscount);
            } else {
                viewHolder.mProductDiscount.setVisibility(View.GONE);
            }
        } else {
            viewHolder.mProductDiscount.setVisibility(View.GONE);
            viewHolder.mProductActualCost.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int productId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView mProductImage;
        public final TextView mIdView;
        public final TextView mProductTitle;
        public final TextView mProductActualCost;
        public final TextView mProductCost;
        public final TextView mProductDiscount;
        public ProductObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mProductImage = view.findViewById(R.id.product_image);
            mProductTitle = view.findViewById(R.id.product_title);
            mProductActualCost = view.findViewById(R.id.product_actual_cost);
            mProductCost = view.findViewById(R.id.product_cost);
            mProductDiscount = view.findViewById(R.id.product_discount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem + "'";
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mItemClickListener != null && mValues != null && position <= mValues.size()) {
                mItemClickListener.onItemClick(itemView, mValues.get(position).getProductId());
            }
        }
    }
}