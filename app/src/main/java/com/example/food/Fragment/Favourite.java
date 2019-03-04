package com.example.food.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {



    public Favourite() {
        // Required empty public constructor
    }
    public static Favourite newInstance(){
        Favourite fav=new Favourite();
        return fav;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favourite, container, false);


        return rootView;
    }
    public String toString(){
        return "Favourite";
    }

}
