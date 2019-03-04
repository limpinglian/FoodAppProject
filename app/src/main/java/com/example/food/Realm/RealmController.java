package com.example.food.Realm;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.food.Model.Restaurant;
import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Restaurant.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Restaurant> getRestaurant() {

        return realm.where(Restaurant.class).findAll();
    }

    //query a single item with the given id
    public Restaurant getRestaurant(String name) {

        return realm.where(Restaurant.class).equalTo("Name", name).findFirst();
    }

    //check if Book.class is empty
    public boolean hasRestaurant() {

        return !realm.allObjects(Restaurant.class).isEmpty();
    }

    //query example
    public RealmResults<Restaurant> queryedRestaurant() {

        return realm.where(Restaurant.class)
                .contains("Name", "MeetMee")
                .or()
                .contains("Type", "Noodles")
                .findAll();

    }
}
