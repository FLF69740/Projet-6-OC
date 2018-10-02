package com.example.francoislf.go4lunch.controllers.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.controllers.fragments.WebViewFragment;

public class WebViewActivity extends AppCompatActivity {

    private WebViewFragment mWebViewFragment;
    public static final String LINK_WEBVIEW = "LINK_WEBVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        this.configureFragment();
    }

    // Configure SearchResultFragment into this Activity
    protected void configureFragment() {
        mWebViewFragment = (WebViewFragment) getFragmentManager().findFragmentById(R.id.frame_layout_webview);
        if (mWebViewFragment == null){
            mWebViewFragment = new WebViewFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_webview, mWebViewFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = getIntent().getStringExtra(LINK_WEBVIEW);
        mWebViewFragment.configureUrlWebView(url);
    }
}
