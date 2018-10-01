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
import com.example.francoislf.go4lunch.api.LikedHelper;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.RecyclerViewItemTransformer;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.LIKE_NUMBER_OF_LIKE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.LIKE_PLACEID;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_DATE_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_HOUR_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_RESTAURANT_CHOICE;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_recyclerView_name)TextView mTextViewName;
    @BindView(R.id.item_recyclerView_adress)TextView mTextViewAdress;
    @BindView(R.id.item_recyclerView_opening)TextView mTextViewOpening;
    @BindView(R.id.item_recyclerView_restaurant_photo)ImageView mImageViewPhoto;
    @BindView(R.id.item_recyclerView_distance)TextView mTextViewDistance;
    @BindView(R.id.item_recyclerView_participant_number)TextView mTextViewParticipantNumber;
    @BindView(R.id.participant_icon)ImageView mImageViewParticipantIcon;
    @BindView(R.id.item_recyclerView_star_1)ImageView mYellowStarOne;
    @BindView(R.id.item_recyclerView_star_2)ImageView mYellowStarTwo;
    @BindView(R.id.item_recyclerView_star_3)ImageView mYellowStarThree;
    View mItemView;
    RecyclerViewItemTransformer mRecyclerViewItemTransformer;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
        mRecyclerViewItemTransformer = new RecyclerViewItemTransformer(this.mItemView.getContext());
    }

    public void updateWithRestaurantProfile(final RestaurantProfile restaurantProfile){
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

        if (restaurantProfile.getPhoto() != null) {
            if (!restaurantProfile.getPhoto().equals(BLANK_ANSWER)) {
                Glide.with(mItemView)
                        .load(restaurantProfile.getPhoto())
                        .apply(RequestOptions.centerCropTransform())
                        .into(mImageViewPhoto);
            }
        }

        UserHelper.getAllUsers().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberOfParticipant = 0;
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (restaurantProfile.getName().equals(document.getString(USER_RESTAURANT_CHOICE))) {
                        if (!new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE), document.getString(USER_DATE_CHOICE)).getCountdownResult())
                            numberOfParticipant++;
                    }

                    String participantString = "(" + numberOfParticipant + ")";
                    if (!participantString.equals("(0)")) {
                        mTextViewParticipantNumber.setText(participantString);
                        mTextViewParticipantNumber.setVisibility(View.VISIBLE);
                        mImageViewParticipantIcon.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        LikedHelper.getAllLiked().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberOfLike = 0;
                for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                    if (restaurantProfile.getPlaceId().equals(document.getString(LIKE_PLACEID))){
                        numberOfLike = document.getLong(LIKE_NUMBER_OF_LIKE).intValue();
                    }
                }

                if (numberOfLike >= 1) mYellowStarOne.setVisibility(View.VISIBLE);
                else mYellowStarOne.setVisibility(View.INVISIBLE);
                if (numberOfLike >= 5) mYellowStarTwo.setVisibility(View.VISIBLE);
                else mYellowStarTwo.setVisibility(View.INVISIBLE);
                if (numberOfLike >= 10) mYellowStarThree.setVisibility(View.VISIBLE);
                else mYellowStarThree.setVisibility(View.INVISIBLE);
            }
        });

    }

}
