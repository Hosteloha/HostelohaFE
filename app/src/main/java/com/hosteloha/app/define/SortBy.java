package com.hosteloha.app.define;

public enum SortBy {
    DEFAULT("views_count"),
    PRICE("selling_price"),
    QUANTITY("quantity"),
    //    LATEST(""),
//    DISCOUNT(""),
    POPULARITY("views_count");

    private String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
