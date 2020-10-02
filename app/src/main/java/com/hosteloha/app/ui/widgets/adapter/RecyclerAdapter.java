package com.hosteloha.app.ui.widgets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.beans.UserFollowers;
import com.hosteloha.app.datarepository.beans.UserFollowings;
import com.hosteloha.app.ui.widgets.adapter.viewholders.LoadingViewHolder;
import com.hosteloha.app.ui.widgets.adapter.viewholders.ProductViewHolder;
import com.hosteloha.app.ui.widgets.adapter.viewholders.UserFollowersViewHolder;
import com.hosteloha.app.ui.widgets.adapter.viewholders.UserFollowingsViewHolder;
import com.hosteloha.app.ui.widgets.adapter.viewholders.WishlistViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum ItemType {PRODUCT, LOADING, CATEGORY_ITEMS, WISHLIST, FOLLOWERS, FOLLOWING}

    private boolean isLoading = false;

    List<T> mItemData;
    private ItemType mItemType;
    private OnItemClickListener mItemClickListener;
    private Context mContext;


    public RecyclerAdapter(Context context, ItemType itemType, OnItemClickListener onItemClickListener) {
        mContext = context;
        mItemData = new ArrayList<>();
        mItemType = itemType;
        mItemClickListener = onItemClickListener;
    }

    public void setArrayList(List<T> arrayList) {
        if (mItemData != null)
            mItemData.clear();
        if (arrayList == null)
            mItemData = new ArrayList<>();
        else
            mItemData = new ArrayList<>(arrayList);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = null;
        ItemType itemType = ItemType.values()[viewType];
        switch (itemType) {
            case LOADING:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            case FOLLOWING:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_accview_followings_list_item, viewGroup, false);
                return new UserFollowingsViewHolder(view);
            case FOLLOWERS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_accview_followers_list_item, viewGroup, false);
                return new UserFollowersViewHolder(view);
            case WISHLIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_accview_watchlist_item, viewGroup, false);
                return new WishlistViewHolder(view);
            case PRODUCT:
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview, viewGroup, false);
                return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemType itemType = ItemType.values()[getItemViewType(position)];
        switch (itemType) {
            case PRODUCT:
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                ProductObject product = (ProductObject) mItemData.get(position);

                productViewHolder.mItem = product;
                productViewHolder.mItemClickListener = mItemClickListener;
                // Set only the first image, after wards we can put effects to change images with time.
                List<String> thumbImages = product.getProduct_images();
                String thumbImageURL = null;
                if (thumbImages.size() > 0) {
                    thumbImageURL = thumbImages.get(0);
                }
                Glide.with(mContext).load(thumbImageURL).placeholder(R.drawable.ic_image_dt).into(productViewHolder.mProductImage);

                //-------
                productViewHolder.mProductTitle.setText(product.getTitle());

                int productSellingPrice = product.getSellingPrice();
                String formattedSP = mContext.getResources().getString(R.string.product_sp_value, productSellingPrice);
                productViewHolder.mProductCost.setText(formattedSP);

                int productCostPrice = product.getCostPrice();
                String formattedCP = mContext.getResources().getString(R.string.product_cp_value, productCostPrice);
                productViewHolder.mActualCost.setText(formattedCP);

                if (product.getSellingPrice() < product.getCostPrice() && product.getCostPrice() != 0) {
                    int discount = 100 * (product.getCostPrice() - product.getSellingPrice()) / product.getCostPrice();
                    if (discount != 0) {
                        String formattedDiscount = mContext.getResources().getString(R.string.product_discount_value, discount);
                        productViewHolder.mDiscount.setText(formattedDiscount);
                    } else {
                        productViewHolder.mDiscount.setVisibility(View.GONE);
                    }
                } else {
                    productViewHolder.mDiscount.setVisibility(View.GONE);
                    productViewHolder.mActualCost.setVisibility(View.GONE);
                }
                break;
            case WISHLIST:
                ProductObject productObject = (ProductObject) mItemData.get(position);
                WishlistViewHolder wishlistViewHolder = (WishlistViewHolder) holder;

                wishlistViewHolder.mItem = productObject;
                WishlistViewHolder.mItemClickListener = mItemClickListener;
                wishlistViewHolder.mIdView.setText(position + 1 + "");

                List<String> thumbImagesList = productObject.getProduct_images();
                String imageURL = null;
                if (thumbImagesList.size() > 0) {
                    thumbImageURL = thumbImagesList.get(0);
                }
                Glide.with(mContext).load(imageURL).placeholder(R.drawable.ic_image_dt).into(wishlistViewHolder.mProductImage);

                //-------
                wishlistViewHolder.mProductTitle.setText(productObject.getTitle());

                int sellingPrice = productObject.getSellingPrice();
                String formattedSellingPrice = mContext.getResources().getString(R.string.product_sp_value, sellingPrice);
                wishlistViewHolder.mProductCost.setText(formattedSellingPrice);

                int costPrice = productObject.getCostPrice();
                String formattedCostPrice = mContext.getResources().getString(R.string.product_cp_value, costPrice);
                wishlistViewHolder.mProductActualCost.setText(formattedCostPrice);

                if (productObject.getSellingPrice() < productObject.getCostPrice() && productObject.getCostPrice() != 0) {
                    int discount = 100 * (productObject.getCostPrice() - productObject.getSellingPrice()) / productObject.getCostPrice();
                    if (discount != 0) {
                        String formattedDiscount = mContext.getResources().getString(R.string.product_discount_value, discount);
                        wishlistViewHolder.mProductDiscount.setText(formattedDiscount);
                    } else {
                        wishlistViewHolder.mProductDiscount.setVisibility(View.GONE);
                    }
                } else {
                    wishlistViewHolder.mProductDiscount.setVisibility(View.GONE);
                    wishlistViewHolder.mProductActualCost.setVisibility(View.GONE);
                }
                break;

            case FOLLOWING:
                UserFollowingsViewHolder userFollowingsHolder = (UserFollowingsViewHolder) holder;
                UserFollowings userFollowings = (UserFollowings) mItemData.get(position);
                userFollowingsHolder.mItem = userFollowings;
                userFollowingsHolder.mIdView.setText(userFollowings.getsellerID() + "");
                userFollowingsHolder.mContentView.setText(userFollowings.getFollower_id() + "");
                break;

            case FOLLOWERS:
                UserFollowers userFollowers = (UserFollowers) mItemData.get(position);
                UserFollowersViewHolder userFollowersViewHolder = (UserFollowersViewHolder) holder;

                final int sellerID = userFollowers.getsellerID();
                final int followerID = userFollowers.getFollower_id();
                userFollowersViewHolder.mItem = userFollowers;
                userFollowersViewHolder.onItemClickListener = mItemClickListener;
                userFollowersViewHolder.mIdView.setText(followerID + "");
                userFollowersViewHolder.mContentView.setText(sellerID + "");
                // Checking if the user is folling the follower
                boolean isUserFollowing = true;
                // Works if followign users exist
                if (mItemData != null) {
                    userFollowersViewHolder.mFollowButton.setActivated(isUserFollowing);
                    if (isUserFollowing) {
                        // Unfollow
                        userFollowersViewHolder.mFollowButton.setText(R.string.btn_following);
                    } else {
                        // Follow
                        userFollowersViewHolder.mFollowButton.setText(R.string.btn_follow);
                    }
                    userFollowersViewHolder.mFollowButton.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (isLoading == false)
            return mItemData.size();
        else
            return mItemData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading == true && position == mItemData.size())
            return ItemType.LOADING.ordinal();
        else
            return mItemType.ordinal();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void showLoading() {
        if (!isLoading()) {
            isLoading = true;
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if (isLoading()) {
            isLoading = false;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int productId);
    }
}
