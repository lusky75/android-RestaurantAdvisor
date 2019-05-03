package com.example.restauadvisor;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

import java.util.List;

public interface UserApi {

    @GET("users/profile")
    Call<JsonObject> getUserProfile(@Header("token") String token);

    @POST("users/register")
    Call<JsonObject> registerUser(@Header("username") String username, @Header("password") String password);

    @POST("users/login")
    Call<JsonObject> loginUser(@Header("username") String username, @Header("password") String password);
}