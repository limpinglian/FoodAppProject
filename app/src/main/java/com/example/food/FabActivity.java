package com.example.food;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.food.Model.Restaurant;
import com.example.food.Realm.RestaurantRealmHelper;
import com.squareup.picasso.Picasso;


import java.util.Random;

public class FabActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/images";
    private static final int GALLERY = 1, CAMERA = 2;

    private ImageView imageview;
    private RatingBar ratingBar;
    private EditText editTitle;
    private EditText editDescription;
    private Spinner tySpinner;
    private Button Upload;
    private Button Cancel;
    private Button Submit;

    private String sUri;

    private RestaurantRealmHelper restaurantRealmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        ratingBar = findViewById(R.id.et_rate);
        editTitle = findViewById(R.id.name);
        editDescription = findViewById(R.id.description);
        tySpinner = findViewById(R.id.spinner);
        imageview = findViewById(R.id.ivRestaurant);
        Upload = findViewById(R.id.btnUpload);
        Cancel = findViewById(R.id.btnCancel);
        Submit = findViewById(R.id.btnOk);

        restaurantRealmHelper = new RestaurantRealmHelper();

        final String totalStar = String.valueOf(ratingBar.getNumStars());
        final int numStar = Integer.parseInt(totalStar);

        ArrayAdapter<String> tyAdapter = new ArrayAdapter<String>(FabActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));
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
                String id = Integer.toString(random);
                restaurant.setId(id);
                restaurant.setRestaurantTitle(editTitle.getText().toString());
                restaurant.setRestaurantType(tySpinner.getSelectedItem().toString());
                restaurant.setDescription(editDescription.getText().toString());
                restaurant.setImageRestaurant(sUri);
                restaurant.setRatingStar(numStar);

                if (editTitle.getText() == null || editDescription.getText().toString().equals("")) {
                    Toast.makeText(FabActivity.this, "Entry not saved, missing info", Toast.LENGTH_SHORT).show();
                } else if (numStar == 0) {
                    Toast.makeText(FabActivity.this, "Entry not saved, missing info", Toast.LENGTH_SHORT).show();
                } else {
                    // Persist your data easily
                    restaurantRealmHelper.getRealm().beginTransaction();
                    restaurantRealmHelper.getRealm().copyToRealm(restaurant);
                    restaurantRealmHelper.getRealm().commitTransaction();
                    Toast.makeText(FabActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                    // scroll the recycler view to bottom
//                    recycler.scrollToPosition(RestaurantRealmHelper.getInstance().getRestaurant().size() );

                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                Picasso.get().load(contentURI).into(imageview);
                if (contentURI != null) {
                    sUri = contentURI.toString();
                }
                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (imageview != null)
                imageview.setImageBitmap(thumbnail);

            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
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
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
}

