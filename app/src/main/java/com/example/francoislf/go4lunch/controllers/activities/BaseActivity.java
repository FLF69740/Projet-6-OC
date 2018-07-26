package com.example.francoislf.go4lunch.controllers.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.example.francoislf.go4lunch.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getContentView());
        configureFragment(savedInstanceState);
        configureToolbar();
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

    //Configure toolbar
    protected void configureToolbar(){
        Toolbar toolbar = findViewById(getToolbarView());
        toolbar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);
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
}
