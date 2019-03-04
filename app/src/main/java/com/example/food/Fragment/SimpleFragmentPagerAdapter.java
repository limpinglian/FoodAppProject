package com.example.food.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> pages=new ArrayList<>();
    private final List<String> lstTitle=new ArrayList<>();

    public SimpleFragmentPagerAdapter( FragmentManager fm) {

        super(fm);


    }
    public Fragment getItem(int position) {
      return pages.get(position);
    }
    public int getCount() {
        return pages.size();
    }

    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
return lstTitle.get(position);
    }

    public void addPage(Fragment f,String title)
    {
        pages.add(f);
        lstTitle.add(title);

    }

}
