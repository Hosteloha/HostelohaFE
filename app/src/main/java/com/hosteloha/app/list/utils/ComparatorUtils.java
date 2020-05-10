package com.hosteloha.app.list.utils;

import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.define.SortingType;

import java.util.Comparator;

public class ComparatorUtils {

    private static Comparator<ProductObject> mCostAscendingComparator = new Comparator<ProductObject>() {
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

    private static Comparator<ProductObject> mCostDescendingComparator = new Comparator<ProductObject>() {
        @Override
        public int compare(ProductObject compare1, ProductObject compare2) {
            if (compare1.getSellingPrice() == compare2.getSellingPrice())
                return 0;
            else if (compare1.getSellingPrice() < compare2.getSellingPrice())
                return 1;
            else
                return -1;
        }
    };

    private static Comparator<ProductObject> mProductTitleComparator = new Comparator<ProductObject>() {
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

    private static Comparator<ProductObject> mProductComparator = new Comparator<ProductObject>() {
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

    public static Comparator getComparator(SortingType sortingType) {
        Comparator tempComparator = mCostAscendingComparator;

        switch (sortingType) {
            case TITLE:
                tempComparator = mProductTitleComparator;
                break;
            case LATEST:
                //To Do
                break;
            case RELEVANT:
                //To Do
                break;
            case POPULARITY:
                //To Do
                break;
            case PRICE_ASCENDING:
                tempComparator = mCostAscendingComparator;
                break;
            case PRICE_DESCENDING:
                tempComparator = mCostDescendingComparator;
                break;
            default:
                tempComparator = mCostAscendingComparator;
                break;
        }
        return tempComparator;
    }
}
