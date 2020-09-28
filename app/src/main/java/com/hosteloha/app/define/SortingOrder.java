package com.hosteloha.app.define;

public enum SortingOrder {
    ASCENDING("asc"),
    DESCENDING("desc");

    private String value;

    SortingOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
