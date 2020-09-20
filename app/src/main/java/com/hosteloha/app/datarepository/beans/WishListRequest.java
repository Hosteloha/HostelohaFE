package com.hosteloha.app.datarepository.beans;

import com.google.gson.annotations.SerializedName;

public class WishListRequest {
    @SerializedName("product_id")
    private int product_id;
    @SerializedName("users_id")
    private int users_id;

    public WishListRequest(int users_id, int product_id) {
        this.users_id = users_id;
        this.product_id = product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }
}
