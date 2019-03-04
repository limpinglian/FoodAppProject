package com.example.food.Adapter;

import android.content.Context;

import com.example.food.Model.Restaurant;

import io.realm.RealmResults;

public class RealmRestaurantAdapter extends RealmModelAdapter<Restaurant> {
    public RealmRestaurantAdapter(Context context, RealmResults<Restaurant> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
