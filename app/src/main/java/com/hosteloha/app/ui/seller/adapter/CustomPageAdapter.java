package com.hosteloha.app.ui.seller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CustomPageAdapter extends PagerAdapter {

    private Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPageAdapter(LayoutInflater layoutInflater) {
//        mContext = context;
        mLayoutInflater = layoutInflater;
//        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    private List<View> pageViews = new ArrayList<>();

    public void insertView(View view) {
        pageViews.add(view);
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (pageViews.get(position) != null) {
            container.addView(pageViews.get(position));
        }
        return pageViews.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Log.d("vinay", "isViewFromObject  ===> bool  " + (view == object));
        return view == object;
    }

}
