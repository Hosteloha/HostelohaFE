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

    public void addProductObject(ProductObject productObject) {
        if (productObject != null && !mProductsList.contains(productObject)) {
            mProductsList.add(productObject);
            mIsSorted = false;
        }
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
