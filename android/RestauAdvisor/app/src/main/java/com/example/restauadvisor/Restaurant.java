package com.example.restauadvisor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("weekEndHour")
    @Expose
    private String weekEndHour;

    @SerializedName("weekHour")
    @Expose
    private String weekHour;

    @SerializedName("menu")
    @Expose
    private List<Menu> menus;

    public Restaurant(String id, String name, String description, String location, String phone, String website, String rating, String weekEndHour, String weekHour) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.phone = phone;
        this.website = website;
        this.rating = rating;
        this.weekEndHour = weekEndHour;
        this.weekHour = weekHour;
        menus = new ArrayList<Menu>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void addMenu(Menu menu) {
        if (menus == null)
            menus = new ArrayList<Menu>();
        menus.add(menu);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWeekEndHour() {
        return weekEndHour;
    }

    public void setWeekEndHour(String weekEndHour) {
        this.weekEndHour = weekEndHour;
    }

    public String getWeekHour() {
        return weekHour;
    }

    public void setWeekHour(String weekHour) {
        this.weekHour = weekHour;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
