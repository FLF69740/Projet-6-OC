package com.example.francoislf.go4lunch.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.francoislf.go4lunch.R;

public class FileRestaurantFragment extends Fragment {

    TextView mRestaurantName;

    public FileRestaurantFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_restaurant, container, false);
        this.mRestaurantName = view.findViewById(R.id.restaurant_name);
        return view;
    }

    public void setSnippetMarkerName(String name){
        mRestaurantName.setText(name);
    }

}
