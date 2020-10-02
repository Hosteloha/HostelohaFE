package com.hosteloha.app.ui.widgets.adapter.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.UserFollowers;
import com.hosteloha.app.ui.widgets.adapter.RecyclerAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class UserFollowersViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public final Button mFollowButton;

    public UserFollowers mItem;
    public RecyclerAdapter.OnItemClickListener onItemClickListener;

    public UserFollowersViewHolder(View view) {
        super(view);
        mView = view;
        mIdView = (TextView) view.findViewById(R.id.item_number);
        mContentView = (TextView) view.findViewById(R.id.content);
        mFollowButton = (Button) view.findViewById(R.id.startFollowButton);
        mFollowButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view != null) {

            }
            if (onItemClickListener != null && mItem != null)
                onItemClickListener.onItemClick(view, mItem.getFollower_id());
        }
    };

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}