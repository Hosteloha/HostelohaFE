package com.hosteloha.app.datarepository.beans;

import com.google.gson.annotations.SerializedName;

public class AddFollowerRequest {
    @SerializedName("follower_id")
    private int follower_id;
    @SerializedName("sellerID")
    private int sellerID;

    public AddFollowerRequest(int follower_id, int sellerID) {
        this.follower_id = follower_id;
        this.sellerID = sellerID;
    }

    public int getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(int follower_id) {
        this.follower_id = follower_id;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }
}
