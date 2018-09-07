package com.example.francoislf.go4lunch.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileRestaurantFragment extends Fragment {

    TextView mRestaurantName, mRestaurantLocalisation;
    @BindView(R.id.button_restaurant_choice)Button mButtonChoiceRestaurant;
    @BindView(R.id.restaurant_choice_no_validate)ImageView mImageViewRestaurantChoiceKO;
    @BindView(R.id.restaurant_choice_validate)ImageView mImageViewRestaurantChoiceOK;
    ImageView mRestaurantImage;
    Boolean isRestaurantChosen;
    String mPhoneNumber, mWebSite;
    View mView;

    public FileRestaurantFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_file_restaurant, container, false);
        this.mRestaurantName = mView.findViewById(R.id.restaurant_name);
        this.mRestaurantLocalisation = mView.findViewById(R.id.restaurant_localisation);
        this.mRestaurantImage = mView.findViewById(R.id.restaurant_photo);
        ButterKnife.bind(this, mView);
        isRestaurantChosen = false;
        setCircleLogoRestaurantChoice(isRestaurantChosen);
        return mView;
    }

    public void setRestaurantProfileInformation(RestaurantProfile restaurantProfile){
        mRestaurantName.setText(restaurantProfile.getName());
        mRestaurantLocalisation.setText(restaurantProfile.getAdress());
        mPhoneNumber = restaurantProfile.getPhoneNumber();
        mWebSite = restaurantProfile.getWebSite();
        if (!restaurantProfile.getPhoto().equals("Empty")) {
            Glide.with(mView)
                    .load(restaurantProfile.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestaurantImage);
        }
    }

    @OnClick(R.id.button_restaurant_choice)
    public void changeRestaurantState(){
        isRestaurantChosen = !isRestaurantChosen;
        setCircleLogoRestaurantChoice(isRestaurantChosen);
    }

    // define the logo of the circle customer restaurant choice
    private void setCircleLogoRestaurantChoice(boolean isChoosen){
        mImageViewRestaurantChoiceKO.setVisibility(View.INVISIBLE);
        mImageViewRestaurantChoiceOK.setVisibility(View.INVISIBLE);
        if (isChoosen) mImageViewRestaurantChoiceOK.setVisibility(View.VISIBLE);
        else mImageViewRestaurantChoiceKO.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.CALL)
    public void launchRestaurantCall(){
        if (mPhoneNumber == null) Toast.makeText(getContext(),getString(R.string.callDisabled),Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(),mPhoneNumber,Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.WEBSITE)
    public void launchRestaurantWebSite(){
        if (mWebSite == null) Toast.makeText(getContext(), getString(R.string.websiteDisabled), Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(),mWebSite,Toast.LENGTH_LONG).show();
    }

}
