package com.example.francoislf.go4lunch.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    @BindView(R.id.avatar_situation)TextView mAvatarSituation;
    @BindView(R.id.avatar)ImageView mAvatar;

    public WorkmatesViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
    }

    public void updateWithWorkmatesProfile(User user){

        String avatarSituationBuild = user.getUsername();

        if (user.getRestaurantChoice().equals("Empty") ||
                new ChoiceRestaurantCountdown(user.getHourChoice(),user.getDateChoice()).getCountdownResult()) avatarSituationBuild += " " + mItemView.getContext().getString(R.string.pas_de_decision);
        else avatarSituationBuild += " " + mItemView.getContext().getString(R.string.decision) + " " + user.getRestaurantChoice();

        mAvatarSituation.setText(avatarSituationBuild);

        if (user.getUrlPicture() != null) Glide.with(mItemView).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mAvatar);
        else  Glide.with(mItemView).load(R.drawable.avatarobxlarge).apply(RequestOptions.circleCropTransform()).into(mAvatar);

    }


}
