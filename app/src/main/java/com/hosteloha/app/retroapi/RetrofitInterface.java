package com.hosteloha.app.retroapi;

import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.SellerProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.beans.WishListRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @POST("/authenticate")
    Call<AuthenticationTokenJWT> getAuthenticationToken(@Body UserAuthentication params);

    @GET("/products")
    Call<List<ProductObject>> getAllProducts(@Header("Authorization") String auth);

    @POST("/add_product/")
    Call<ProductObject> uploadProduct(@Body SellerProductObject productObject, @Header("Authorization") String auth);

    @GET("/AllCategories")
    Call<String[]> getProductMainCategories(@Header("Authorization") String auth);

    @GET("/categorymaplist")
    Call<Map<String, Set<String>>> getCategoryMapList(@Header("Authorization") String auth);

    @GET("/{categoryname}/subcategory1")
    Call<String[]> getProductSubCategoryFirstList(@Path("categoryname") String categoryName, @Header("Authorization") String auth);

    @GET("/{categoryname}/subcategory2")
    Call<String[]> getProductSubCategorySecondList(@Path("categoryname") String categoryName, @Header("Authorization") String auth);

    @GET("/product_condition")
    Call<ResponseBody> getProductConditions(@Header("Authorization") String auth);

    @GET("/{userid}/wishlist")
    Call<ResponseBody> getWishList(@Header("Authorization") String auth, @Path("userid") int userId);

    @POST("/wishlist")
    Call<ResponseBody> addToWishList(@Header("Authorization") String auth, @Body WishListRequest wishListRequest);

    @DELETE("/deleteWishlist/{userid}/{productid}")
    Call<ResponseBody> removeFromWishlist(@Header("Authorization") String auth, @Path("userid") int userId, @Path("productid") int productId);

    @GET("/payment_option")
    Call<ResponseBody> getProductPaymentOptions(@Header("Authorization") String auth);

    @GET("/delivery_format")
    Call<ResponseBody> getProductDeliveryFormats(@Header("Authorization") String auth);

}
