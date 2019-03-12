package com.example.food.Realm;

import android.util.Log;

import com.example.food.Model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RestaurantRealmHelper {

    public RestaurantRealmHelper() {
    }

    public Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public List<Restaurant> getRestaurant() {
        Realm realm = null;
        List<Restaurant> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<Restaurant> results = realm
                    .where(Restaurant.class)
                    .findAll();
            list.addAll(realm.copyFromRealm(results));
            realm.commitTransaction();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list;
    }

    public void updateRestaurantFavouriteStatus(String id, boolean isFavourite) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Restaurant restaurant = realm
                    .where(Restaurant.class)
                    .equalTo("id", id)
                    .findFirst();
            if (restaurant != null) {
                restaurant.setFavourite(isFavourite);
                realm.copyToRealm(restaurant);
            }
            realm.commitTransaction();
        } catch (Exception e) {
            Log.e("RealmHelper", e.getMessage());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public List<Restaurant> getFavRestaurant() {
        Realm realm = null;
        List<Restaurant> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<Restaurant> results = realm
                    .where(Restaurant.class)
                    .equalTo("isFavourite", true)
                    .findAll();
            list.addAll(realm.copyFromRealm(results));
            realm.commitTransaction();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list;
    }


}
