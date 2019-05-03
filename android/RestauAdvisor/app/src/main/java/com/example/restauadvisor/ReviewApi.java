package com.example.restauadvisor;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ReviewApi {

    @GET("reviews")
    Call<JsonObject> getReviews(@Header("token") String token);

    @GET("reviews/restau/{id}")
    Call<JsonObject> getReviewByRestaurant(@Header("token") String token, @Path("id") String id);

    @POST("reviews/add/{id}")
    Call<JsonObject> addReviewToRestaurant(@Header("token") String token, @Path("id") String id);
}