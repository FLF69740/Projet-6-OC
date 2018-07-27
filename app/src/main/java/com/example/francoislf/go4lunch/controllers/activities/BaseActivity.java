package com.example.francoislf.go4lunch.controllers.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.francoislf.go4lunch.R;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getContentView());
        configureFragment(savedInstanceState);
        configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }

    // Load the view
    protected abstract int getContentView();

    // Link the fragment to the View
    protected void configureFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(this.getFragmentLayout(), newInstance())
                    .commit();
        }
    }

    // Load the fragment
    protected abstract Fragment newInstance();

    // Load the layout id
    protected abstract int getFragmentLayout();

    /**
     * Toolbar
     */

    //Configure toolbar
    protected void configureToolbar(){
        this.mToolbar = findViewById(getToolbarView());
        mToolbar.setTitle(getToolbarTitle());
        setSupportActionBar(mToolbar);
    }

    // Load Toolbar View
    protected abstract int getToolbarView();

    // Load Toolbar Title
    protected abstract int getToolbarTitle();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Navigation drawer
     */

    protected void configureDrawerLayout(){
        this.mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected void configureNavigationView(){
        this.mNavigationView = findViewById(R.id.activity_main_nav_view);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
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
            case  R.id.logout :
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
