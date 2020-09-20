package com.hosteloha.app.datarepository.beans;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class ImageUrl {

    @SerializedName("imageURL")
    private String url;

    public ImageUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return url;
    }
}
