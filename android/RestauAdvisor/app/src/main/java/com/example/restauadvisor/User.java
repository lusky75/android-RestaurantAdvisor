package com.example.restauadvisor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

}
