package com.example.food;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.food.Model.Restaurant;
import com.example.food.Realm.RestaurantRealmHelper;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";

    private String name;
    private String descrip;
    private String image;
    private String id;
    private int rate;
    private int rating;
    private Uri uri;
    private RestaurantRealmHelper restaurantRealmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getIncomingIntent();

        restaurantRealmHelper = new RestaurantRealmHelper();

        final ImageView imageRestaurant = findViewById(R.id.ivRestaurant_Details);
        final TextView RestaurantName = findViewById(R.id.Name_Details);
        final RatingBar rate_details = findViewById(R.id.Rate_Details);
        final TextView DescriptionRestaurant = findViewById(R.id.Description_Details);
        final ToggleButton toggleButton = findViewById(R.id.button_favorite);

        Picasso.get().load(uri).into(imageRestaurant);

        rate_details.setNumStars(rate);
        RestaurantName.setText(name);
        DescriptionRestaurant.setText(descrip);

//        if (restaurant.isFavourite()) {
//            toggleButton.setChecked(true);
//        } else {
//            toggleButton.setChecked(false);
//        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    restaurantRealmHelper.updateRestaurantFavouriteStatus(id,true);
                    Toast.makeText(DetailsActivity.this, "Item favourite", Toast.LENGTH_SHORT).show();
                } else {
                    restaurantRealmHelper.updateRestaurantFavouriteStatus(id,false);
                }

            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("image") && getIntent().hasExtra("rating") && getIntent().hasExtra("nameRestaurant") && getIntent().hasExtra("descripRestaurant")) {
            id = getIntent().getStringExtra("id");
            image = getIntent().getStringExtra("image");
            uri = Uri.parse(image);
            rate = getIntent().getIntExtra("rate", 0);
            name = getIntent().getStringExtra("nameRestaurant");
            descrip = getIntent().getStringExtra("descripRestaurant");
        }
    }
}

