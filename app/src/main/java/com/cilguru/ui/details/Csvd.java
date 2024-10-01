package com.cilguru.ui.details;

//public class Csvd {
//}

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.cilguru.R;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import com.cilguru.adapter.RclrTv;
import com.cilguru.adapter.RclrTv;
import com.cilguru.model.ShopData;
import com.cilguru.model.YtData;
import com.cilguru.ui.MainActivity;
import com.cilguru.ui.loginreg.Login;
import com.cilguru.ui.web.VideoPlayer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Csvd extends AppCompatActivity implements RclrTv.OnItemClickListener, View.OnClickListener {
    public static DatabaseReference mDatabaseRef2;
    private SwipeRefreshLayout swpr;
    private RecyclerView mRecyclerView;
    private RclrTv mAdapter;
    private ProgressBar mProgressBar;
    //  private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<YtData> mTvdt;
    String mnn = "";
    private String tag = "Demo";
    private ShimmerFrameLayout mShimmer;
    TextView dmv, blk, adl, kpv, mn,ttltv;
    ImageView menubar;
    public String tagg = null;
    private String oldestPostId;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    TextView nametv,ftrtv,pricetv,desctv,jtv,buynow;
    ImageView imgtv;
    //Firebase
    private List<ShopData> fShop;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_course_video);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

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

        mShimmer = findViewById(R.id.shimmerFrameLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        //  swpr=findViewById(R.id.swpr);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);

        mTvdt = new ArrayList<>();
        mAdapter = new RclrTv(this, mTvdt);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(Csvd.this);

        mAdapter = new RclrTv(this, mTvdt);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mRecyclerView.setLayoutManager(new LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,false));

        //  mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Demo");
        ttltv= findViewById(R.id.ttltv);


        nametv=findViewById(R.id.nameTextView);
        ftrtv=findViewById(R.id.ftrtv);
        pricetv=findViewById(R.id.pricetv);
        jtv=findViewById(R.id.jtv);
        desctv=findViewById(R.id.descriptionTextView);
        imgtv=findViewById(R.id.imgfetch);
        buynow=findViewById(R.id.buynow);


        Intent i=getIntent();
        name=i.getExtras().getString("Pkey");

//        Log.d("NAMEEE",name);
//            if (name!= null && name.equals("DMV")) {
//                mnn = name;
//            } else if (name!= null && name.equals("KPV") ) {
//                mnn = name;
//            } else if (name!= null && name.equals("ALK") ) {
//                mnn = name;
//            } else if (name!= null && name.equals("BLK")) {
//                mnn = name;
//            } else if (name!= null && name.equals("RC")) {
//                mnn = name;
//            } else if (name!= null && name.equals("MN")) {
//                mnn = name;
//            }
        Log.d("NAMEEE2", mnn);
        tagg=name;

        String value = tagg;
        Log.d("loadddd",value);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();


        ttltv.setText(mnn);
        // loaddt();

        loaddata();
        loadvdo();

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Buy").child(tagg);
        Log.d("mdtrf",mDatabaseRef2.toString());
        buynow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
//              //  mShimmer.setVisibility(View.GONE);
//                final Dialog dialog = new Dialog(Csvd.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.setContentView(R.layout.sorry);
//                TextView cross=(TextView) dialog.findViewById(R.id.btnn);
//                TextView txtmsg=(TextView)dialog.findViewById(R.id.txtmsg);
//                txtmsg.setText("Sorry Unavailable☹️!!!\n" +
//                        "This course will be available for purchase from 2021.\nTill then contact 9711233954 \n");
//
////                cross.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(Csvd.this, SendActivity.class);
////                        startActivity(intent);
////                    }
////                });
//
//                dialog.show();
                loadpay();
            }


        });



    }



    private void loaddata() {
        Intent i=getIntent();
        name=i.getExtras().getString("Pkey");
        Log.d("Specific Node Value" , "\n"+name);

        Query mdb = FirebaseDatabase.getInstance().getReference("Shop").orderByChild("pName").equalTo(name);
        Log.d("Specific Node Value" , "\n"+name+"\n"+mDatabaseRef.toString());
        mdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //mTvdt.clear();
                //Get the node from the datasnapshot
                String myParentNode = dataSnapshot.getKey();

                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                    //   map.put(key,value);
                }

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Shop").child(myParentNode);
                Log.d("Specific" , mDatabaseRef.toString());
                mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // mTvdt.clear();
                        for (DataSnapshot moviesnapshot : dataSnapshot.getChildren()) {
                            String prname,price,ftr,img,desc,join;
                            prname = dataSnapshot.child("pName").getValue().toString();
                            price = dataSnapshot.child("pprice").getValue().toString();
                            desc = dataSnapshot.child("pdesc").getValue().toString();
                            ftr = dataSnapshot.child("pfeature").getValue().toString();
                            img = dataSnapshot.child("pimgurl").getValue().toString();


                            nametv.setText(prname);
                            pricetv.setText(price);
                            desctv.setText(desc);
                            ftrtv.setText(ftr);
                            Picasso.get()
                                    .load(img)
                                    .placeholder(R.drawable.itemimg)
                                    //.fit()
                                    // .centerCrop()
                                    // .load(imgurll)
                                    .into(imgtv);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Csvd.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        mShimmer.setVisibility(View.INVISIBLE);
                        Toast.makeText(Csvd.this,"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                });

//                    }
//                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Csvd.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mShimmer.setVisibility(View.INVISIBLE);
                Toast.makeText(Csvd.this,"No Data Found",Toast.LENGTH_SHORT).show();
            }
        });

    }
    //  loaddt();


    private void loadpay() {
        //Pay button Ftr
        Intent i=getIntent();
        name=i.getExtras().getString("Pkey");
        Log.d("Specific Node Value" , "\n"+name);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();


        Query qalk = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Buy").child(name);
        Log.d("paybtn",qalk.toString());
        qalk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("payst")) {
                    String pst = dataSnapshot.child("payst").getValue().toString();
                    Log.d("Payt", pst);
                    if (pst!= null && pst.equals("Paid") ) {
                        //do video show
                        buynow.setVisibility(View.GONE);
                    }
                    else if (pst!= null && pst.equals("Unpaid") ){
                        Log.d("Payt","unpaid");
                        unpdmssg();
                    }
                }else{

                    mShimmer.setVisibility(View.GONE);
                    final Dialog dialog = new Dialog(Csvd.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.sorry);
                    TextView cross=(TextView) dialog.findViewById(R.id.btnn);
                    TextView txtmsg=(TextView)dialog.findViewById(R.id.txtmsg);

                    TextView btnn=(TextView) dialog.findViewById(R.id.btnn);
                    btnn.setVisibility(View.GONE);
                    txtmsg.setText("Please Purchase this one.");

                    dialog.show();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        Toast.makeText(Csvd.this, mDatabaseRef.toString(), Toast.LENGTH_SHORT).show();
    }

    private void unpdmssg() {

        mShimmer.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(Csvd.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.sorry);
        ImageView cimg=(ImageView) dialog.findViewById(R.id.cimg);
        TextView cross=(TextView) dialog.findViewById(R.id.btnn);
        TextView txtmsg=(TextView)dialog.findViewById(R.id.txtmsg);
        cimg.setImageResource(R.drawable.payt);
        TextView btnn=(TextView) dialog.findViewById(R.id.btnn);
        btnn.setVisibility(View.GONE);
        txtmsg.setText("Please Use given details to pay\nif already paid then ignore it.");
        txtmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Csvd.this, MainActivity.class);
                startActivity(intent);
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Csvd.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    public void loadvdo() {
        // Load videos

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag).child(tagg);
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   mTvdt.clear();
                for (DataSnapshot moviesnapshot : dataSnapshot.getChildren()) {
                    YtData upload = moviesnapshot.getValue(YtData.class);
                    upload.setKey(moviesnapshot.getKey());
                    mTvdt.add(upload);
                    Log.d("Tagg",mDatabaseRef.toString());
                }
                mAdapter.notifyDataSetChanged();
                mShimmer.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                Log.d("Tagg",tag);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Csvd.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mShimmer.setVisibility(View.INVISIBLE);
                Toast.makeText(Csvd.this,"No Data Found",Toast.LENGTH_SHORT).show();
            }
        });

    }


//   class getpd() {
//        int vall=0;
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        mAuth = FirebaseAuth.getInstance();
//        String USER_ID=mAuth.getCurrentUser().getUid();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(USER_ID).child("Buy").child(tagg);
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String cch = dataSnapshot.child("payst").getValue().toString();
//                if (!cch.equals(null)||cch.equals("Unpaid")){
//                    vall=3;
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        return vall;
//    }
//    }

    private void openDetail(String[] data) {
        Intent intent = new Intent(Csvd.this, ShopDetail.class);
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


    public void onItemClick(int position) {
        YtData clickMovie = mTvdt.get(position);//,clickMovie.getCate(),clickMovie.getLang()
        String movieData = clickMovie.getWlink().toString();
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("URL", movieData);
        startActivity(intent);
        //    Toast.makeText(this, movieData.toString(), Toast.LENGTH_LONG).show();
        Log.d("ITEMSs", movieData.toString());


        // openDetailActivity(movieData);
    }

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("NAME_KEY", data[0]);
        intent.putExtra("IMG ", data[1]);
        intent.putExtra("WLINK", data[2]);
        startActivity(intent);
    }


    public void onDestroy() {
        super.onDestroy();
        // mDatabaseRef.removeEventListener(mDBListener);
        Intent intent =new Intent(Csvd.this,MainActivity.class);
    }


    @Override
    public void onClick(View v) {


    }


    private void logOutUser() {

        Intent loginIntent =  new Intent(this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
///        logOutUser();
        startActivity(loginIntent);
        finish();
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
}