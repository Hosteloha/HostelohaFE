package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

public class ApiObject {

    @SerializedName("ID")
    private String title;

    @SerializedName("NAME")
    private String description;

    public ApiObject(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
