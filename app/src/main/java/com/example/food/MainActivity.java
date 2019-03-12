package com.example.food;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.food.Fragment.Favourite;
import com.example.food.Fragment.All;
import com.example.food.Fragment.Nearby;
import com.example.food.Fragment.SimpleFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private TabLayout tab;
    private ViewPager vp;
    private SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab = findViewById(R.id.tabs);
        vp = findViewById(R.id.viewpager);

        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        simpleFragmentPagerAdapter.addPage(All.newInstance(), "All");
        simpleFragmentPagerAdapter.addPage(new Favourite(), "Favourite");
        simpleFragmentPagerAdapter.addPage(new Nearby(), "Nearby");

        vp.setAdapter(simpleFragmentPagerAdapter);
        tab.setupWithViewPager(vp);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setupWithViewPager(vp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food App");
    }
}
