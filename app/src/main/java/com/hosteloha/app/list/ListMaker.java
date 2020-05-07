package com.hosteloha.app.list;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.define.SortingType;
import com.hosteloha.app.list.data.AllProductsSubject;
import com.hosteloha.app.list.data.ProductListHelper;

import java.util.ArrayList;
import java.util.List;

public class ListMaker {

    private final int HANDLER_MESSAGE_ALL = -1;
    private final int HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED = HANDLER_MESSAGE_ALL + 1;
    private final int HANDLER_MESSAGE_UPDATE_SORTING_LIST = HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED + 1;
    private final int HANDLER_MESSAGE_SORTING_LIST_UPDATED = HANDLER_MESSAGE_UPDATE_SORTING_LIST + 1;
    HandlerThread mHandlerThread = null;
    ArrayList<ProductObject> mMainProductList = null;
    ProductListHelper mMainProductListHelper = new ProductListHelper(SortingType.DEFAULT);
    ProductListHelper mPricingListHelper = null;
    ProductListHelper mTitleListHelper = null;

    AllProductsSubject mAllProductsSubject = null;
    ProductListHelper mLatestListHelper = null;

    private boolean mIsSortingDone = false;
    ProductListHelper mRelevantListHelper = null;
    ProductListHelper mPopularListHelper = null;
    ProductListHelper mTempListHelper = new ProductListHelper(SortingType.DEFAULT);

    public void setSortingType(SortingType sortingType) {
        mSortingType = sortingType;
        if (mIsSortingDone) {
            notifyProductlistUpdated();
        }
//        startSortingThread();
    }

    private SortingType mSortingType = SortingType.DEFAULT;
    private ListMakerHandler mListMakerHandler = null;
    public ListMaker() {
        mAllProductsSubject = AllProductsSubject.getAllProductsSubject();
        mHandlerThread = new HandlerThread("ListMaker Handler Thread");
        mHandlerThread.start();
        mListMakerHandler = new ListMakerHandler(mHandlerThread.getLooper());

    }

    public void setMainProductList(ArrayList<ProductObject> productList) {
            mMainProductList = productList;
        mListMakerHandler.sendEmptyMessage(HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED);
    }

    public void notifyProductlistUpdated() {
        if (mAllProductsSubject != null) {
            List<ProductObject> tempProductList = new ArrayList<>();
            boolean isSorted = false;

            switch (mSortingType) {
                case TITLE:
                    if (mTitleListHelper.isSorted()) {
                        tempProductList = mTitleListHelper.getProductsList();
                        isSorted = true;
                    }
                    break;
                case LATEST:
                    if (mLatestListHelper.isSorted()) {
                        tempProductList = mLatestListHelper.getProductsList();
                        isSorted = true;
                    }
                    break;
                case RELEVANT:
                    if (mRelevantListHelper.isSorted()) {
                        tempProductList = mRelevantListHelper.getProductsList();
                        isSorted = true;
                    }
                    break;
                case POPULARITY:
                    if (mPopularListHelper.isSorted()) {
                        tempProductList = mPopularListHelper.getProductsList();
                        isSorted = true;
                    }
                    break;
                case PRICE_ASCENDING:
                    if (mPricingListHelper.isSorted()) {
                        tempProductList = mPricingListHelper.getProductsList();
                        isSorted = true;
                    }
                    break;
                case PRICE_DESCENDING:
                    // To Do
                    break;
            }
            if (isSorted)
                mAllProductsSubject.setProductsList(tempProductList);
            else
                mListMakerHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SORTING_LIST_UPDATED, 500);
        }
    }

    private void updateSortingList() {
        clearSordingLists();
        mPricingListHelper = mMainProductListHelper.cloneProductListHelper();
        mPricingListHelper.setSortingType(SortingType.PRICE_ASCENDING);
        mTitleListHelper = mMainProductListHelper.cloneProductListHelper();
        mTitleListHelper.setSortingType(SortingType.TITLE);
        mLatestListHelper = mMainProductListHelper.cloneProductListHelper();
        mLatestListHelper.setSortingType(SortingType.LATEST);
        mRelevantListHelper = mMainProductListHelper.cloneProductListHelper();
        mRelevantListHelper.setSortingType(SortingType.RELEVANT);
        mPopularListHelper = mMainProductListHelper.cloneProductListHelper();
        mPopularListHelper.setSortingType(SortingType.POPULARITY);
        mListMakerHandler.sendEmptyMessage(HANDLER_MESSAGE_UPDATE_SORTING_LIST);
    }

    private void clearSordingLists() {
        mPricingListHelper = null;
        mTitleListHelper = null;
        mLatestListHelper = null;
        mRelevantListHelper = null;
        mPopularListHelper = null;
    }

    private class ListMakerHandler extends Handler {
        public ListMakerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED:
                    if (mMainProductListHelper.setProductsList(mMainProductList))
                        sendEmptyMessage(HANDLER_MESSAGE_UPDATE_SORTING_LIST);
                    break;
                case HANDLER_MESSAGE_UPDATE_SORTING_LIST:
                    updateSortingList();
                    break;
                case HANDLER_MESSAGE_SORTING_LIST_UPDATED:
                    notifyProductlistUpdated();
                    break;
            }
        }
    }

}
