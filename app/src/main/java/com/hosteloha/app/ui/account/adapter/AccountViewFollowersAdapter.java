package com.hosteloha.app.ui.account.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.UserFollowers;
import com.hosteloha.app.datarepository.beans.UserFollowings;
import com.hosteloha.app.ui.account.AccountViewModel;
import com.hosteloha.app.ui.account.dummy.DummyContent.DummyItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AccountViewFollowersAdapter extends RecyclerView.Adapter<AccountViewFollowersAdapter.ViewHolder> {

    private final List<UserFollowers> mValues;
    private List<UserFollowings> mUserFollowings = null;
    private AccountViewModel mAccountViewModel;

    public AccountViewFollowersAdapter(List<UserFollowers> items, AccountViewModel accountViewModel) {
        mValues = items;
        mAccountViewModel = accountViewModel;
        mUserFollowings = accountViewModel.getUserFollowingLive().getValue();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_accview_followers_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int sellerID = mValues.get(position).getsellerID();
        final int followerID = mValues.get(position).getFollower_id();
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(followerID + "");
        holder.mContentView.setText(sellerID + "");
        // Checking if the user is folling the follower
        boolean isUserFollowing = false;
        // Works if followign users exist
        if (mUserFollowings != null) {
            for (UserFollowings in_each_userFollowing : mUserFollowings) {
                // if the following seller and current seller(user)
                if (in_each_userFollowing.getsellerID() == followerID) {
                    isUserFollowing = true;
                    break;
                }
            }

            holder.mFollowButton.setActivated(isUserFollowing);
            if (isUserFollowing) {
                // Unfollow
                holder.mFollowButton.setText(R.string.btn_following);
            } else {
                // Follow
                holder.mFollowButton.setText(R.string.btn_follow);
            }

            holder.mFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggling color change on button click
                    boolean isStateActivated = holder.mFollowButton.isActivated();
                    // state activated true means following
                    if (isStateActivated) {
                        // then Unfollow so set follow to activate
                        holder.mFollowButton.setActivated(false);
                        holder.mFollowButton.setText(R.string.btn_follow);
                        mAccountViewModel.removeUserFollower(followerID, sellerID);
                    } else {
                        // then follow
                        holder.mFollowButton.setActivated(true);
                        holder.mFollowButton.setText(R.string.btn_following);
                        mAccountViewModel.addUserFollowers(followerID);
                    }
                }
            });
            holder.mFollowButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button mFollowButton;
        public UserFollowers mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mFollowButton = (Button) view.findViewById(R.id.startFollowButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}