package com.hosteloha.app.datarepository.beans;

public class ProductCategory {
    String[] categories;

    public ProductCategory(String[] categories) {
        this.categories = categories;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
}
