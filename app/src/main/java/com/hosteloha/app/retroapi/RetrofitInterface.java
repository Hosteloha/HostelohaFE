package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.ApiObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("v2/5daf5a853200003f46d96207")
    public Call<List<ApiObject>> getAllPost();
}
