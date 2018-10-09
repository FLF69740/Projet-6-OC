package com.example.francoislf.go4lunch.controllers.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Utils.MyAlarmReceiver;
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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import static com.example.francoislf.go4lunch.controllers.activities.SettingsActivity.SWITCH_STATE;

public class FileRestaurantActivity extends BaseActivity implements FileRestaurantFragment.OnClicChoiceRestaurant{

    public static final String EXTRA_SNIPPET_MARKER = "EXTRA_SNIPPET_MARKER";
    private FileRestaurantFragment mFileRestaurantFragment;
    RestaurantProfile mRestaurantProfile;
    private PendingIntent mPendingIntent;
    public static final String UID_SETTINGS = "UID_SETTINGS";
    public static final int NOTIFICATION_CODE = 100;
    SharedPreferences mPreferences;
    Boolean mIsNotificationActivated;

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
    protected String getFragmentTag() {
        return TAG_FILE_RESTAURANT_FRAGMENT;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mIsNotificationActivated = Boolean.valueOf(mPreferences.getString(SWITCH_STATE, null));
        this.snippetMarkerTransmissionToFragment();
        this.snippetLikeTransmissionToFragment();
        this.creationListOfJoiningWorkmates();
    }

    // Update fragment UI about choice button
    private void snippetMarkerTransmissionToFragment(){
        mRestaurantProfile = Objects.requireNonNull(getIntent().getExtras()).getParcelable(EXTRA_SNIPPET_MARKER);
        mFileRestaurantFragment.setRestaurantProfileInformation(mRestaurantProfile);
        UserHelper.getUser(Objects.requireNonNull(this.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                if (currentUser != null) {
                    if (!currentUser.getHourChoice().equals(BLANK_ANSWER) &&
                    new ChoiceRestaurantCountdown(currentUser.getHourChoice(), currentUser.getDateChoice()).getCountdownResult())
                        mFileRestaurantFragment.setRestaurantChoiceForButton(BLANK_ANSWER);
                    else mFileRestaurantFragment.setRestaurantChoiceForButton(currentUser.getRestaurantChoice());
                }
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
                            if (Objects.equals(document.getString(LIKE_PLACEID), mRestaurantProfile.getPlaceId())) {
                                toCreate = false;
                                if (Objects.requireNonNull(document.getString(LIKE_PARTICIPANTS)).contains(Objects.requireNonNull(getCurrentUser()).getUid()))
                                    mFileRestaurantFragment.setLikeButton(true, Objects.requireNonNull(document.getLong(LIKE_NUMBER_OF_LIKE)).intValue(),
                                            document.getString(LIKE_PARTICIPANTS), false);
                                else
                                    mFileRestaurantFragment.setLikeButton(false, Objects.requireNonNull(document.getLong(LIKE_NUMBER_OF_LIKE)).intValue(),
                                            document.getString(LIKE_PARTICIPANTS), false);
                            }
                        }
                        if (toCreate) mFileRestaurantFragment.setLikeButton(false, 0, "", true);
                    }
                    else mFileRestaurantFragment.setLikeButton(false, 0, "", true);
                }
            }
        });
    }

    // callback from fragment child about User update
    @Override
    public void onResultChoiceTransmission(View view, String name, String adress, String placeId, int hour, int date) {
        final DateTime dt = new DateTime();
        if (mIsNotificationActivated != null && mIsNotificationActivated) this.configureAlarmManager();
        if (!name.equals(BLANK_ANSWER)) {
            // add the restaurant choice to user Firestore dataBase
            UserHelper.updateRestaurantChoice(name, Objects.requireNonNull(getCurrentUser()).getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateHourChoice(String.valueOf(dt.getHourOfDay()), getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateDateChoice(String.valueOf(dt.getDayOfYear()), getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateAdressRestaurant(adress, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            if (mIsNotificationActivated != null && mIsNotificationActivated) startAlarm();
        }
        else { // delete daily choice from user after retractation
            UserHelper.updateRestaurantChoice(BLANK_ANSWER, Objects.requireNonNull(getCurrentUser()).getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateDateChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateHourChoice(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            UserHelper.updateAdressRestaurant(BLANK_ANSWER, getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            if (mIsNotificationActivated != null && mIsNotificationActivated) stopAlarm();
        }
    }

    // callback from fragment child about Restaurant Like update
    @Override
    public void onResultLikeTransmission(View view, String listOfParticipant, String placeId, String decision, int like, boolean toCreate) {
        boolean boolLike;
        if (toCreate) {
            LikedHelper.createLiked(mRestaurantProfile.getName(), placeId, Objects.requireNonNull(this.getCurrentUser()).getUid());
            like = 1;
            listOfParticipant = this.getCurrentUser().getUid() + ";";
            boolLike = true;
        } else {
            if (decision.equals(getString(R.string.LIKE))) {
                boolLike = true;
                listOfParticipant += Objects.requireNonNull(getCurrentUser()).getUid() + ";";
                LikedHelper.updateParticipants(placeId, listOfParticipant);
                like++;
                LikedHelper.updateLiked(placeId, like);
                Toast.makeText(this, getString(R.string.LIKE) + " + 1",Toast.LENGTH_LONG).show();
            } else {
                boolLike = false;
                listOfParticipant = listOfParticipant.replace(Objects.requireNonNull(getCurrentUser()).getUid() + ";", "");
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
                            if (Objects.equals(document.getString(USER_RESTAURANT_CHOICE), mRestaurantProfile.getName())
                                    && !Objects.equals(document.getString(USER_UID), Objects.requireNonNull(getCurrentUser()).getUid())
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

    /**
     *  ALARM MANAGER
     */

    private void configureAlarmManager(){
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra(UID_SETTINGS, Objects.requireNonNull(getCurrentUser()).getUid());
        mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);

        DateTime dt = new DateTime();
        if (dt.getHourOfDay() > 11) calendar.add(Calendar.DAY_OF_YEAR, 1);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPendingIntent); }
        }
    }

    private void stopAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) alarmManager.cancel(mPendingIntent);
    }
}
