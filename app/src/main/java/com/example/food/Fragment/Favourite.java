package com.example.food.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food.Adapter.RealmRestaurantAdapter;
import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.App.Prefs;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.example.food.Realm.RealmController;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {

RecyclerView recycler;
    private RestaurantAdapter adapter;
    private Realm realm;
    public Favourite() {
        // Required empty public constructor
    }
    public static Favourite newInstance(){
        Favourite fav=new Favourite();
        return fav;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);


        this.realm = RealmController.with(this).getRealm();
        setupRecycler();
        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getRestaurant());

        return rootView;
    }
    public String toString(){
        return "Favourite";
    }

    public void setRealmAdapter(RealmResults<Restaurant> restaurant) {

        RealmRestaurantAdapter realmAdapter = new RealmRestaurantAdapter(getActivity().getApplicationContext(), restaurant, true);
        // Set the data and tell the RecyclerView to draw
        Realm realm = Realm.getDefaultInstance();
       RealmResults<Restaurant>favRestaurant=realm.where(Restaurant.class).equalTo("isFavourite",true).findAll();
        realm.commitTransaction();
        recycler.setAdapter(adapter);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new RestaurantAdapter(getActivity());
        recycler.setAdapter(adapter);
    }

}
