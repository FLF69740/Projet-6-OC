package com.example.francoislf.go4lunch.Views;

import android.os.Build;
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

import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;

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

        if (user.getRestaurantChoice().equals(BLANK_ANSWER) ||
                new ChoiceRestaurantCountdown(user.getHourChoice(),user.getDateChoice()).getCountdownResult()) {
            avatarSituationBuild += " " + mItemView.getContext().getString(R.string.pas_de_decision);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mAvatarSituation.setTextAppearance(R.style.italic_text);
            }
        }
        else avatarSituationBuild += " " + mItemView.getContext().getString(R.string.decision) + " " + user.getRestaurantChoice();

        mAvatarSituation.setText(avatarSituationBuild);

        if (user.getUrlPicture() != null) Glide.with(mItemView).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mAvatar);
        else  Glide.with(mItemView).load(R.drawable.avatarobxlarge).apply(RequestOptions.circleCropTransform()).into(mAvatar);

    }


}
