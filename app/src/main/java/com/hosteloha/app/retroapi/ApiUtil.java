package com.hosteloha.app.retroapi;

public class ApiUtil {
    private static final String BASE_URL = "https://hosteloha.com/";

    public static RetrofitInterface getServiceClass(){
        return RetrofitAPI.getRetrofit(BASE_URL).create(RetrofitInterface.class);
    }
}