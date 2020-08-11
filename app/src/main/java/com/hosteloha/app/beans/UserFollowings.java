package com.hosteloha.app.beans;

import java.sql.Timestamp;

public class UserFollowings {
    private int id;
    private int sellerID;
    private int follower_id;
    private int is_notify;
    private Timestamp inserted_at;
    private Timestamp updated_at;

    public UserFollowings() {
        super();
    }

    public UserFollowings(int id, int sellerID, int follower_id, int is_notify, Timestamp inserted_at) {
        super();
        this.id = id;
        this.sellerID = sellerID;
        this.follower_id = follower_id;
        this.is_notify = is_notify;
        this.inserted_at = inserted_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getsellerID() {
        return sellerID;
    }

    public void setsellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(int follower_id) {
        this.follower_id = follower_id;
    }

    public int getIs_notify() {
        return is_notify;
    }

    public void setIs_notify(int is_notify) {
        this.is_notify = is_notify;
    }

    public Timestamp getInserted_at() {
        return inserted_at;
    }

    public void setInserted_at(Timestamp inserted_at) {
        this.inserted_at = inserted_at;
    }
}
