package com.cilguru.ui.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cilguru.adapter.RecyclerAdatpterShop;
import com.cilguru.adapter.RecyleItem;
import com.cilguru.model.Req;
import com.cilguru.model.ShopData;
import com.cilguru.ui.fragments.ShopFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import xyz.hasnat.sweettoast.SweetToast;
import com.cilguru.R;
import com.cilguru.ui.MainActivity;
import com.cilguru.ui.loginreg.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPurchase extends AppCompatActivity  {

private SwipeRefreshLayout swpr;
private RecyclerView mRecyclerView;
private DatabaseReference mDatabaseRef;
private ValueEventListener mDBListener;
private List<Req> fReq;
private String tag = "Buy";
private RecyleItem mAdapter;
private ProgressBar mProgressBar;
        EditText search;
        ImageView menubar;
public String tagg = null;
private String oldestPostId;
private ShimmerFrameLayout mShimmer;
FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    //Firebase
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;

    private CircleImageView profile_settings_image;
    private TextView display_status, updatedMsg, recheckGender;
    private ImageView profile_img, editStatusBtn;
    private TextView display_name, display_email, user_phone, user_profession, user_nickname,logout;
    private RadioButton maleRB, femaleRB;

    private Button saveInfoBtn,purchase;

    private DatabaseReference getUserDatabaseReference;


    private final static int GALLERY_PICK_CODE = 1;
    Bitmap thumb_Bitmap = null;

    private ProgressDialog progressDialog;
    private String selectedGender = "", profile_download_url, profile_thumb_download_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_my_purchase);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            logOutUser(); // Return to Login activity
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }



        mRecyclerView = findViewById(R.id.recyclerView);

        fReq = new ArrayList<>();
        mAdapter = new RecyleItem(this, fReq);

        mRecyclerView.setAdapter(mAdapter);
       // mAdapter.setOnItemClickListener(MyPurchase.this);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyPurchase.this));
        mShimmer=findViewById(R.id.shimmerFrameLayout);
//        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);
        loaddt();

    }


    public void onItemClick(int position) {
        Req clickFile = fReq.get(position);//,clickMovie.getCate(),clickMovie.getLang()
        String[] fData = {clickFile.getPname(), clickFile.getPprice(), clickFile.getPimgurl(),
                clickFile.getPayst(), clickFile.getUemail()};
        //    Toast.makeText(this, movieData.toString(), Toast.LENGTH_LONG).show();
        Log.d("ITEMSs", fData.toString());

        openDetailActivity(fData);
    }

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(this, ShopDetail.class);
        intent.putExtra("PName", data[0]);
        intent.putExtra("PDesc", data[1]);
        intent.putExtra("PImg", data[2]);
        intent.putExtra("PPrice", data[3]);
        intent.putExtra("PFtr", data[4]);
        intent.putExtra("PJion", data[5]);
        // intent.putExtra("DATE", data[3]);
        startActivity(intent);
        Log.d("fileintent","done");
    }




    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    public void loaddt() {
        String value = tagg;

//        Query q = FirebaseDatabase.getInstance().getReference()
//                .child("Movies")
//                .orderByChild("signName")
//                .equalTo(value);

        if (TextUtils.isEmpty(value)) {
            mAuth = FirebaseAuth.getInstance();
            String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child(tag);
            Log.d("pathss",mDatabaseRef.toString());
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    fReq.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        Req upload = noticenapshot.getValue(Req.class);
                        upload.setKey(noticenapshot.getKey());
                        fReq.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mShimmer.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MyPurchase.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
            // mDatabaseRef = FirebaseDatabase.getInstance().getReference(String.valueOf(q));
        } else {
            //  mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag);

            //mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag).child("cate");
            Query mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child(tag)
                    .orderByChild("pName")
                    .equalTo(tagg);
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    fReq.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        Req upload = noticenapshot.getValue(Req.class);
                        upload.setKey(noticenapshot.getKey());
                        fReq.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mShimmer.setVisibility(View.GONE);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MyPurchase.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
         //   Toast.makeText(MyPurchase.this, mDatabaseRef.toString(), Toast.LENGTH_LONG).show();
        }
    }


    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(MyPurchase.this, data, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmer.stopShimmerAnimation();
        super.onPause();
    }




    private void logOutUser() {
        Intent loginIntent =  new Intent(this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
///        logOutUser();
        startActivity(loginIntent);
        finish();
    }


}