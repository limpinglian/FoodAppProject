package com.example.food.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food.App.Prefs;
import com.example.food.DetailsActivity;
import com.example.food.FabActivity;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.example.food.Realm.RealmController;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;


public class RestaurantAdapter extends RealmRecycleViewAdapter<Restaurant>  {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
Restaurant restaurant;







    public RestaurantAdapter(Context context) {

        this.context = context;
    }
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_restaurant, parent, false);
        return new CardViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();



        // get the article
        final Restaurant restaurant = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        String sUri=restaurant.getImageRestaurant();


        Uri uri=Uri.parse(sUri);
        // set the title and the snippet
        holder.textName.setText(restaurant.getRestaurantTitle());
        holder.textType.setText(restaurant.getRestaurantType());
        holder.textDescription.setText(restaurant.getDescription());
        Picasso.get().load(uri).into(holder.imageBackground);
        holder.totalsStar.setNumStars(restaurant.getRatingStar());






        //remove single match from realm
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                RealmResults<Restaurant> results = realm.where(Restaurant.class).findAll();

                // Get the book title to show it in toast message
                Restaurant r = results.get(position);
                String title = r.getRestaurantTitle();

                // All changes to data must happen in a transaction
                realm.beginTransaction();

                // remove single match
                results.remove(position);
                realm.commitTransaction();

                if (results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                notifyDataSetChanged();

                Toast.makeText(context, title + " is removed from Realm", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    holder.card.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            Intent intent=new Intent(context, DetailsActivity.class);
            intent.putExtra("id",restaurant.getId());
            intent.putExtra("image",restaurant.getImageRestaurant());
            intent.putExtra("rating",restaurant.getRatingStar());
            intent.putExtra("nameRestaurant",restaurant.getRestaurantTitle());
            intent.putExtra("descripRestaurant",restaurant.getDescription());

            ((Activity)context).startActivityForResult(intent,2);


                /*inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editName = (EditText) content.findViewById(R.id.name);
                final Spinner editType = (Spinner) content.findViewById(R.id.spinner);
                final EditText editDescription = (EditText) content.findViewById(R.id.description);

                editName.setText(restaurant.getRestaurantTitle());

                editDescription.setText(restaurant.getDescription());



                RealmResults<Restaurant> results = realm.where(Restaurant.class).findAll();

                realm.beginTransaction();
                results.get(position).setRestaurantTitle(editName.getText().toString());
                results.get(position).setRestaurantType(editType.getSelectedItem().toString());
                results.get(position).setDescription(editDescription.getText().toString());

                realm.commitTransaction();

                notifyDataSetChanged();*/
            }






    });


}

    public int getItemCount() {


        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }




    public static class CardViewHolder extends RecyclerView.ViewHolder {

            public CardView card;
            public TextView textName;
            public TextView textType;
            public TextView textDescription;
            public ImageView imageBackground;
            public RatingBar totalsStar;

            public CardViewHolder(View itemView) {
                // standard view holder pattern with Butterknife view injection
                super(itemView);

                card = (CardView) itemView.findViewById(R.id.card_resraurant);
                textName = (TextView) itemView.findViewById(R.id.text_name);
                textType = (TextView) itemView.findViewById(R.id.text_type);
                textDescription = (TextView) itemView.findViewById(R.id.text_description);
               imageBackground = (ImageView) itemView.findViewById(R.id.image_background);
               totalsStar=(RatingBar)itemView.findViewById(R.id.rate);
            }

        }
}
