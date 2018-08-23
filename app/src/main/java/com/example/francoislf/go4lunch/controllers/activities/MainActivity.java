package com.example.francoislf.go4lunch.controllers.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.MainFragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar mToolbar;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Override
    protected int getContentView() {return R.layout.activity_main;}

    @Override
    protected Fragment newInstance() {return new MainFragment();}

    @Override
    protected int getFragmentLayout() {return (R.id.frame_layout_main);}

    @Override
    protected boolean getContentViewBoolean() {return true;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.updateProfileInformations();
    }

    // Update Nave_header informations about user informations
    protected void updateProfileInformations() {
        if (this.getCurrentUser() != null){
            if (this.getCurrentUser().getPhotoUrl() != null){
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfile);
            }

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
                    case R.id.ic_onglet_map_view:
                        Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_1));
                        break;
                    case R.id.ic_onglet_list_view: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_2)); break;
                    case R.id.ic_onglet_workmates: Log.i(getString(R.string.Log_i),getString(R.string.bottom_item_3)); break;
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
            case R.id.your_lunch :
                break;
            case R.id.settings :
                break;
            case  R.id.logout : signOutFormFirebase(); break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



}
