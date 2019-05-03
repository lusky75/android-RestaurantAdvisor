package com.example.restauadvisor;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {

    @GET("restau/list")
    Call<JsonObject> getRestaurants(@Header("token") String token);

    @GET("restau/list/name/{name}")
    Call<JsonObject> getRestaurantsByName(@Path("name") String name, @Header("token") String token);

    @POST("restau/insert")
    Call<JsonObject> createRestaurant(@Header("token") String token,
                                      @Header("name") String name,
                                      @Header("description") String description,
                                      @Header("location") String location,
                                      @Header("phone") String phone,
                                      @Header("webiste") String website,
                                      @Header("rating") String rating,
                                      @Header("weekhour") String weekhour,
                                      @Header("weekendhour") String weekendhour);

    /*
    @PUT("restau/update")
    Call<JsonObject> updateRestaurant(@Header("token") String token,
                                      @Query("name") String name,
                                      @Query("description") String description,
                                      @Query("location") String location,
                                      @Query("phone") String phone,
                                      @Query("webiste") String website,
                                      @Query("rating") String rating,
                                      @Query("weekhour") String weekhour,
                                      @Query("weekendhour") String weekendhour);
    */

    @DELETE("restau/delete/{id}")
    Call<JsonObject> deleteRestaurant(@Path("id") String restau_id, @Header("token") String token);
}