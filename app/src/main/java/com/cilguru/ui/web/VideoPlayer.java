package com.cilguru.ui.web;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cilguru.R;

import im.delight.android.webview.AdvancedWebView;


public class VideoPlayer extends AppCompatActivity implements AdvancedWebView.Listener {
    AdvancedWebView webview;
    private static final String TEST_PAGE_URL = null;
    String tv= null;
    String dl= null;
    String tm=null;
    private WebView mWebView;

//
ImageView rlOverlay;
    private ProgressBar mProgressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_web);

        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        rlOverlay = findViewById(R.id.rlOverlay);
        rlOverlay.setEnabled(true);
        rlOverlay.setClickable(true);
        rlOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                return true;
            }
        });

        rlOverlay.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                v.onHoverEvent(event);
                return true;
            }
        });

            Intent i=this.getIntent();
          //  String nm=i.getExtras().getString("NAME_KEY");
            tv=i.getExtras().getString("URL");

            Log.d("urrrrl",tv+"\n");

        rlOverlay.setVisibility(View.VISIBLE);
        webview =findViewById(R.id.webview);
        webview.setListener(this, this);
        webview.setMixedContentAllowed(false);
        int clength=tv.length();
        webview.loadUrl("https://drive.google.com/file/d/"+tv+"/preview");
//        webview.loadUrl("https://drive.google.com/file/d/1S5MFRKNjlwp6ERnQHn3F4Ez2LdGehN0d/preview");
        }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webview.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webview.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webview.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!webview.onBackPressed()) { return; }
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    }
