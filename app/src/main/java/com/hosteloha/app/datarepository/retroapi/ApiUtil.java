package com.hosteloha.app.datarepository.retroapi;

public class ApiUtil {
    /**
     * Use the BASE_URL when goes to production
     */
    private static final String BASE_URL = "https://hosteloha.com/";
    private static final String BASE_URL_HEROKU_GROOT = "https://hosteloha-api.herokuapp.com/";


    public static RetrofitInterface getServiceClass(){
        return RetrofitAPI.getRetrofit(BASE_URL_HEROKU_GROOT).create(RetrofitInterface.class);
    }
}