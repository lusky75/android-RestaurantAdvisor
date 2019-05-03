package com.example.restauadvisor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("restaurant")
    @Expose
    private String restaurant;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("rating")
    @Expose
    private String rating;

    public Review(String username, String restaurant, String message, String rating) {
        this.username = username;
        this.restaurant = restaurant;
        this.message = message;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
