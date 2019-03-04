package com.example.food.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nearby extends Fragment {


    public Nearby() {
        // Required empty public constructor
    }
    public static Nearby newInstance(){
        Nearby near=new Nearby();
        return near;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location, container, false);
        return rootView;

    }
    public String toString(){
        return "Nearby";
    }

}
