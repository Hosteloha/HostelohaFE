package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.ProductCategory;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.QueryResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("/products")
    Call<List<ProductObject>> getAllPost();

    @POST("/add_product/")
    Call<ProductObject> uploadProduct(@Body RequestBody params);

    @GET("/AllCategories")
    public Call<String[]> getProductMainCategories();

    @GET("/{categoryname}/subcategory1")
    public Call<String[]> getProductSubCategoryFirstList(@Path("categoryname") String categoryName);

    @GET("/{categoryname}/subcategory2")
    public Call<String[]> getProductSubCategorySecondList(@Path("categoryname") String categoryName);

    @GET("/product_condition")
    public Call<ResponseBody> getProductConditions();

    @GET("/payment_option")
    public Call<ResponseBody> getProductPaymentOptions();

    @GET("/delivery_format")
    public Call<ResponseBody> getProductDeliveryFormats();


}
