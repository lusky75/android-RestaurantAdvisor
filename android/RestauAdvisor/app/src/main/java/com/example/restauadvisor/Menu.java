package com.example.restauadvisor;

public class Menu {
    private String id;

    private String name;

    private String description;

    private String price;

    private String restaurant_id;

    public Menu(String id, String name, String description, String price, String restaurant_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurant_id = restaurant_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}
