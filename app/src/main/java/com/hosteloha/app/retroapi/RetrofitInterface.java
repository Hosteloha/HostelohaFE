package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @POST("/authenticate")
    Call<AuthenticationTokenJWT> getAuthenticationToken(@Body UserAuthentication params);

    @GET("/products")
    Call<List<ProductObject>> getAllProcducts(@Header("Authorization") String auth);

    @POST("/add_product/")
    Call<ProductObject> uploadProduct(@Body ProductObject productObject, @Header("Authorization") String auth);

    @GET("/AllCategories")
    Call<String[]> getProductMainCategories(@Header("Authorization") String auth);

    @GET("/{categoryname}/subcategory1")
    Call<String[]> getProductSubCategoryFirstList(@Path("categoryname") String categoryName, @Header("Authorization") String auth);

    @GET("/{categoryname}/subcategory2")
    Call<String[]> getProductSubCategorySecondList(@Path("categoryname") String categoryName, @Header("Authorization") String auth);

    @GET("/product_condition")
    Call<ResponseBody> getProductConditions(@Header("Authorization") String auth);

    @GET("/payment_option")
    Call<ResponseBody> getProductPaymentOptions(@Header("Authorization") String auth);

    @GET("/delivery_format")
    Call<ResponseBody> getProductDeliveryFormats(@Header("Authorization") String auth);

}
