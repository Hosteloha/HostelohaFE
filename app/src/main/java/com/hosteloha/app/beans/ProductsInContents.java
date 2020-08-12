package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

public class ProductsInContents {

    @SerializedName("content")
    private ProductObject[] productObjects;

    @SerializedName("empty")
    private boolean isEmpty = false;

    @SerializedName("first")
    private boolean isFirst = false;

    @SerializedName("last")
    private boolean isLast = false;

    @SerializedName("totalPages")
    private int totalPages = 0;

    @SerializedName("totalElements")
    private int totalProductsCount = 0;

    public ProductObject[] getProductObjects() {
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

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalProductsCount() {
        return totalProductsCount;
    }
}
