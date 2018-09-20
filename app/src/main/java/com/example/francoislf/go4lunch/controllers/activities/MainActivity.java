package com.example.francoislf.go4lunch.controllers.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnClickedResultMarker, ListViewFragment.OnClickedResultItem, WorkmatesFragment.OnClickedAvatarItem {

    private Toolbar mToolbar;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    private ArrayList<RestaurantProfile> mRestaurantProfileList;
    PlacesExtractor mPlacesExtractor;
    private Disposable mDisposable, mDisposable2;
    private static final String PARAMETER_PLACE_API_RADIUS = "1000";
    private static final String PARAMETER_PLACE_API_TYPE = "restaurant";
    private String PARAMETER_PLACE_API_KEY;
    private MainFragment mMainFragment;

    @Override
    protected int getContentView() {return R.layout.activity_main;}
    @Override
    protected Fragment newInstance() {mMainFragment = new MainFragment(); return mMainFragment;}
    @Override
    protected int getFragmentLayout() {return (R.id.frame_layout_main);}
    @Override
    protected boolean getContentViewBoolean() {return true;}
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        PARAMETER_PLACE_API_KEY = getString(R.string.google_place_api_key);
        mRestaurantProfileList = new ArrayList<>();
        mPlacesExtractor = new PlacesExtractor(this);
        configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.updateProfileInformations();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
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
                        getFragmentManager().beginTransaction().replace(getFragmentLayout(), newInstance()).commit();
                        configureToolbarTitle(1);
                    break;
                    case R.id.ic_onglet_list_view: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_2));
                        getFragmentManager().beginTransaction().replace(getFragmentLayout(), new ListViewFragment().newInstanceWithList(mRestaurantProfileList)).commit();
                        configureToolbarTitle(2);
                    break;
                    case R.id.ic_onglet_workmates: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_3));
                        getFragmentManager().beginTransaction().replace(getFragmentLayout(), new WorkmatesFragment().newInstanceWorkmates()).commit();
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
            case R.id.your_lunch : break;
            case R.id.settings : break;
            case R.id.logout : signOutFormFirebase(); break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
        if (!title.equals("Empty")){
            Intent intent = new Intent(this, FileRestaurantActivity.class);
            RestaurantProfile restaurantProfile = mPlacesExtractor.getRestaurantProfile(title);
            intent.putExtra(FileRestaurantActivity.EXTRA_SNIPPET_MARKER, restaurantProfile);
            startActivity(intent);
        }
    }

    /**
     *  HTTP (RxJAVA)
     */

    public void executePlacesWithExtractor(View view, final String coordinates){
        this.mDisposable = GoogleStreams.streamListPlaces(coordinates,PARAMETER_PLACE_API_RADIUS, PARAMETER_PLACE_API_TYPE, PARAMETER_PLACE_API_KEY)
                .subscribeWith(new DisposableObserver<List<Places>>() {
                                   @Override
                                   public void onNext(List<Places> places) {
                                       mPlacesExtractor.setPlacesList(places);
                                       mRestaurantProfileList = mPlacesExtractor.getRestaurantProfileList();
                                       UserHelper.getAllUsers().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       if (!document.getString("dateChoice").equals("Empty"))
                                                           if (!new ChoiceRestaurantCountdown(document.getString("hourChoice"),
                                                                       document.getString("dateChoice")).getCountdownResult()) {
                                                           for (int i = 0; i < mRestaurantProfileList.size(); i++) {
                                                               if (document.getString("restaurantChoice").equals(mRestaurantProfileList.get(i).getName())) {
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
                                   public void onComplete() {executePhotoWithExtractor(coordinates);}
                               });
    }

    private void executePhotoWithExtractor(String coordinates){
        this.mDisposable2 = GoogleStreams.streamListPhotos(coordinates,PARAMETER_PLACE_API_RADIUS, PARAMETER_PLACE_API_TYPE, PARAMETER_PLACE_API_KEY)
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        List<String> tempArrayList = new ArrayList<>(mPlacesExtractor.organisePhotoAndProfile(mRestaurantProfileList, strings));
                        for (int i = 0 ; i < tempArrayList.size() ; i++) mRestaurantProfileList.get(i).setPhoto(tempArrayList.get(i));
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