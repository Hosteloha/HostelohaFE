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
import com.hosteloha.app.log.HostelohaLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListMaker {

    private final int HANDLER_MESSAGE_ALL = -1;

    ArrayList<ProductObject> mMainProductList = null;
    ProductListHelper mMainProductListHelper = new ProductListHelper(SortingType.DEFAULT);
    ProductListHelper mPricingListHelper = null;
    ProductListHelper mTitleListHelper = null;

    AllProductsSubject mAllProductsSubject = null;
    ProductListHelper mLatestListHelper = null;

    private boolean mIsSortingDone = false;
    ProductListHelper mRelevantListHelper = null;
    ProductListHelper mPopularListHelper = null;
    private final int HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED = HANDLER_MESSAGE_ALL + 1;
    private final int HANDLER_MESSAGE_UPDATE_SORTING_LIST = HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED + 1;

    private SortingType mSortingType = SortingType.DEFAULT;
    private ListMakerHandler mListMakerHandler = null;

    public ListMaker() {
        mAllProductsSubject = AllProductsSubject.getAllProductsSubject();
        mHandlerThread = new HandlerThread("ListMaker Handler Thread");
        mHandlerThread.start();
        mListMakerHandler = new ListMakerHandler(mHandlerThread.getLooper());

    }

    private final int HANDLER_MESSAGE_SORTING_LIST_UPDATED = HANDLER_MESSAGE_UPDATE_SORTING_LIST + 1;
    private final int HANDLER_MESSAGE_UPDATE_PRODUCTS_MAP = HANDLER_MESSAGE_SORTING_LIST_UPDATED + 1;
    Map<Integer, ProductObject> mProductMap = new HashMap<>();

    private void clearSordingLists() {
        mPricingListHelper = null;
        mTitleListHelper = null;
        mLatestListHelper = null;
        mRelevantListHelper = null;
        mPopularListHelper = null;
    }

    ProductListHelper mCurrentListHelper = new ProductListHelper(SortingType.DEFAULT);
    HandlerThread mHandlerThread = null;

    public void setSortingType(SortingType sortingType) {
        if (mSortingType != sortingType) {
            mSortingType = sortingType;
            if (mCurrentListHelper != null && mCurrentListHelper.getSortingType() == mSortingType && mCurrentListHelper.isSorted())
                notifyProductlistUpdated();
            else
                mListMakerHandler.sendEmptyMessage(HANDLER_MESSAGE_UPDATE_SORTING_LIST);
        }

    }

    public void setMainProductList(ArrayList<ProductObject> productList) {
        if (mMainProductList != null)
            HostelohaLog.debugOut("  is Product list updated :: " + mMainProductList.equals(productList));
        if (mMainProductList == null || mMainProductList.equals(productList))
            mMainProductList = productList;
        mListMakerHandler.sendEmptyMessage(HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED);
    }

    public void notifyProductlistUpdated() {
        if (mAllProductsSubject != null && mCurrentListHelper != null) {
            HostelohaLog.debugOut("  " + mCurrentListHelper.isSorted() + "  size : " + mCurrentListHelper.getProductsList().size());
            if (mCurrentListHelper.isSorted() || mCurrentListHelper.getSortingType() == SortingType.DEFAULT) {
                mAllProductsSubject.setProductsList(mCurrentListHelper.getProductsList());
            } else
                mCurrentListHelper.sort();

/*            switch (mSortingType) {
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
            }*/
        }
    }

    private void updateSortingList() {
        mCurrentListHelper = null;
        mCurrentListHelper = mMainProductListHelper.cloneProductListHelper();
        mCurrentListHelper.setSortingType(mSortingType);

/*        clearSordingLists();
        mPricingListHelper = mMainProductListHelper.cloneProductListHelper();
        mPricingListHelper.setSortingType(SortingType.PRICE_ASCENDING);
        mTitleListHelper = mMainProductListHelper.cloneProductListHelper();
        mTitleListHelper.setSortingType(SortingType.TITLE);
        mLatestListHelper = mMainProductListHelper.cloneProductListHelper();
        mLatestListHelper.setSortingType(SortingType.LATEST);
        mRelevantListHelper = mMainProductListHelper.cloneProductListHelper();
        mRelevantListHelper.setSortingType(SortingType.RELEVANT);
        mPopularListHelper = mMainProductListHelper.cloneProductListHelper();
        mPopularListHelper.setSortingType(SortingType.POPULARITY);*/
        mListMakerHandler.sendEmptyMessage(HANDLER_MESSAGE_SORTING_LIST_UPDATED);
    }

    public ProductObject getProductObject(int productId) {
        return mProductMap.get(productId);
    }

    private void updateProductsMap() {
        if (mMainProductList != null) {
            mProductMap.clear();
            for (ProductObject obj : mMainProductList) {
                mProductMap.put(obj.getProductId(), obj);
            }
        } else
            HostelohaLog.debugOut("  mMainProductList is null ");
    }

    private class ListMakerHandler extends Handler {
        public ListMakerHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            HostelohaLog.debugOut(" ======== >  Handler message msg " + msg.what);
            switch (msg.what) {
                case HANDLER_MESSAGE_CHECK_PRODUCTS_UPDATED:
                    if (mMainProductListHelper.setProductsList(mMainProductList)) {
                        sendEmptyMessage(HANDLER_MESSAGE_UPDATE_SORTING_LIST);
                        sendEmptyMessage(HANDLER_MESSAGE_UPDATE_PRODUCTS_MAP);
                    }
                    break;
                case HANDLER_MESSAGE_UPDATE_SORTING_LIST:
                    updateSortingList();
                    break;
                case HANDLER_MESSAGE_SORTING_LIST_UPDATED:
                    notifyProductlistUpdated();
                    break;
                case HANDLER_MESSAGE_UPDATE_PRODUCTS_MAP:
                    updateProductsMap();
                    break;
            }
        }
    }

}
