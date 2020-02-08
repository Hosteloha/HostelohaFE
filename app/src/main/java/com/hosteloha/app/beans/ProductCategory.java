package com.hosteloha.app.beans;

import java.util.List;

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
