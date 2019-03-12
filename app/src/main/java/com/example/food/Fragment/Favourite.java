package com.example.food.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.example.food.Realm.RestaurantRealmHelper;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {

    private RecyclerView recycler;

    private RestaurantAdapter adapter;
    private RestaurantRealmHelper restaurantRealmHelper;
    private List<Restaurant> restaurantList;

    public static Favourite newInstance() {
        return new Favourite();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantRealmHelper = new RestaurantRealmHelper();
        restaurantList = restaurantRealmHelper.getFavRestaurant();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite, container, false);

        recycler = rootView.findViewById(R.id.recycler);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecycler();
    }

    public String toString() {
        return "Favourite";
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
        adapter = new RestaurantAdapter(getContext(), restaurantList);
        recycler.setAdapter(adapter);
    }
}
