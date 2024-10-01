/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cilguru.ui.web;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import com.cilguru.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class FullscreenDemoActivity extends YouTubeFailureRecoveryActivity implements
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener,
    YouTubePlayer.OnFullscreenListener {

  private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
      ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
      : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

  private LinearLayout baseLayout;
  private YouTubePlayerView playerView;
  private YouTubePlayer player;
  private Button fullscreenButton;
  private CompoundButton checkbox;
  private View otherViews;
  String url2=null;
  private boolean fullscreen;
  ImageView rlOverlay,rlOverlay2,rlOverlay3;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);;
    setContentView(R.layout.fullscreen_demo);

    baseLayout = (LinearLayout) findViewById(R.id.layout);
    playerView = (YouTubePlayerView) findViewById(R.id.player);
    fullscreenButton = (Button) findViewById(R.id.fullscreen_button);
    checkbox = (CompoundButton) findViewById(R.id.landscape_fullscreen_checkbox);
    otherViews = findViewById(R.id.other_views);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);


    rlOverlay = findViewById(R.id.rlOverlay);
    rlOverlay2=findViewById(R.id.rlOverlay2);
    rlOverlay3=findViewById(R.id.rlOverlay3);



    rlOverlay.setEnabled(true);
    rlOverlay2.setEnabled(true);
    rlOverlay3.setEnabled(true);

    rlOverlay.setClickable(true);
    rlOverlay2.setClickable(true);
    rlOverlay3.setClickable(true);

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
    rlOverlay2.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event);
        return true;
      }
    });

    rlOverlay2.setOnHoverListener(new View.OnHoverListener() {
      @Override
      public boolean onHover(View v, MotionEvent event) {
        v.onHoverEvent(event);
        return true;
      }
    });

    rlOverlay3.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event);
        return true;
      }
    });

    rlOverlay3.setOnHoverListener(new View.OnHoverListener() {
      @Override
      public boolean onHover(View v, MotionEvent event) {
        v.onHoverEvent(event);
        return true;
      }
    });



    checkbox.setOnCheckedChangeListener(this);
    // You can use your own button to switch to fullscreen too
    fullscreenButton.setOnClickListener(this);
    DatabaseReference getUserDatabaseReference;
    getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Keys");
    getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // retrieve data from db
        String name = dataSnapshot.child("ytlink").getValue().toString();
        //   display_status.setText(status);

        url2 = name;
        playerView.initialize(url2, FullscreenDemoActivity.this);
      }
      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });


    doLayout();
  }

  @Override
  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
      boolean wasRestored) {
    this.player = player;
    setControlsEnabled();
    // Specify that we want to handle fullscreen behavior ourselves.
    player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
    player.setOnFullscreenListener(this);
    if (!wasRestored) {
      Intent i=this.getIntent();
      //  String nm=i.getExtras().getString("NAME_KEY");
      String tv=i.getExtras().getString("URL");

      Log.d("urrrrl",tv+"\n");
      player.cueVideo(tv);
    }
  }

  @Override
  protected YouTubePlayer.Provider getYouTubePlayerProvider() {
    return playerView;
  }

  @Override
  public void onClick(View v) {
    player.setFullscreen(!fullscreen);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int controlFlags = player.getFullscreenControlFlags();
    if (isChecked) {

      setRequestedOrientation(PORTRAIT_ORIENTATION);
      controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
      controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
    }
    player.setFullscreenControlFlags(controlFlags);
  }

  private void doLayout() {
    LayoutParams playerParams =
        (LayoutParams) playerView.getLayoutParams();
    if (fullscreen) {

      playerParams.width = LayoutParams.MATCH_PARENT;
      playerParams.height = LayoutParams.MATCH_PARENT;

      otherViews.setVisibility(View.GONE);
    } else {

      otherViews.setVisibility(View.VISIBLE);
      ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
      if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        playerParams.width = otherViewsParams.width = 0;
        playerParams.height = WRAP_CONTENT;
        otherViewsParams.height = MATCH_PARENT;
        playerParams.weight = 1;
        baseLayout.setOrientation(LinearLayout.HORIZONTAL);
      } else {
        playerParams.width = otherViewsParams.width = MATCH_PARENT;
        playerParams.height = WRAP_CONTENT;
        playerParams.weight = 0;
        otherViewsParams.height = 0;
        baseLayout.setOrientation(LinearLayout.VERTICAL);
      }
      setControlsEnabled();
    }
  }

  private void setControlsEnabled() {
    checkbox.setEnabled(player != null
        && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    fullscreenButton.setEnabled(player != null);
  }

  @Override
  public void onFullscreen(boolean isFullscreen) {
    fullscreen = isFullscreen;
    doLayout();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    doLayout();
  }

}
