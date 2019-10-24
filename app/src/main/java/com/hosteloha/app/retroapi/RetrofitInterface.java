package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.ApiObject;
import com.hosteloha.app.beans.QueryResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @GET("android/get_products.php")
    public Call<List<ApiObject>> getAllPost();

    @POST("android/upload_products.php")
    @FormUrlEncoded
    Call<QueryResponse> uploadProduct(@Field("product_name") String mProductName);
}
