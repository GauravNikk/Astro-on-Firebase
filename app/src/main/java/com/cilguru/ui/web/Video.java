package com.cilguru.ui.web;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cilguru.R;

import im.delight.android.webview.AdvancedWebView;

public class Video extends AppCompatActivity implements AdvancedWebView.Listener {
   AdvancedWebView webview;
   String videoPath="https://drive.google.com/file/d/1WMmNdwdU-UsFiWHMXM9kB47EDvkAj2St/preview";
  // String id="/uc?authuser=0&"+videoPath+
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        webview =findViewById(R.id.webview);
        webview.setListener(this, this);
        webview.setMixedContentAllowed(false);
        webview.loadUrl(videoPath);


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