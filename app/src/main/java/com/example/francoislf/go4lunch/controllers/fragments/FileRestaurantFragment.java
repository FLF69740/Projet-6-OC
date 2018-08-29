package com.example.francoislf.go4lunch.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileRestaurantFragment extends Fragment {

    TextView mRestaurantName;
    @BindView(R.id.button_restaurant_choice)Button mButtonChoiceRestaurant;
    @BindView(R.id.restaurant_choice_no_validate)ImageView mImageViewRestaurantChoiceKO;
    @BindView(R.id.restaurant_choice_validate)ImageView mImageViewRestaurantChoiceOK;
    Boolean isRestaurantChoosen;

    public FileRestaurantFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_restaurant, container, false);
        this.mRestaurantName = view.findViewById(R.id.restaurant_name);
        ButterKnife.bind(this, view);
        isRestaurantChoosen = false;
        setCircleLogoResturantChoice(isRestaurantChoosen);
        return view;
    }

    public void setSnippetMarkerName(RestaurantProfile restaurantProfile){
        mRestaurantName.setText(restaurantProfile.getName());
    }

    @OnClick(R.id.button_restaurant_choice)
    public void changeRestaurantState(){
        isRestaurantChoosen = !isRestaurantChoosen;
        setCircleLogoResturantChoice(isRestaurantChoosen);
    }

    // define the logo of the circle customer restaurant choice
    private void setCircleLogoResturantChoice(boolean isChoosen){
        mImageViewRestaurantChoiceKO.setVisibility(View.INVISIBLE);
        mImageViewRestaurantChoiceOK.setVisibility(View.INVISIBLE);
        if (isChoosen) mImageViewRestaurantChoiceOK.setVisibility(View.VISIBLE);
        else mImageViewRestaurantChoiceKO.setVisibility(View.VISIBLE);
    }

}
