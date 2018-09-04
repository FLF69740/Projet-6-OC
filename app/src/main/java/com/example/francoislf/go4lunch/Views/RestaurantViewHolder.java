package com.example.francoislf.go4lunch.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.RecyclerViewItemTransformer;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_recyclerView_name)TextView mTextViewName;
    @BindView(R.id.item_recyclerView_adress)TextView mTextViewAdress;
    @BindView(R.id.item_recyclerView_opening)TextView mTextViewOpening;
    @BindView(R.id.item_recyclerView_restaurant_photo)ImageView mImageViewPhoto;
    View mItemView;
    RecyclerViewItemTransformer mRecyclerViewItemTransformer;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
        mRecyclerViewItemTransformer = new RecyclerViewItemTransformer();
    }

    public void updateWithRestaurantProfile(RestaurantProfile restaurantProfile){
        this.mTextViewName.setText(restaurantProfile.getName());
        this.mTextViewAdress.setText(restaurantProfile.getAdress());

        List<String> openingList = new ArrayList<>(restaurantProfile.getWeekHour());
        this.mTextViewOpening.setText(mRecyclerViewItemTransformer.getOpeningAnswer(openingList));

        if (!restaurantProfile.getPhoto().equals("Empty")){
            Glide.with(mItemView)
                    .load(restaurantProfile.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mImageViewPhoto);
        }    }

}
