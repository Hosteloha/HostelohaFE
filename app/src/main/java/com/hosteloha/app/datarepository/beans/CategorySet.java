package com.hosteloha.app.datarepository.beans;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class CategorySet {

    @SerializedName("category_name")
    private String mainCategory = "Beauty";
    @SerializedName("sub_category1")
    private String subCategory1 = "FaceCreams";
    @SerializedName("sub_category2")
    private String subCategory2 = "Facewash";

    public CategorySet() {
        this("", "", "");
    }

    public CategorySet(String mainCategory, String subCategory1, String subCategory2) {
        this.mainCategory = mainCategory;
        this.subCategory1 = subCategory1;
        this.subCategory2 = subCategory2;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory1() {
        return subCategory1;
    }

    public void setSubCategory1(String subCategory1) {
        this.subCategory1 = subCategory1;
    }

    public String getSubCategory2() {
        return subCategory2;
    }

    public void setSubCategory2(String subCategory2) {
        this.subCategory2 = subCategory2;
    }

    @NonNull
    @Override
    public String toString() {
        return "[ CategorySet == mainCategory = " + mainCategory + ", subCategory1 = " + subCategory1 + ", subCategory2 = " + subCategory2 + " ]";
    }
}
