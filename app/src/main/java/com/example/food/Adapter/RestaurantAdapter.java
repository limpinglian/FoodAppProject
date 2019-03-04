package com.example.food.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food.App.Prefs;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.example.food.Realm.RealmController;

import io.realm.Realm;
import io.realm.RealmResults;

public class RestaurantAdapter extends RealmRecycleViewAdapter<Restaurant>  {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

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

        // set the title and the snippet
        holder.textName.setText(restaurant.getRestaurantTitle());
        holder.textType.setText(restaurant.getRestaurantType());
        holder.textDescription.setText(restaurant.getDescription());

        // load the background image
        if (restaurant.getImageRestaurant() != null) {
            Glide.with(context)
                    .load(restaurant.getImageRestaurant().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.imageBackground);
        }

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

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.edit_item, null);
            final EditText editName = (EditText) content.findViewById(R.id.name);
            final EditText editType = (EditText) content.findViewById(R.id.type);
            final EditText editDescription = (EditText) content.findViewById(R.id.description);

            editName.setText(restaurant.getRestaurantTitle());
            editType.setText(restaurant.getRestaurantType());
            editDescription.setText(restaurant.getDescription());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(content)
                    .setTitle("Edit Restaurant")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RealmResults<Restaurant> results = realm.where(Restaurant.class).findAll();

                            realm.beginTransaction();
                            results.get(position).setRestaurantTitle(editName.getText().toString());
                            results.get(position).setRestaurantType(editType.getText().toString());
                            results.get(position).setDescription(editDescription.getText().toString());

                            realm.commitTransaction();

                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
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

            public CardViewHolder(View itemView) {
                // standard view holder pattern with Butterknife view injection
                super(itemView);

                card = (CardView) itemView.findViewById(R.id.card_resraurant);
                textName = (TextView) itemView.findViewById(R.id.text_name);
                textType = (TextView) itemView.findViewById(R.id.text_type);
                textDescription = (TextView) itemView.findViewById(R.id.text_description);
               imageBackground = (ImageView) itemView.findViewById(R.id.image_background);
            }

        }
}
