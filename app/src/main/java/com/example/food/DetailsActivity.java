package com.example.food;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.food.Adapter.RealmRestaurantAdapter;
import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.Model.Restaurant;
import com.example.food.Realm.RealmController;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class DetailsActivity extends AppCompatActivity {
    private Realm realm;
    private LayoutInflater inflater;
    private RecyclerView recycler;
    private RestaurantAdapter adapter;
    String name;
    String descrip;
    String image;
    String id;
    int rate;
    int rating;
    Uri uri;
    private static final String TAG="DetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        realm = RealmController.getInstance().getRealm();

        RealmResults<Restaurant> results = realm.where(Restaurant.class).findAll();
        getIncomingIntent();
        // get the article
        final Restaurant restaurant = new Restaurant();
        String sUri = restaurant.getImageRestaurant();
       // Uri uri = Uri.parse(sUri);

        final ImageView imageRestaurant = (ImageView) findViewById(R.id.ivRestaurant_Details);
        final TextView RestaurantName = (TextView) findViewById(R.id.Name_Details);
        final RatingBar rate_details = (RatingBar) findViewById(R.id.Rate_Details);
        final TextView DescriptionRestaurant = (TextView) findViewById(R.id.Description_Details);
        final ToggleButton toggleButton=(ToggleButton)findViewById(R.id.button_favorite);

        Picasso.get().load(uri).into(imageRestaurant);

        rate_details.setNumStars(rate);
        RestaurantName.setText(name);
        DescriptionRestaurant.setText(descrip);


        if(restaurant.isFavourite()){
            toggleButton.setChecked(true);

        }else {
            toggleButton.setChecked(false);
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        Restaurant restaurant = realm.where(Restaurant.class).equalTo("id", id).findFirst();

                        if (realm != null) {
                            restaurant.setFavourite(true);
                        }
                        realm.copyToRealm(restaurant);
                        realm.commitTransaction();
                    }catch (Exception e){}
                    finally {
                        if(realm!=null){
                            realm.close();
                        }
                    }

                    Toast.makeText(DetailsActivity.this,"Item favourite",Toast.LENGTH_SHORT).show();
                }else{

                    restaurant.setFavourite(false);
                }

            }
        });


    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("id")&&getIntent().hasExtra("image")&&getIntent().hasExtra("rating")&&getIntent().hasExtra("nameRestaurant")&&getIntent().hasExtra("descripRestaurant")){

             id=getIntent().getStringExtra("id");
            image=getIntent().getStringExtra("image");
            uri=Uri.parse(image);
            rate=getIntent().getIntExtra("rate",0);
             name=getIntent().getStringExtra("nameRestaurant");
             descrip=getIntent().getStringExtra("descripRestaurant");
        }
    }
    public void setRealmAdapter(RealmResults<Restaurant> restaurant) {

        RealmRestaurantAdapter realmAdapter = new RealmRestaurantAdapter(getApplicationContext(), restaurant, true);
        // Set the data and tell the RecyclerView to draw
        recycler.setAdapter(adapter);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }
}

