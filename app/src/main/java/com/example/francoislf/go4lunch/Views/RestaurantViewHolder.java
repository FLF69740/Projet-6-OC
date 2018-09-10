package com.example.francoislf.go4lunch.Views;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.item_recyclerView_distance)TextView mTextViewDistance;
    View mItemView;
    RecyclerViewItemTransformer mRecyclerViewItemTransformer;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
        mRecyclerViewItemTransformer = new RecyclerViewItemTransformer(this.mItemView.getContext());
    }

    public void updateWithRestaurantProfile(RestaurantProfile restaurantProfile){
        this.mTextViewName.setText(mRecyclerViewItemTransformer.getGoodSizeName(restaurantProfile.getName()));
        this.mTextViewAdress.setText(restaurantProfile.getAdress());

        List<String> openingList = new ArrayList<>(restaurantProfile.getWeekHour());
        this.mTextViewOpening.setText(mRecyclerViewItemTransformer.getOpeningAnswer(openingList));
        if (mTextViewOpening.getText().equals(mItemView.getContext().getString(R.string.fermé)) ||
                mTextViewOpening.getText().equals(mItemView.getContext().getString(R.string.bientôt_fermé))){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTextViewOpening.setTextAppearance(R.style.red_text);
            }
        }
        this.mTextViewDistance.setText(mRecyclerViewItemTransformer.getDistance(restaurantProfile.getLat(), restaurantProfile.getLng()));

        if (!restaurantProfile.getPhoto().equals("Empty")){
            Glide.with(mItemView)
                    .load(restaurantProfile.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .into(mImageViewPhoto);
        }    }

}
