package com.example.francoislf.go4lunch.controllers.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.LikedHelper;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.controllers.fragments.FileRestaurantFragment;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.example.francoislf.go4lunch.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class FileRestaurantActivity extends BaseActivity implements FileRestaurantFragment.OnClicChoiceRestaurant{

    public static final String EXTRA_SNIPPET_MARKER = "EXTRA_SNIPPET_MARKER";
    private static final String BLANK_ANSWER = "Empty";
    private FileRestaurantFragment mFileRestaurantFragment;
    RestaurantProfile mRestaurantProfile;

    @Override
    protected boolean getContentViewBoolean() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_file_restaurant;
    }

    @Override
    protected android.app.Fragment newInstance() {
        mFileRestaurantFragment = new FileRestaurantFragment();
        return mFileRestaurantFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.id.frame_layout_file_restaurant;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.snippetMarkerTransmissionToFragment();
        this.snippetLikeTransmissionToFragment();
        this.creationListOfJoiningWorkmates();
    }

    // Update fragment UI about choice button
    private void snippetMarkerTransmissionToFragment(){
        mRestaurantProfile = getIntent().getExtras().getParcelable(EXTRA_SNIPPET_MARKER);
        mFileRestaurantFragment.setRestaurantProfileInformation(mRestaurantProfile);
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                if (!currentUser.getHourChoice().equals(BLANK_ANSWER) &&
                new ChoiceRestaurantCountdown(currentUser.getHourChoice(), currentUser.getDateChoice()).getCountdownResult())
                    mFileRestaurantFragment.setRestaurantChoiceForButton(BLANK_ANSWER);
                else mFileRestaurantFragment.setRestaurantChoiceForButton(currentUser.getRestaurantChoice());

            }
        });
    }

    // Update fragment UI about Like button and database restaurants collection update if necessary
    private void snippetLikeTransmissionToFragment(){
        LikedHelper.getLikedCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        boolean toCreate = true;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getString(LIKE_PLACEID).equals(mRestaurantProfile.getPlaceId())) {
                                toCreate = false;
                                if (document.getString(LIKE_PARTICIPANTS).contains(getCurrentUser().getUid()))
                                    mFileRestaurantFragment.setLikeButton(true, document.getLong(LIKE_NUMBER_OF_LIKE).intValue(), document.getString(LIKE_PARTICIPANTS), toCreate);
                                else
                                    mFileRestaurantFragment.setLikeButton(false, document.getLong(LIKE_NUMBER_OF_LIKE).intValue(), document.getString(LIKE_PARTICIPANTS), toCreate);
                            }
                        }
                        if (toCreate) mFileRestaurantFragment.setLikeButton(false, 0, "", toCreate);
                    }
                    else mFileRestaurantFragment.setLikeButton(false, 0, "", true);
                }
            }
        });
    }

    // callback from fragment child about User update
    @Override
    public void onResultChoiceTransmission(View view, String name, String placeId, int hour, int date) {
        final DateTime dt = new DateTime();
        if (!name.equals(BLANK_ANSWER)) {
            // add the restaurant choice to user Firestore dataBase
            UserHelper.updateRestaurantChoice(name, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateHourChoice(String.valueOf(dt.getHourOfDay()), getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateDateChoice(String.valueOf(dt.getDayOfYear()), getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
        }
        else { // delete daily choice from user after retractation
            UserHelper.updateRestaurantChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateDateChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateHourChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
        }
    }

    // callback from fragment child about Restaurant Like update
    @Override
    public void onResultLikeTransmission(View view, String listOfParticipant, String placeId, String decision, int like, boolean toCreate) {
        boolean boolLike;
        if (toCreate) {
            LikedHelper.createLiked(mRestaurantProfile.getName(), placeId, this.getCurrentUser().getUid());
            like = 1;
            listOfParticipant = this.getCurrentUser().getUid() + ";";
            boolLike = true;
        }
        else {
            if (decision.equals(getString(R.string.LIKE))) {
                boolLike = true;
                listOfParticipant += getCurrentUser().getUid() + ";";
                LikedHelper.updateParticipants(placeId, listOfParticipant);
                like++;
                LikedHelper.updateLiked(placeId, like);
                Toast.makeText(this, getString(R.string.LIKE) + " + 1",Toast.LENGTH_LONG).show();
            } else {
                boolLike = false;
                listOfParticipant = listOfParticipant.replace(getCurrentUser().getUid() + ";", "");
                LikedHelper.updateParticipants(placeId, listOfParticipant);
                like--;
                LikedHelper.updateLiked(placeId, like);
                Toast.makeText(this, getString(R.string.LIKE) + " - 1",Toast.LENGTH_LONG).show();
            }
        }
        mFileRestaurantFragment.setLikeButton(boolLike, like, listOfParticipant,false);
    }

    @Override
    public void webSiteVisiting(View view, String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.LINK_WEBVIEW, url);
        startActivity(intent);
    }

    // define the list of workmates participation for the restaurant
    private void creationListOfJoiningWorkmates(){
        UserHelper.getAllUsers().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.getString(USER_RESTAURANT_CHOICE) != null) {
                            if (document.getString(USER_RESTAURANT_CHOICE).equals(mRestaurantProfile.getName())
                                    && !document.getString(USER_UID).equals(getCurrentUser().getUid())
                                    && !new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE), document.getString(USER_DATE_CHOICE)).getCountdownResult()) {
                                userList.add(document.toObject(User.class));
                            }
                        }
                    }
                    mFileRestaurantFragment.configureRecyclerView(userList);
                }
            }
        });
    }



}
