package com.example.francoislf.go4lunch.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IsJoiningHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    @BindView(R.id.avatar_situation)TextView mAvatarSituation;
    @BindView(R.id.avatar)ImageView mAvatar;

    public IsJoiningHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
    }

    public void updateAvatarIsJoining(User user){

        String avatarSituationBuild = user.getUsername() + " " + mItemView.getContext().getString(R.string.is_joining);
        mAvatarSituation.setText(avatarSituationBuild);
        if (user.getUrlPicture() != null) Glide.with(mItemView).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mAvatar);
        else  Glide.with(mItemView).load(R.drawable.avatarobxlarge).apply(RequestOptions.circleCropTransform()).into(mAvatar);
    }

}
