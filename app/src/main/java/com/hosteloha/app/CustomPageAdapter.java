package com.hosteloha.app;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CustomPageAdapter extends PagerAdapter {

    private  Context mContext;
    LayoutInflater mLayoutInflater;

    CustomPageAdapter(Context context){
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }
    private List<Integer> pageIds = new ArrayList<>();

    public void insertViewId( int pageId) {
        pageIds.add(pageId);
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("vinay","instantiateItem  ===> position  "+position+"  id "+pageIds.get(position));

        View view = mLayoutInflater.inflate(pageIds.get(position),null);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pageIds.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Log.d("vinay","isViewFromObject  ===> bool  "+(view == object));
        return view == object;
    }

}
