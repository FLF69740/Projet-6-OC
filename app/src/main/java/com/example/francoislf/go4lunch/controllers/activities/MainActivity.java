package com.example.francoislf.go4lunch.controllers.activities;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.controllers.fragments.ListViewFragment;
import com.example.francoislf.go4lunch.controllers.fragments.MainFragment;
import com.example.francoislf.go4lunch.controllers.fragments.WorkmatesFragment;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.HttpRequest.GoogleStreams;
import com.example.francoislf.go4lunch.models.HttpRequest.Places;
import com.example.francoislf.go4lunch.models.PlacesExtractor;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnClickedResultMarker, ListViewFragment.OnClickedResultItem, WorkmatesFragment.OnClickedAvatarItem {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Toolbar mToolbar;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.toolbar_edit_text) EditText mToolbarEditText;
    @BindView(R.id.toolbar_button) TextView mToolBarButton;
    @BindView(R.id.imageView4) ImageView mToolbarSeparator;
    private ArrayList<RestaurantProfile> mRestaurantProfileList;
    PlacesExtractor mPlacesExtractor;
    private Disposable mDisposable, mDisposable2;
    private static final String PARAMETER_PLACE_API_RADIUS = "1000";
    private static final String PARAMETER_PLACE_API_TYPE = "restaurant";
    private static final String PARAMETER_AUTOCOMPLETE_TYPE = "establishment";
    private String PARAMETER_PLACE_API_KEY;
    private MainFragment mMainFragment;
    private String mCoordinates;

    @Override
    protected int getContentView() {return R.layout.activity_main;}
    @Override
    protected Fragment newInstance() {mMainFragment = new MainFragment(); return mMainFragment;}
    @Override
    protected int getFragmentLayout() {return (R.id.frame_layout_main);}
    @Override
    protected String getFragmentTag() {return TAG_MAIN_FRAGMENT;}

    @Override
    protected boolean getContentViewBoolean() {return true;}
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        PARAMETER_PLACE_API_KEY = getString(R.string.google_place_api_key);
        mRestaurantProfileList = new ArrayList<>();
        mPlacesExtractor = new PlacesExtractor(this);
        mToolbarEditText.setText("");
        configureToolbar();
        this.askLocationPermission();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.updateProfileInformations();
        this.configureEditTextToolbarListener();
    }

    // Update Nave_header informations about user informations
    protected void updateProfileInformations() {
        if (this.getCurrentUser() != null){
            if (this.getCurrentUser().getPhotoUrl() != null)
                Glide.with(this).load(this.getCurrentUser().getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(mImageViewProfile);
            else Glide.with(this).load(R.drawable.avatarobxlarge).apply(RequestOptions.circleCropTransform()).into(mImageViewProfile);
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();
            this.mTextViewEmail.setText(email);
            this.mTextViewName.setText(username);
        }
    }

    /**
     * BOTTOM NAVIGATION BAR
     */

    private void configureBottomNavigationView(){
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_onglet_map_view: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_1));
                        getFragmentManager().beginTransaction().replace(getFragmentLayout(), newInstance(), TAG_MAIN_FRAGMENT).commit();
                        configureToolbarTitle(1);
                    break;
                    case R.id.ic_onglet_list_view: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_2));
                        getFragmentManager().beginTransaction().replace(getFragmentLayout(), ListViewFragment.newInstanceWithList(mRestaurantProfileList), TAG_LISTVIEW_FRAGMENT).commit();
                        configureToolbarTitle(2);
                    break;
                    case R.id.ic_onglet_workmates: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_3));
                        getFragmentManager().beginTransaction().replace(getFragmentLayout(), WorkmatesFragment.newInstanceWorkmates(), TAG_WORKMATES_FRAGMENT).commit();
                        configureToolbarTitle(3);
                    break;
                }
                return true;
            }
        });
    }

    /**
     * Toolbar
     */

    //Configure toolbar
    protected void configureToolbar(){
        this.mToolbar = findViewById(R.id.activity_main_toolbar);
        mToolbar.setTitle(R.string.general_toolbar_title);
        setSupportActionBar(mToolbar);
    }

    private void configureToolbarTitle(int index){
        if (index == 3) mToolbar.setTitle(getString(R.string.toolbar_title_frag3));
        else mToolbar.setTitle(getString(R.string.general_toolbar_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_activity_main_search:
                mToolbarEditText.setVisibility(View.VISIBLE);
                mToolBarButton.setVisibility(View.VISIBLE);
                mToolbarSeparator.setVisibility(View.VISIBLE);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.toolbar_button)
    public void hideSearchAutocomplete(){
        mToolbarEditText.setVisibility(View.INVISIBLE);
        mToolBarButton.setVisibility(View.INVISIBLE);
        mToolbarSeparator.setVisibility(View.INVISIBLE);
    }

    /**
     * Navigation drawer
     */

    // Configure Drawer Layout
    protected void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure Navigation View
    protected void configureNavigationView(){
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
        mViewHeader = mNavigationView.getHeaderView(0);
        mTextViewName = mViewHeader.findViewById(R.id.nave_header_name);
        mTextViewEmail = mViewHeader.findViewById(R.id.nave_header_email);
        mImageViewProfile = mViewHeader.findViewById(R.id.nave_header_picture);
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) this.mDrawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.your_lunch : findChoiceRestaurant(); break;
            case R.id.settings : startSettings(); break;
            case R.id.logout : signOutFormFirebase(); break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Find the choice restaurant from user
    private void findChoiceRestaurant(){
        UserHelper.getUser(Objects.requireNonNull(getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!Objects.equals(documentSnapshot.getString(USER_RESTAURANT_CHOICE), BLANK_ANSWER)){
                        if (!new ChoiceRestaurantCountdown(documentSnapshot.getString(USER_HOUR_CHOICE), documentSnapshot.getString(USER_DATE_CHOICE)).getCountdownResult())
                            onResultItemTransmission(getCurrentFocus(), documentSnapshot.getString(USER_RESTAURANT_CHOICE));
                        else Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.your_lunch), Toast.LENGTH_LONG).show();
                } else Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.your_lunch), Toast.LENGTH_LONG).show();
            }
        });
    }

    // start settings activity
    private void startSettings(){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    // Callback from MainFragment when a listener from a marker is activated : launch FileRestaurantActivity
    @Override
    public void onResultMarkerTransmission(View view, String title) {
        Intent intent = new Intent(this, FileRestaurantActivity.class);
        RestaurantProfile restaurantProfile = mPlacesExtractor.getRestaurantProfile(title);
        intent.putExtra(FileRestaurantActivity.EXTRA_SNIPPET_MARKER, restaurantProfile);
        startActivity(intent);
    }

    // Callback from ListViewFragment when a listener from an item is activated : launch FileRestaurantActivity
    @Override
    public void onResultItemTransmission(View view, String title) {
        Intent intent = new Intent(this, FileRestaurantActivity.class);
        RestaurantProfile restaurantProfile = mPlacesExtractor.getRestaurantProfile(title);
        intent.putExtra(FileRestaurantActivity.EXTRA_SNIPPET_MARKER, restaurantProfile);
        startActivity(intent);
    }

    // Callback from WorkmatesFragment when a listener from an item is activated : launch FileRestaurantActivity
    @Override
    public void onResultAvatarTransmission(View view, String title) {
        if (!title.equals(BLANK_ANSWER)){
            Intent intent = new Intent(this, FileRestaurantActivity.class);
            RestaurantProfile restaurantProfile = mPlacesExtractor.getRestaurantProfile(title);
            intent.putExtra(FileRestaurantActivity.EXTRA_SNIPPET_MARKER, restaurantProfile);
            startActivity(intent);
        }
    }

    // configure the listener of toolbar EditText
    private void configureEditTextToolbarListener(){
        this.mToolbarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_LISTVIEW_FRAGMENT);
                if (s.length() > 2) executePlacesWithExtractorPrediction(s.toString());
                else if (s.length() < 2 && fragment != null){
                    executePlacesWithExtractor(mCoordinates);
                    getFragmentManager().beginTransaction()
                            .replace(getFragmentLayout(), ListViewFragment.newInstanceWithList(mRestaurantProfileList), TAG_LISTVIEW_FRAGMENT).commit();
                }
                else executePlacesWithExtractor(mCoordinates);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // getting location permissions
    private void askLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            getFragmentManager().beginTransaction()
                                    .replace(getFragmentLayout(), newInstance(), TAG_MAIN_FRAGMENT).commit();
                            return;
                        }
                    }
                }}
    }

    /**
     *  HTTP (RxJAVA)
     */

    @Override
    public void executePlacesCallback(View view, String coordinates) {
        String toolbarText = mToolbarEditText.getText().toString();
        if (toolbarText.length() < 2) executePlacesWithExtractor(coordinates);
        else executePlacesWithExtractorPrediction(toolbarText);
    }

    // Observable with autocomplete api for places
    private void executePlacesWithExtractorPrediction(String result){
        this.mDisposable2 = GoogleStreams.streamListPlacesPrediction(result, mCoordinates, PARAMETER_PLACE_API_RADIUS, PARAMETER_AUTOCOMPLETE_TYPE, PARAMETER_PLACE_API_KEY)
                .subscribeWith(new DisposableObserver<List<Places>>() {
                    @Override
                    public void onNext(List<Places> places) {
                        mPlacesExtractor.setPlacesList(places);
                        mRestaurantProfileList = new ArrayList<>();
                        mRestaurantProfileList = mPlacesExtractor.getRestaurantProfileList();
                        UserHelper.getAllUsers().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!Objects.equals(document.getString(USER_DATE_CHOICE), BLANK_ANSWER))
                                            if (!new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE),
                                                    document.getString(USER_DATE_CHOICE)).getCountdownResult()) {
                                                for (int i = 0; i < mRestaurantProfileList.size(); i++) {
                                                    if (Objects.equals(document.getString(USER_RESTAURANT_CHOICE), mRestaurantProfileList.get(i).getName())) {
                                                        int newNumber = mRestaurantProfileList.get(i).getNumberOfParticipant() + 1;
                                                        mRestaurantProfileList.get(i).setNumberOfParticipant(newNumber);
                                                    }}}}}
                                mMainFragment.markersCreation(mRestaurantProfileList);
                                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_LISTVIEW_FRAGMENT);
                                if (fragment != null)
                                    getFragmentManager().beginTransaction()
                                            .replace(getFragmentLayout(), ListViewFragment.newInstanceWithList(mRestaurantProfileList), TAG_LISTVIEW_FRAGMENT).commit();
                            }
                        });
                    }
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }

    // Observable with NearbySearch api for places
    public void executePlacesWithExtractor(final String coordinates){
        mCoordinates = coordinates;
        this.mDisposable = GoogleStreams.streamListPlaces(coordinates,PARAMETER_PLACE_API_RADIUS, PARAMETER_PLACE_API_TYPE, PARAMETER_PLACE_API_KEY)
                .subscribeWith(new DisposableObserver<List<Places>>() {
                    @Override
                    public void onNext(List<Places> places) {
                        mPlacesExtractor.setPlacesList(places);
                        mRestaurantProfileList = new ArrayList<>();
                        mRestaurantProfileList = mPlacesExtractor.getRestaurantProfileList();
                        UserHelper.getAllUsers().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!Objects.equals(document.getString(USER_DATE_CHOICE), BLANK_ANSWER))
                                            if (!new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE),
                                                    document.getString(USER_DATE_CHOICE)).getCountdownResult()) {
                                            for (int i = 0; i < mRestaurantProfileList.size(); i++) {
                                                if (Objects.equals(document.getString(USER_RESTAURANT_CHOICE), mRestaurantProfileList.get(i).getName())) {
                                                    int newNumber = mRestaurantProfileList.get(i).getNumberOfParticipant() + 1;
                                                    mRestaurantProfileList.get(i).setNumberOfParticipant(newNumber);
                                                    }}}}}
                                mMainFragment.markersCreation(mRestaurantProfileList);
                            }
                        });
                    }
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
        if (this.mDisposable2 != null && !this.mDisposable2.isDisposed()) this.mDisposable2.dispose();
    }
}