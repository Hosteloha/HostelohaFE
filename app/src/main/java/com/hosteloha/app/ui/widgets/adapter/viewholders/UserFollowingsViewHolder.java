package com.hosteloha.app.ui.widgets.adapter.viewholders;

import android.view.View;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.UserFollowings;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserFollowingsViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public UserFollowings mItem;

    public UserFollowingsViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mIdView = (TextView) itemView.findViewById(R.id.item_number);
        mContentView = (TextView) itemView.findViewById(R.id.content);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}
