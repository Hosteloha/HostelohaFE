package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagedCategoryListModel {

    @SerializedName("content")
    private List<ProductObject> productObjects;

    @SerializedName("empty")
    private boolean isEmpty = false;

    @SerializedName("first")
    private boolean isFirst = false;

    @SerializedName("last")
    private boolean isLast = false;

    @SerializedName("number")
    private int currentPageNumber = 0;

    @SerializedName("totalPages")
    private int totalPages = 0;

    @SerializedName("totalElements")
    private int totalProductsCount = 0;

    public List<ProductObject> getProductObjects() {
        return productObjects;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalProductsCount() {
        return totalProductsCount;
    }

    public boolean appendData(PagedCategoryListModel pagedCategoryListModel) {
        if (pagedCategoryListModel == null || pagedCategoryListModel.isEmpty() || pagedCategoryListModel.isFirst())
            return false;
        else {
            if (pagedCategoryListModel.getCurrentPageNumber() == currentPageNumber + 1) {
                productObjects.addAll(pagedCategoryListModel.getProductObjects());
                isFirst = false;
                isEmpty = false;
                currentPageNumber++;
                totalProductsCount = pagedCategoryListModel.getTotalProductsCount();
                totalPages = pagedCategoryListModel.getTotalPages();
            }
            return true;
        }
    }
}
