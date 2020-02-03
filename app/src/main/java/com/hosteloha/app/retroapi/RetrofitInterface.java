package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.QueryResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @GET("/products")
    Call<List<ProductObject>> getAllPost();

    @POST("/add_product/")
    Call<ResponseBody> uploadProduct(@Body RequestBody params);
}
