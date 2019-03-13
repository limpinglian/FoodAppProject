package com.example.food.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.App.Prefs;
import com.example.food.FabActivity;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.example.food.Realm.RestaurantRealmHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class All extends Fragment {
    //Declare the view objects after constants
    private RecyclerView recycler;
    private FloatingActionButton fab, fab_add, fab_random;
    private Animation fabOpen, fabClose, fabClockwise, fabAnticlockwise;

    //Variables
    private RestaurantAdapter adapter;
    private RestaurantRealmHelper restaurantRealmHelper;
    private List<Restaurant> restaurantList;
    boolean isOpen = false;

    public static All newInstance() {
        return new All();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize animation sequences
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        fabAnticlockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anticlockwise);

        restaurantRealmHelper = new RestaurantRealmHelper();

        //Check if there is mock data
        //If there is no mock data, add it
        if (!Prefs.with(getActivity()).getPreLoad()) {
            setRealmData();
        }

        restaurantList = restaurantRealmHelper.getRestaurant();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all, container, false);

        //Assign xml views to view class object
        recycler = rootView.findViewById(R.id.recycler);
        fab = rootView.findViewById(R.id.fab);
        fab_add = rootView.findViewById(R.id.fb_add);
        fab_random = rootView.findViewById(R.id.fb_random);

        return rootView;
    }

    //Actions that access views should be called after the views is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fab_add.startAnimation(fabClose);
                    fab_random.startAnimation(fabClose);
                    fab.startAnimation(fabAnticlockwise);
                    fab_random.setClickable(false);
                    fab_add.setClickable(false);
                    isOpen = false;
                } else {
                    fab_add.startAnimation(fabOpen);
                    fab_random.startAnimation(fabOpen);
                    fab.startAnimation(fabClockwise);
                    fab_random.setClickable(true);
                    fab_add.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FabActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        setupRecycler();

        // refresh the realm instance

        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically

        Toast.makeText(getActivity(), "Press card item for edit, long press to remove item", Toast.LENGTH_LONG).show();
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

    public String toString() {
        return "All";
    }

    //this thing shouldnt be placed here
    //but for learning purpose will just leave it here
    //on first launch, set mock data
    private void setRealmData() {
        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
        Restaurant restaurant = new Restaurant();

        restaurant.setId("sjsanjd");
        restaurant.setRestaurantTitle("MeetMee");
        restaurant.setRestaurantType("Nooddles ");
        restaurant.setDescription("Good Taste");
        restaurant.setImageRestaurant("https://api.androidhive.info/images/realm/4.png");
        restaurantArrayList.add(restaurant);

        for (Restaurant r : restaurantArrayList) {
            // Persist your data easily
            restaurantRealmHelper.getRealm().beginTransaction();
            restaurantRealmHelper.getRealm().copyToRealm(r);
            restaurantRealmHelper.getRealm().commitTransaction();
        }
        restaurantRealmHelper.getRealm().close();

        Prefs.with(getActivity()).setPreLoad(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100
                && resultCode == Activity.RESULT_OK) {
            restaurantList = restaurantRealmHelper.getRestaurant();
            if (adapter != null) {
                adapter.updateData(restaurantList);
            }
        }
    }
}


