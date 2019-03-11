package com.example.food.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food.Adapter.RealmRestaurantAdapter;
import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.App.Prefs;
import com.example.food.FabActivity;
import com.example.food.Model.Restaurant;
import com.example.food.R;
import com.example.food.Realm.RealmController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class All extends Fragment {
    private RecyclerView recycler;
    private Restaurant restaurant;
    private RestaurantAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab,fab_add,fab_random;
    Animation fabOpen,fabClose,fabClockwise,fabAnticlockwise;
boolean isOpen=false;
    private static final String IMAGE_DIRECTORY = "/images";
    private int GALLERY = 1, CAMERA = 2;

    private ImageView imageview;
    private LayoutInflater inflater2;


    public All() {
        // Required empty public constructor
    }

    public static All newInstance() {
        All all = new All();
        return all;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.all, container, false);


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        fab_add=(FloatingActionButton)rootView.findViewById(R.id.fb_add);
        fab_random=(FloatingActionButton)rootView.findViewById(R.id.fb_random);
        fabOpen= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        fabClockwise= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_clockwise);
        fabAnticlockwise= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_anticlockwise);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fab_add.startAnimation(fabClose);
                    fab_random.startAnimation(fabClose);
                    fab.startAnimation(fabAnticlockwise);
                    fab_random.setClickable(false);
                    fab_add.setClickable(false);
                    isOpen=false;
                }else {
                    fab_add.startAnimation(fabOpen);
                    fab_random.startAnimation(fabOpen);
                    fab.startAnimation(fabClockwise);
                    fab_random.setClickable(true);
                    fab_add.setClickable(true);
                    isOpen=true;
                }
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FabActivity.class);
                 getActivity().startActivityForResult(intent,1);
            }
        });
        this.realm = RealmController.with(this).getRealm();
        setupRecycler();


        // refresh the realm instance

        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically


        if (!Prefs.with(getActivity()).getPreLoad()) {
            setRealmData();

        }
        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getRestaurant());

        Toast.makeText(getActivity(), "Press card item for edit, long press to remove item", Toast.LENGTH_LONG).show();

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              // Intent intent=new Intent(getActivity(), FabActivity.class);
              // getActivity().startActivityForResult(intent,1);
            }
        });*/
        adapter.notifyDataSetChanged();
        return rootView;
    }

    public void setRealmAdapter(RealmResults<Restaurant> restaurant) {

        RealmRestaurantAdapter realmAdapter = new RealmRestaurantAdapter(getActivity().getApplicationContext(), restaurant, true);
        // Set the data and tell the RecyclerView to draw
        recycler.setAdapter(adapter);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setRealmData() {

        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();

        Restaurant restaurant = new Restaurant();

        restaurant.setRestaurantTitle("MeetMee");
        restaurant.setRestaurantType("Nooddles ");
        restaurant.setDescription("Good Taste");
        restaurant.setImageRestaurant("https://api.androidhive.info/images/realm/4.png");
        restaurantArrayList.add(restaurant);


        for (Restaurant r : restaurantArrayList) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(r);
            realm.commitTransaction();
        }

        Prefs.with(getActivity()).setPreLoad(true);

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

    public String toString() {
        return "All";
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    if (imageview != null)
                        imageview.setImageBitmap(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (imageview != null)
                imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


}


