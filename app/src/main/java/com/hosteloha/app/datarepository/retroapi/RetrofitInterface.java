package com.hosteloha.app.datarepository.retroapi;

import com.hosteloha.app.datarepository.beans.AddFollowerRequest;
import com.hosteloha.app.datarepository.beans.AuthenticationTokenJWT;
import com.hosteloha.app.datarepository.beans.PagedCategoryListModel;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.beans.SellerProductObject;
import com.hosteloha.app.datarepository.beans.UserAuthentication;
import com.hosteloha.app.datarepository.beans.UserDetails;
import com.hosteloha.app.datarepository.beans.UserFollowers;
import com.hosteloha.app.datarepository.beans.UserFollowings;
import com.hosteloha.app.datarepository.beans.WishListRequest;

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

    @GET("/products/{productid}")
    Call<ProductObject> getProductById(@Header("Authorization") String auth, @Path("productid") int productId);

    @GET("/getAllProductsByPages/{pagenumber}/{pagesize}/{sortingtype}/{sortby}")
    Call<PagedCategoryListModel> getAllProductsByPages(@Header("Authorization") String auth, @Path("pagenumber") int pageNumber, @Path("pagesize") int pageSize,
                                                       @Path("sortby") String sortBy, @Path("sortingtype") String sortingType);

    @GET("/getAllProductsByCategoryPages/{categoryname}/{pagenumber}/{pagesize}/{sortingtype}/{sortby}")
    Call<PagedCategoryListModel> getAllProductsByCategoryPages(@Header("Authorization") String auth, @Path("categoryname") String categoryName, @Path("pagenumber") int pageNumber,
                                                               @Path("pagesize") int pageSize, @Path("sortby") String sortBy, @Path("sortingtype") String sortingType);


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

    @POST("/addFollower")
    Call<ResponseBody> addFollower(@Header("Authorization") String auth, @Body AddFollowerRequest addFollowerRequest);

    @DELETE("/removeFollwer/{sellerid}/{followerid}")
    Call<ResponseBody> removeFollower(@Header("Authorization") String auth, @Path("sellerid") int userId, @Path("followerid") int followerId);

    @GET("/sellerFollowers/{userid}")
    Call<List<UserFollowers>> getUserFollowers(@Header("Authorization") String auth, @Path("userid") int userId);

    @GET("/followedsellers/{userid}")
    Call<List<UserFollowings>> getUserFollowings(@Header("Authorization") String auth, @Path("userid") int userId);

    @GET("/users/{userid}")
    Call<UserDetails> getUserDetails(@Header("Authorization") String auth, @Path("userid") int userId);


}
