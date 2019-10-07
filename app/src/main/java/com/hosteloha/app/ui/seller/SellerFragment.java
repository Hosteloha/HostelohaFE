package com.hosteloha.app.ui.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hosteloha.R;
import com.hosteloha.app.ui.seller.adapter.CustomPageAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class SellerFragment extends Fragment {

    private SellerViewModel sellerViewModel;

    Button mNextBtn;
    Button mPrevBtn;
    ViewPager mViewPager;
    private CustomPageAdapter pageadapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sellerViewModel =
                ViewModelProviders.of(this).get(SellerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_seller, container, false);
        final TextView textView = root.findViewById(R.id.text_seller);
        sellerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mNextBtn = (Button) root.findViewById(R.id.seller_next_btn);
        mPrevBtn = (Button) root.findViewById(R.id.seller_prev_btn);
        mViewPager = root.findViewById(R.id.seller_viewpager);
        pageadapter = new CustomPageAdapter(inflater) ;

        pageadapter.insertViewId(R.layout.seller_page1);
        pageadapter.insertViewId(R.layout.seller_page2);
        pageadapter.insertViewId(R.layout.seller_page3);
        mViewPager.setAdapter(pageadapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);

        mPrevBtn.setOnClickListener(mOnClickListener);
        mNextBtn.setOnClickListener(mOnClickListener);


        return root;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.seller_next_btn :
                    if(mViewPager != null)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                    break;
                case R.id.seller_prev_btn :
                    if(mViewPager != null)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                    break;
            }
        }
    };

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {


            if(mPrevBtn != null)
                mPrevBtn.setEnabled((position == 0)? false:true );

            if(mNextBtn != null)
                mNextBtn.setText((position == 2)? "Submit":"Next" );
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}