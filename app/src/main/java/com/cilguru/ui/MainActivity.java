package com.cilguru.ui;

import android.Manifest;
import android.app.Activity;

import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.cilguru.R;
import com.cilguru.model.ProfileInfo;
import com.cilguru.ui.fragments.VideoFragment;
import com.cilguru.ui.fragments.ShopFragment;
import com.cilguru.ui.fragments.HomeFragment;
import com.cilguru.ui.fragments.GalleryFragment;
import com.cilguru.ui.loginreg.Login;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    //Firebase
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    //Firebase

    private DatabaseReference storeDefaultDatabaseReference;

    //@SuppressLint("SetTextI18n")
    public static final String TAG="LOGIN";
    private final static int ID_HOME = 1;
    private final static int ID_SHOP = 2;
    private final static int ID_YTVDO = 3;
    private final static int ID_GALLERY = 4;
    private final static int ID_ACCOUNT = 5;
    MeowBottomNavigation navView;

    private static final String AUTH_KEY = "key=AAAAvfRU7Qk:APA91bHW6nJMmbVWT48RFwV81WnTrot6LAvUzogrLNp3aUgMHOx-x9pW32EqHQ65NBJCP1ySEXtlSRNCJAyjTD8_3nax9d5ccY96bxvVaRPDy2c2fAFmIyPiXluQkBPFqOpJ0g8zxbT5";
    private TextView mTextView;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_main);
        // Call the function callInstamojo to start payment here

        navView=findViewById(R.id.nav_view);
        navView.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home_black_24dp));
        navView.add(new MeowBottomNavigation.Model(ID_YTVDO, R.drawable.ytvdeo));
        navView.add(new MeowBottomNavigation.Model(ID_SHOP, R.drawable.shop));
        navView.add(new MeowBottomNavigation.Model(ID_GALLERY, R.drawable.gallery));
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null||mAuth.getCurrentUser().getUid()==null){
            logOutUser(); // Return to Login activity
        }
        String user_uID = mAuth.getCurrentUser().getUid();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+user_uID);

        storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_uID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }


        navView.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        navView.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment selectedFragment = null;
                String name;
                switch (item.getId()) {
                    case ID_HOME:
                        HomeFragment fragmenthomee = new HomeFragment();
                        FragmentTransaction transactionhm = getSupportFragmentManager().beginTransaction();
                        transactionhm.replace(R.id.nav_host_fragment, fragmenthomee);
                        transactionhm.commit();
                        name = "HOME";
                        break;
                    case ID_YTVDO:
                        name = "EXPLORE";
                        VideoFragment fragmentv = new VideoFragment();
                        FragmentTransaction transactionv = getSupportFragmentManager().beginTransaction();
                        transactionv.replace(R.id.nav_host_fragment, fragmentv);
                        transactionv.commit();
                        break;
                    case ID_SHOP:
                        ShopFragment fragmentd = new ShopFragment();
                        FragmentTransaction transactiond = getSupportFragmentManager().beginTransaction();
                        transactiond.replace(R.id.nav_host_fragment, fragmentd);
                        transactiond.commit();

                        break;
                    case ID_GALLERY:
                        GalleryFragment fragmentn = new GalleryFragment();
                        FragmentTransaction transactionn = getSupportFragmentManager().beginTransaction();
                        transactionn.replace(R.id.nav_host_fragment, fragmentn);
                        transactionn.commit();
                        break;
                    default:
                        name = "";
                }
            }
        });

        navView.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });


        navView.show(ID_HOME,true);
        if(isWorkingInternetPersent()){

        }
        else{
            showAlertDialog(MainActivity.this, "Internet Connection",
                    "You don't have internet connection", false);
        }
        navView = findViewById(R.id.nav_view);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    token = task.getException().getMessage();
                    Log.w("FCM TOKEN Failed", task.getException());
                } else {
                    token = task.getResult().getToken();
                    Log.i("FCM TOKEN", token);
                }
            }
        });
    }

    private void splash() {

    }

    private void logOutUser() {

        Intent loginIntent =  new Intent(this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    public boolean isWorkingInternetPersent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
    public void showAlertDialog(MainActivity context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

         alertDialog.setIcon((status) ? R.drawable.cillogo : R.drawable.cillogo);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });

        alertDialog.show();
    }

}

