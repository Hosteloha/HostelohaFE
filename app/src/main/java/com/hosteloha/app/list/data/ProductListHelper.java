package com.hosteloha.app.list.data;

import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.define.SortingType;
import com.hosteloha.app.list.utils.ComparatorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProductListHelper {

    private List<ProductObject> mProductsList = new ArrayList<>();
    private boolean mIsSorted = false;
    private SortingType mSortingType = SortingType.LATEST;
    private boolean mIsTempList = false;

    public ProductListHelper(SortingType mSortingType) {
        this.mSortingType = mSortingType;
    }

    public ProductListHelper cloneProductListHelper() {
        ProductListHelper tempList = new ProductListHelper(mSortingType);
        tempList.mIsSorted = mIsSorted;
        Iterator iterator = mProductsList.listIterator();
        while (iterator.hasNext()) {
            ProductObject productObject = ((ProductObject) iterator.next()).cloneProductObject();
            tempList.addProductObject(productObject);
        }
        return tempList;
    }

    public boolean isSorted() {
        return mIsSorted;
    }

    public boolean isListChanged(ArrayList<ProductObject> productObjects) {
        boolean isUpdated = false;
        if (productObjects != null)
            isUpdated = !productObjects.equals(mProductsList);
        return isUpdated;
    }

    public boolean setProductsList(ArrayList<ProductObject> productsList) {
        boolean isUpdated = false;
        if (isListChanged(productsList)) {
            isUpdated = true;
            Iterator iterator = productsList.listIterator();
            while (iterator.hasNext()) {
                ProductObject productObject = ((ProductObject) iterator.next()).cloneProductObject();
                mProductsList.add(productObject);
            }
            mIsSorted = false;
            sort();
        }
        return isUpdated;
    }

    public List<ProductObject> getProductsList() {
        return mProductsList;
    }

    public void addProductObject(ProductObject productObject) {
        if (productObject != null && !mProductsList.contains(productObject)) {
            mProductsList.add(productObject);
            mIsSorted = false;
        }
    }

    public SortingType getSortingType() {
        return mSortingType;
    }

    public void setSortingType(SortingType sortingType) {
        if (this.mSortingType != sortingType) {
            mSortingType = sortingType;
            mIsSorted = false;
            sort();
        }
    }

    public boolean isTempList() {
        return mIsTempList;
    }

    public void setIsTempList(boolean mIsTempList) {
        this.mIsTempList = mIsTempList;
    }

    public void sort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Collections.sort(mProductsList, ComparatorUtils.getComparator(mSortingType));
                mIsSorted = true;
            }
        }).start();
    }

}
