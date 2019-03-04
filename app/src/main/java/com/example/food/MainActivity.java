package com.example.food;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food.Adapter.RealmRestaurantAdapter;
import com.example.food.Adapter.RestaurantAdapter;
import com.example.food.Fragment.Favourite;
import com.example.food.App.Prefs;
import com.example.food.Fragment.All;
import com.example.food.Fragment.Nearby;
import com.example.food.Fragment.SimpleFragmentPagerAdapter;
import com.example.food.Model.Restaurant;
import com.example.food.Realm.RealmController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tab;
    private ViewPager vp;
    private SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
    private RestaurantAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;

    private static final String IMAGE_DIRECTORY = "/images";
    private int GALLERY = 1, CAMERA = 2;

    private ImageView imageview;
    private Uri imageCaptureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tab = (TabLayout) findViewById(R.id.tabs);
        vp = (ViewPager) findViewById(R.id.viewpager);
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

//        Fragment fragment = new All();
//        ((All) fragment).addItem();
        simpleFragmentPagerAdapter.addPage(All.newInstance(), "All");
        simpleFragmentPagerAdapter.addPage(new Favourite(), "Favourite");
        simpleFragmentPagerAdapter.addPage(new Nearby(), "Nearby");

        vp.setAdapter(simpleFragmentPagerAdapter);
        tab.setupWithViewPager(vp);


        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setupWithViewPager(vp);
        tab.addOnTabSelectedListener(this);


        this.realm = RealmController.with(this).getRealm();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
