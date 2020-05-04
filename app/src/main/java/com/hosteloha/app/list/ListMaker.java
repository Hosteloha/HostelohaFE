package com.hosteloha.app.list;

import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.define.SortingType;
import com.hosteloha.app.list.data.AllProductsSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListMaker {

    List<ProductObject> mMainProductList = null;

    List<ProductObject> mPricingProductList = null;
    List<ProductObject> mTitleProductList = null;
    List<ProductObject> mLatestProductList = null;
    List<ProductObject> mRelevantProductList = null;
    List<ProductObject> mPopularProductList = null;

    AllProductsSubject mAllProductsSubject = null;
    private SortingType mSortingType = SortingType.LATEST;

    private boolean mIsSortingDone = false;
    private Comparator<ProductObject> mProductCostComparator = new Comparator<ProductObject>() {
        @Override
        public int compare(ProductObject compare1, ProductObject compare2) {
            if (compare1.getSellingPrice() == compare2.getSellingPrice())
                return 0;
            else if (compare1.getSellingPrice() > compare2.getSellingPrice())
                return 1;
            else
                return -1;
        }
    };
    private Comparator<ProductObject> mProductTitleComparator = new Comparator<ProductObject>() {
        @Override
        public int compare(ProductObject compare1, ProductObject compare2) {

            if (compare1.getTitle() != null)
                return -1;
            else if (compare2.getTitle() != null)
                return 1;
            else
                return compare1.getTitle().compareToIgnoreCase(compare2.getTitle());
        }
    };
    private Comparator<ProductObject> mProductComparator = new Comparator<ProductObject>() {
        @Override
        public int compare(ProductObject compare1, ProductObject compare2) {
            if (compare1.getSellingPrice() == compare2.getSellingPrice())
                return 0;
            else if (compare1.getSellingPrice() > compare2.getSellingPrice())
                return 1;
            else
                return -1;
        }
    };

    public ListMaker() {
        mAllProductsSubject = AllProductsSubject.getAllProductsSubject();
    }

    public void setMainProductList(List<ProductObject> productList) {
        if (!mMainProductList.equals(productList)) {
            mMainProductList = productList;
            mIsSortingDone = false;
            startSortingThread();
        }
    }

    public void notifyProductlistUpdated() {
        if (mAllProductsSubject != null) {
            List<ProductObject> tempProductList = new ArrayList<>();

            switch (mSortingType) {
                case TITLE:
                    tempProductList = mTitleProductList;
                    break;
                case LATEST:
                    tempProductList = mMainProductList;
                    break;
                case RELEVANCE:
                    tempProductList = mRelevantProductList;
                    break;
                case POPULARITY:
                    tempProductList = mPopularProductList;
                    break;
                case PRICE_ASCENDING:
                    tempProductList = mPricingProductList;
                    break;
                case PRICE_DESCENDING:
                    // To Do
                    break;
            }
            mAllProductsSubject.setProductsList(tempProductList);
        }
    }

    public void setSortingType(SortingType sortingType) {
        mSortingType = sortingType;
        if (mIsSortingDone) {
            notifyProductlistUpdated();
        }
//        startSortingThread();
    }

    private void startSortingThread() {

        startCostComparatorThread();
        startTitleComparatorThread();
        mIsSortingDone = true;
    }

    private void startCostComparatorThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mMainProductList != null && mMainProductList.size() > 0)
                    mPricingProductList = mMainProductList.subList(0, mMainProductList.size() - 1);

                if (mPricingProductList != null && mPricingProductList.size() > 0)
                    Collections.sort(mPricingProductList, mProductCostComparator);

                if (mSortingType == SortingType.PRICE_ASCENDING)
                    notifyProductlistUpdated();


            }
        }).start();
    }

    private void startTitleComparatorThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mMainProductList != null && mMainProductList.size() > 0)
                    mTitleProductList = mMainProductList.subList(0, mMainProductList.size() - 1);

                if (mPricingProductList != null && mPricingProductList.size() > 0)
                    Collections.sort(mTitleProductList, mProductCostComparator);

                if (mSortingType == SortingType.TITLE)
                    notifyProductlistUpdated();


            }
        }).start();
    }

}
