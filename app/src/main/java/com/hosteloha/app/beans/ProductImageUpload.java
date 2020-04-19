package com.hosteloha.app.beans;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ProductImageUpload {
    private String id;
    private List<String> product_images = new ArrayList<>();
    public ProductImageUpload() {
    }

    public ProductImageUpload(String id, List<String> product_images) {
        this.id = id;
        this.product_images = product_images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(List<String> product_images) {
        this.product_images = product_images;
    }
}
