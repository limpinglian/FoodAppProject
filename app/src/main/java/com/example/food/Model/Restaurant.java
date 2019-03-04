package com.example.food.Model;

import io.realm.RealmObject;

public class Restaurant extends RealmObject {

    private String ImageRestaurant;
    private String restaurantTitle;
    private String restaurantType;

    public String getImageRestaurant() {
        return ImageRestaurant;
    }

    public void setImageRestaurant(String imageRestaurant) {
        ImageRestaurant = imageRestaurant;
    }

    public String getRestaurantTitle() {
        return restaurantTitle;
    }

    public void setRestaurantTitle(String restaurantTitle) {
        this.restaurantTitle = restaurantTitle;
    }

    public String getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(String restaurantType) {
        this.restaurantType = restaurantType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    private String description;
    private String rating;
    private String area;
    private String creationDate;
}
