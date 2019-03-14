package com.example.food;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.food.Model.Restaurant;
import com.example.food.Realm.RestaurantRealmHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

public class EditActivity extends AppCompatActivity {
    String id;
    String image;
    Uri uri;
    int rate;
    String name;
    String descrip;
    private RestaurantRealmHelper restaurantRealmHelper;
    private List<Restaurant> restaurantList;
    private Realm realm;
    final ImageView imageRestaurant = findViewById(R.id.ivRestaurant);
    final EditText RestaurantName = findViewById(R.id.name);
    final RatingBar rate_details = findViewById(R.id.et_rate);
    final EditText DescriptionRestaurant = findViewById(R.id.description);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        Restaurant restaurant=new Restaurant();
        final ImageView imageRestaurant = findViewById(R.id.ivRestaurant);
        final EditText RestaurantName = findViewById(R.id.name);
        final RatingBar rate_details = findViewById(R.id.et_rate);
        final EditText DescriptionRestaurant = findViewById(R.id.description);
        id = getIntent().getStringExtra("id");

        if(getIntent().hasExtra("Id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("descrip")){
            String id= getIntent().getStringExtra("Id");
            String Name=getIntent().getStringExtra("name");
            String des=getIntent().getStringExtra("descrip");

            RestaurantName.setText(Name);
            DescriptionRestaurant.setText(des);

        }


      /*  for(int i=0; i<restaurantList.size();i++){
            if(id.equals(restaurant.getId())){
                RestaurantName.setText(restaurant.getRestaurantTitle());
            }
        }
*/

    }

    public void updatePersonDetails(Restaurant model,int position,int id) {
        Restaurant editRestaurantDetails = realm.where(Restaurant.class).equalTo("id", id).findFirst();
        realm.beginTransaction();

    }


}
