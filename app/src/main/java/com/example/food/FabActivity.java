package com.example.food;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.food.Adapter.RealmRestaurantAdapter;
import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.App.Prefs;
import com.example.food.Fragment.All;
import com.example.food.Model.Restaurant;
import com.example.food.Realm.RealmController;
import com.squareup.picasso.Picasso;


import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class FabActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private RestaurantAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;

    private static final String IMAGE_DIRECTORY = "/images";
    private int GALLERY = 1, CAMERA = 2;

    private ImageView imageview;
    private LayoutInflater inflater2;

    Uri contentURI;
    String sUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        final RatingBar ratingBar=(RatingBar)findViewById(R.id.et_rate);
        final EditText editTitle = (EditText) findViewById(R.id.name);
        String record;
        final EditText editDescription = (EditText) findViewById(R.id.description);

        final Spinner tySpinner=(Spinner)findViewById(R.id.spinner);
        imageview = (ImageView) findViewById(R.id.ivRestaurant);
        Button Upload = (Button) findViewById(R.id.btnUpload);
        Button Cancel=(Button)findViewById(R.id.btnCancel);
        Button Submit = (Button) findViewById(R.id.btnOk);
        final RecyclerView recycler=(RecyclerView)findViewById(R.id.recycler);
        this.realm = RealmController.with(this).getRealm();
        adapter = new RestaurantAdapter(this);



         final String totalStar=String.valueOf(ratingBar.getNumStars());
          final int numStar=Integer.parseInt(totalStar);

        ArrayAdapter<String>tyAdapter=new ArrayAdapter<String>(FabActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.types));
        tyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tySpinner.setAdapter(tyAdapter);

        tySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurant = new Restaurant();
                final int min = 20;
                final int max = 80;
                final int random = new Random().nextInt((max - min) + 1) + min;
                String id=Integer.toString(random);
                restaurant.setId(id);
                restaurant.setRestaurantTitle(editTitle.getText().toString());
                restaurant.setRestaurantType(tySpinner.getSelectedItem().toString());
                restaurant.setDescription(editDescription.getText().toString());
                restaurant.setImageRestaurant(sUri);
                restaurant.setRatingStar(numStar);


                if (editTitle.getText() == null || editDescription.getText().toString().equals("") ) {
                    Toast.makeText(FabActivity.this, "Entry not saved, missing info", Toast.LENGTH_SHORT).show();
                }else if(numStar==0){
                    Toast.makeText(FabActivity.this, "Entry not saved, missing info", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Persist your data easily
                    realm.beginTransaction();
                    realm.copyToRealm(restaurant);
                    realm.commitTransaction();
                    Toast.makeText(FabActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                    adapter.notifyDataSetChanged();


                    // scroll the recycler view to bottom
                    recycler.scrollToPosition(RealmController.getInstance().getRestaurant().size() );

                }
            }
        });



    }


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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
                 contentURI = data.getData();

//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                    Picasso.get().load(contentURI).into(imageview);

                    sUri = contentURI.toString();
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();


//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (imageview != null)
                imageview.setImageBitmap(thumbnail);

            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }





}

