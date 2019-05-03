package com.example.restauadvisor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {

    public Retrofit configureRetrofit() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8085/jersey/rest/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

}
