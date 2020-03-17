package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductCategory;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.QueryResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @POST("/authenticate")
    public Call<AuthenticationTokenJWT> getAuthenticationToken(@Body RequestBody params);

    @GET("/products")
    Call<List<ProductObject>> getAllPost(@Header("Authorization") String auth);

    @POST("/add_product/")
    Call<ProductObject> uploadProduct(@Body RequestBody params, @Header("Authorization") String auth);

    @GET("/AllCategories")
    public Call<String[]> getProductMainCategories(@Header("Authorization") String auth);

    @GET("/{categoryname}/subcategory1")
    public Call<String[]> getProductSubCategoryFirstList(@Path("categoryname") String categoryName, @Header("Authorization") String auth);

    @GET("/{categoryname}/subcategory2")
    public Call<String[]> getProductSubCategorySecondList(@Path("categoryname") String categoryName, @Header("Authorization") String auth);

    @GET("/product_condition")
    public Call<ResponseBody> getProductConditions(@Header("Authorization") String auth);

    @GET("/payment_option")
    public Call<ResponseBody> getProductPaymentOptions(@Header("Authorization") String auth);

    @GET("/delivery_format")
    public Call<ResponseBody> getProductDeliveryFormats(@Header("Authorization") String auth);

}
