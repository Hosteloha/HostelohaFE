package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

public class ProductsInContents {

    @SerializedName("content")
    private ProductObject[] productObjects;

    @SerializedName("empty_")
    private boolean isEmpty = false;

    @SerializedName("first_")
    private boolean isFirst = false;

    @SerializedName("last_")
    private boolean isLast = false;

    @SerializedName("totalPages_")
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
