package com.example.food.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.food.DetailsActivity;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Restaurant> restaurantList;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_restaurant, parent, false);
        return new CardViewHolder(view);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        // get the restaurant
        final Restaurant restaurant = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        //Get the image uri stored in the DB
        String sUri = restaurant.getImageRestaurant();
        Uri uri = Uri.parse(sUri);

        // set the title and the snippet
        holder.textName.setText(restaurant.getRestaurantTitle());
        holder.textType.setText(restaurant.getRestaurantType());
        holder.textDescription.setText(restaurant.getDescription());
        holder.totalsStar.setNumStars(restaurant.getRatingStar());
        Picasso.get().load(uri).into(holder.imageBackground);

        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("id", restaurant.getId());
                intent.putExtra("image", restaurant.getImageRestaurant());
                intent.putExtra("rating", restaurant.getRatingStar());
                intent.putExtra("nameRestaurant", restaurant.getRestaurantTitle());
                intent.putExtra("descripRestaurant", restaurant.getDescription());

                ((Activity) context).startActivityForResult(intent, 2);

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

    public Restaurant getItem(int index) {
        if (getItemCount() > 0 && index < restaurantList.size())
            return restaurantList.get(index);
        else
            return null;
    }

    public int getItemCount() {
        return restaurantList != null ? restaurantList.size() : 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView textName;
        TextView textType;
        TextView textDescription;
        ImageView imageBackground;
        RatingBar totalsStar;

        public CardViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_resraurant);
            textName = itemView.findViewById(R.id.text_name);
            textType = itemView.findViewById(R.id.text_type);
            textDescription = itemView.findViewById(R.id.text_description);
            imageBackground = itemView.findViewById(R.id.image_background);
            totalsStar = itemView.findViewById(R.id.rate);
        }
    }
}
