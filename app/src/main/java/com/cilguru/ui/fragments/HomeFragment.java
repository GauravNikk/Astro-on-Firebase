package com.cilguru.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.cilguru.R;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cilguru.adapter.CstmSc;
import com.cilguru.adapter.RecyclerHome;
import com.cilguru.model.GalleryData;
import com.cilguru.ui.ProfileSetting.SettingAcivity;
import com.cilguru.ui.details.CourseVideo;
import com.cilguru.ui.details.Csvd;
import com.cilguru.ui.details.ShopDetail;
import com.cilguru.ui.details.ShopDetail2;
import com.cilguru.ui.loginreg.Login;
import com.cilguru.ui.other.Nikkimg;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iriis.libzoomableimageview.ZoomableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class HomeFragment extends Fragment implements RecyclerHome.OnItemClickListener, View.OnClickListener {
    private SwipeRefreshLayout swpr;
    private RecyclerView mRecyclerView;
    private RecyclerHome mAdapter;
    private ProgressBar mProgressBar;
    //  private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<GalleryData> fShop;
    private String tag = "Banner";
    EditText search;
    ImageView menubar,profile_image;
   // private List<ShopData> jshop;

    public String tagg = null;
    private String oldestPostId;
    private ShimmerFrameLayout mShimmer;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    Button mnb,kpvb,blkb,alkb,rcb;
    TextView ltv,ftrtv;
    final Handler mHandler = new Handler(Looper.getMainLooper());
    final int duration = 2;
    String lang;
    final int pixelsToMove = 300;
    ImageView w1,w2,w3,w4,w5,w6,w7,w8,cert1,cert2,cert3,cert4;
    String img;
    ImageView cimg,certi2,certi1,certi3,certi4,certi5,certi6,certi7,certi8,certi9,certi10;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
//        img= currentUser.getPhotoUrl().toString();
        if (currentUser == null){
            logOutUser(); // Return to Login activity
        }
        mRecyclerView = root.findViewById(R.id.recyclerView);
        cimg=root.findViewById(R.id.cimg);
        profile_image=root.findViewById(R.id.profile_image);
        mnb= root.findViewById(R.id.button2);
        kpvb= root.findViewById(R.id.button4);
        blkb= root.findViewById(R.id.button);
        alkb= root.findViewById(R.id.button1);
        rcb= root.findViewById(R.id.button3);
        ltv=root.findViewById(R.id.ltv);
        ftrtv=root.findViewById(R.id.ftrtv);


        certi1=root.findViewById(R.id.certi1);
        certi2=root.findViewById(R.id.certi2);
        certi3=root.findViewById(R.id.certi3);
        certi4=root.findViewById(R.id.certi4);
        certi5=root.findViewById(R.id.certi5);
        certi6=root.findViewById(R.id.certi6);
        certi7=root.findViewById(R.id.certi7);
        certi8=root.findViewById(R.id.certi8);
        certi9=root.findViewById(R.id.certi9);

        cert1=root.findViewById(R.id.crt1);
        cert2=root.findViewById(R.id.crt2);
        cert3=root.findViewById(R.id.crt3);
        cert4=root.findViewById(R.id.crt4);

        w1= root.findViewById(R.id.w1);
        w2= root.findViewById(R.id.w2);
        w3= root.findViewById(R.id.w3);
        w4= root.findViewById(R.id.w4);
        w5= root.findViewById(R.id.w5);
        w6= root.findViewById(R.id.w6);
        w7= root.findViewById(R.id.w7);
        w8= root.findViewById(R.id.w8);

        w1.setOnTouchListener(new View.OnTouchListener(){
            @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Mumbai");
               // cimg.setImageResource(R.drawable.mumbai);
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        w2.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;

                ltv.setText("Hyderabad");
               /// cimg.setImageResource(R.id.bnnrimg);
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        w3.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Bengaluru");
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        w4.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Ahmedabad");
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        w5.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Chandigarh");
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        w6.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Delhi");
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        w7.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Kochi");
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });

        w8.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                float lastXAxis = x;
                float lastYAxis = y;
                // E1.setText(Float.toString(lastXAxis));
                //  E2.setText(Float.toString(lastYAxis));
//                ltv.setScaleX(lastXAxis);
//                ltv.setScaleY(lastYAxis);
                ltv.setText("Surat");
                ltv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener()
            {
             @Override
               public void onClick(View v) {
                 Intent intent = new Intent(getContext(), SettingAcivity.class);
                 startActivity(intent);
             }
        });
        ftrtv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String url = "http://anthemitsol.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        mnb.setOnClickListener(this);
        kpvb.setOnClickListener(this);
        blkb.setOnClickListener(this);
        alkb.setOnClickListener(this);
        rcb.setOnClickListener(this);

        cert1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_cert);
//               // TextView cross=(TextView) dialog.findViewById(R.id.cross);
//                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
//              //  cross.setText("You are the one who keeps me sane, who inspires me to get out of bed every morning. If it weren't for you. I don't know where or who I'd be. You are the reason I am still alive. If it weren't for you. I wouldn't be here or be the person I am today. You are my inspiration and I am truly grateful.");
//                img.setImageResource(R.drawable.crt1);
                Nikkimg nikkimg = (Nikkimg) dialog.findViewById(R.id.cimg);
                nikkimg.setImageResource(R.drawable.crt1);
                dialog.show();
            }
        });
        cert2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_cert);
//                // TextView cross=(TextView) dialog.findViewById(R.id.cross);
//                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
//                //  cross.setText("You are the one who keeps me sane, who inspires me to get out of bed every morning. If it weren't for you. I don't know where or who I'd be. You are the reason I am still alive. If it weren't for you. I wouldn't be here or be the person I am today. You are my inspiration and I am truly grateful.");
//                img.setImageResource(R.drawable.crt2);
                Nikkimg nikkimg = (Nikkimg) dialog.findViewById(R.id.cimg);
                nikkimg.setImageResource(R.drawable.crt2);
                dialog.show();
            }
        });
        cert3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_cert);
//               // TextView cross=(TextView) dialog.findViewById(R.id.cross);
//                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
//                //cross.setText("A teacher is someone who understands us, loves us, inspires us, guides us and takes care of us. Teachers understand our feelings, and help us when we have problems. Teachers show us the right path. They treat all of us equally, love us equally, like their own children. Teachers try their best to explain to us what is right and what is wrong. They help us to find our creativity. They become sad when we are sad, and they become happy when we are happy. Teachers are like our friends. They take second place in our life, after family. They are kind, friendly, responsible,\n" +
//                 //       "honest and loyal. We are lucky to have a wonderful, loving, kind, honest, helping, caring, loyal, responsible teacher like you.\n");
//                img.setImageResource(R.drawable.crt3);
                Nikkimg nikkimg = (Nikkimg) dialog.findViewById(R.id.cimg);
                nikkimg.setImageResource(R.drawable.crt3);
                dialog.show();
            }
        });
        cert4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_cert);
//                //TextView cross=(TextView) dialog.findViewById(R.id.cross);
//                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
//                //cross.setText("You are the best teacher I have ever had. Your heart is bigger than the world. You are the smartest, strongest most skillful teacher ever. You look prettiest when you smile, like a beautiful princess, and when you talk to us, you always smile! When I talk to you, I feel I am talking with my best friend. Although I make many mistakes, you try to teach me the right ways. You teach me how to be successful in my life. You let me know myself, who I am and what I can do. I don't know how to say thank you. I am always glad I have a teacher like you. I love you and will remember you forever.");
//                img.setImageResource(R.drawable.crt4);
                Nikkimg nikkimg = (Nikkimg) dialog.findViewById(R.id.cimg);
                nikkimg.setImageResource(R.drawable.crt4);
                dialog.show();
            }
        });
//        certi4.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.setContentView(R.layout.custom_dlg);
//              //  TextView cross=(TextView) dialog.findViewById(R.id.cross);
//                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
//               // cross.setText("Thank you, my teacher, for all you have done. You always are nice to me wherever I am. I am happy that you are my teacher. With your kindness you always make my day. Thank You for teaching me everything. Thank you for all those lessons.Thank You for being my teacher. You are my number 1!");
//                img.setImageResource(R.drawable.crt4);
//                dialog.show();
//            }
//        });

        certi1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("You are the one who keeps me sane, who inspires me to get out of bed every morning. If it weren't for you. I don't know where or who I'd be. You are the reason I am still alive. If it weren't for you. I wouldn't be here or be the person I am today. You are my inspiration and I am truly grateful.");
                img.setImageResource(R.drawable.archanasai);
                dialog.show();
            }
        });

        certi2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("A teacher is someone who understands us, loves us, inspires us, guides us and takes care of us. Teachers understand our feelings, and help us when we have problems. Teachers show us the right path. They treat all of us equally, love us equally, like their own children. Teachers try their best to explain to us what is right and what is wrong. They help us to find our creativity. They become sad when we are sad, and they become happy when we are happy. Teachers are like our friends. They take second place in our life, after family. They are kind, friendly, responsible,\n" +
                        "honest and loyal. We are lucky to have a wonderful, loving, kind, honest, helping, caring, loyal, responsible teacher like you.\n");
                img.setImageResource(R.drawable.ashabamola);
                dialog.show();
            }
        });
        certi3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("You are the best teacher I have ever had. Your heart is bigger than the world. You are the smartest, strongest most skillful teacher ever. You look prettiest when you smile, like a beautiful princess, and when you talk to us, you always smile! When I talk to you, I feel I am talking with my best friend. Although I make many mistakes, you try to teach me the right ways. You teach me how to be successful in my life. You let me know myself, who I am and what I can do. I don't know how to say thank you. I am always glad I have a teacher like you. I love you and will remember you forever.");
                img.setImageResource(R.drawable.bindiyabhl);
                dialog.show();
            }
        });
        certi4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("Thank you, my teacher, for all you have done. You always are nice to me wherever I am. I am happy that you are my teacher. With your kindness you always make my day. Thank You for teaching me everything. Thank you for all those lessons.Thank You for being my teacher. You are my number 1!");
                img.setImageResource(R.drawable.drswativirendraprakash);
                dialog.show();
            }
        });
        certi5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("\n" +
                        "Just by being a teacher in all these wonderful days, you have filled and touched my heart so many different ways. Every time I had problems, you helped me through it all, coming to save me every time, When I was about to fall. You are such a special teacher. No words can truly tell how greatly you are valued for the work you do so well. I really do wonder. I was blessed with inspiration, and it has always been you.\n");
                img.setImageResource(R.drawable.gaurikailash);
                dialog.show();
            }
        });
        certi6.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("A teacher is someone who understands us, loves us, inspires us, guides us and takes care of us. Teachers understand our feelings, and help us when we have problems. Teachers show us the right path. They treat all of us equally, love us equally, like their own children. Teachers try their best to explain to us what is right and what is wrong. They help us to find our creativity. They become sad when we are sad, and they become happy when we are happy.");
                img.setImageResource(R.drawable.kantilalmunot);
                dialog.show();
            }
        });
        certi7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("\n" +
                        "You are the best teacher I have ever had. Your heart is bigger than the world. You are the smartest, strongest most skillful teacher ever. You look prettiest when you smile, like a beautiful princess, and when you talk to us, you always smile! When I talk to you, I feel I am talking with my best friend. Although I make many mistakes, you try to teach me the right ways. You teach me how to be successful in my life. You let me know myself, who I am and what I can do. I don't know how to say thank you. I am always glad I have a teacher like you. I love you and will remember you forever.\n");
                img.setImageResource(R.drawable.mayadhyaneshwarmayakal);
                dialog.show();
            }
        });
        certi8.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("This is a quick note to let you know you were the absolute best teacher I’ve ever had! You motivated and inspired me to do my best and your answers to my questions were always clear as day. Thank you so much!!!");
                img.setImageResource(R.drawable.shambhunathvaid);
                dialog.show();
            }
        });
        certi9.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dlg);
                TextView cross=(TextView) dialog.findViewById(R.id.cross);
                ImageView img=(ImageView) dialog.findViewById(R.id.cimg);
                cross.setText("\n" +
                        "Just by being a teacher in all these wonderful days, you have filled and touched my heart so many different ways. Every time I had problems, you helped me through it all, coming to save me every time, When I was about to fall. You are such a special teacher. No words can truly tell how greatly you are valued for the work you do so well. I really do wonder. I was blessed with inspiration, and it has always been you.\n");
                img.setImageResource(R.drawable.urvarshijain);
                dialog.show();
            }
        });


//        Picasso.with(getContext())
//                .load(currentUser.getPhotoUrl().toString())
//                .placeholder(R.drawable.person)
//                .fit()
//                //.centerCrop()
//                // .load(imgurll)
//                .into(profile_image);
      //  Log.d("ProfImg",img);
        fShop = new ArrayList<>();
        Log.d("rvs",String.valueOf(fShop.size()));
        mAdapter = new RecyclerHome(getContext(), fShop);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(HomeFragment.this);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (getContext(),LinearLayoutManager.HORIZONTAL,false));


//        CstmSc cmsc =
//                new CstmSc(getContext(), LinearLayoutManager.HORIZONTAL, false);
     //   mRecyclerView.setLayoutManager(cmsc);
     //   mRecyclerView.setAdapter(adapter);
        // Scroll to the position we want to snap to
      //  cmsc.scrollToPosition(fShop.size() / 2);
        // Wait until the RecyclerView is laid out.

//        final SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(mRecyclerView);
//        mRecyclerView.setLayoutManager(cmsc);
//        mRecyclerView.setAdapter(mAdapter);
//       int sizee= fShop.size();
//       Log.d("rvs",String.valueOf(sizee));
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastItem = cmsc.findLastCompletelyVisibleItemPosition();
//                if(lastItem == cmsc.getItemCount()-1){
//                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
//                    Handler postHandler = new Handler();
//                    postHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mRecyclerView.setAdapter(null);
//                            mRecyclerView.setAdapter(mAdapter);
//                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
//                        }
//                    }, 2000);
//                }
//            }
//        });
//        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);

//        mRecyclerView.post(new Runnable() {
//                               @Override
//                               public void run() {
//                                   // Shift the view to snap  near the center of the screen.
//                                   // This does not have to be precise.
//                                   int dx = (mRecyclerView.getWidth() - mRecyclerView.getChildAt(0).getWidth()) / 2;
//                                   mRecyclerView.scrollBy(-dx, 0);
//                                   // Assign the LinearSnapHelper that will initially snap the near-center view.
//                                   LinearSnapHelper snapHelper = new LinearSnapHelper();
//                                   snapHelper.attachToRecyclerView(mRecyclerView);
//                               }
//                           });

        mShimmer=root.findViewById(R.id.shimmerFrameLayout);
//        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);


        loaddt();
        return root;
    }
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            mRecyclerView.smoothScrollBy(pixelsToMove, 2000);
            mHandler.postDelayed(this, duration);
        }
    };
    public void onItemClick(int position) {
//        GalleryData clickFile = fShop.get(position);//,clickMovie.getCate(),clickMovie.getLang()
//        String[] fData = {clickFile.getpName(), clickFile.getPdesc(), clickFile.getPimgurl(),
//                clickFile.getPprice(),clickFile.getPfeature(),clickFile.getPjoin()};
//
//        //    Toast.makeText(this, movieData.toString(), Toast.LENGTH_LONG).show();
//        Log.d("ITEMSs", fData.toString());
//
//        openDetailActivity(fData);
    }

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(getContext(), ShopDetail.class);
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



//    @Override
//    public void onDeleteItemClick(int position) {
//
//    }

    @Override
    public void onClick(View v) {
        String lltv = null;
        switch (v.getId()) {
            case R.id.button:
                tagg = "LAL KITAAB BASIC GRAMMAR";
                break;
            case R.id.button1:
                tagg = "ADVANCE LAL KITAAB";
                break;
            case R.id.button2:
                tagg = "MOBILE NUMEROLOGY";
                break;
            case R.id.button3:
                tagg = "PANCH PAKSHI SHASTRA";
                break;
            case R.id.button4:
                tagg = "KP";
                break;
        }
      gotod();
    }

    private void gotod() {
        if (!tagg.equals(null)&&tagg.equals("ADVANCE LAL KITAAB")||tagg.equals("MOBILE NUMEROLOGY")){
            final Dialog dialog2 = new Dialog(getContext());
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setCanceledOnTouchOutside(true);
            dialog2.setContentView(R.layout.dlang);
            TextView mrthlang2=(TextView) dialog2.findViewById(R.id.mrthlang);
            TextView hindilang2=(TextView) dialog2.findViewById(R.id.hindilang);
            mrthlang2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lang=" - Marathi";
                    tagg = tagg+lang;
                    gotov();
                    dialog2.dismiss();
                }
            });
            hindilang2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lang=" - Hindi";
                    tagg = tagg+lang;
                    gotov();
                    dialog2.dismiss();
                }
            });
            dialog2.show();

        }else {
            gotov();
        }
    }

    private void gotov() {

            getcc();
      //  getcc();


    }
    public void  getcc(){
        //  CourseVideo v = new CourseVideo();
        String value = tagg;
        Log.d("loadddd",value);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        Query qalk = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Buy").child(value);
        Log.d("loadddd",qalk.toString());

        qalk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("payst")) {
                    String pst = dataSnapshot.child("payst").getValue().toString();
                    Log.d("Payt", pst);
                    if (pst!= null && pst.equals("Paid") ) {
                       oldestPostId="Paid";
                        jtocv();
                    } else if (pst!= null && pst.equals("Unpaid")){
                        Log.d("Payt","unpaid");
                        oldestPostId="Unpaid";
                        jtocv();
                    } else {
                        Log.d("Payt","unpaid");
                        oldestPostId="Unpaid";
                        jtocv();
                    }

                }else{
                    Log.d("Payt","unpaid");
                    oldestPostId="Unpaid";
                    jtocv();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void jtocv() {
                if (oldestPostId.equals(null)||oldestPostId.equals("Unpaid")){
                    Intent intent = new Intent(getContext(), Csvd.class);
                    Log.d("tggv",tagg+"by cvd");
                    intent.putExtra("Pkey", tagg);

                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getContext(), CourseVideo.class);
                    Log.d("tggv",tagg+"by cvd");
                    intent.putExtra("Pkey", tagg);

                    startActivity(intent);
                }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void loaddt() {
        String value = tagg;



        if (TextUtils.isEmpty(value)) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Banner");
            Log.d("pathss",mDatabaseRef.toString());
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    fShop.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        GalleryData upload = noticenapshot.getValue(GalleryData.class);
                        upload.setKey(noticenapshot.getKey());
                        fShop.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mShimmer.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);
                }
            });
        } else {

         //   after course done below code need
           Query mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child(tag)
                    .orderByChild("pName")
                    .equalTo(tagg);
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    fShop.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        GalleryData upload = noticenapshot.getValue(GalleryData.class);
                        upload.setKey(noticenapshot.getKey());
                        fShop.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    mShimmer.setVisibility(View.GONE);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });


          // // Toast.makeText(getContext(), mDatabaseRef.toString(), Toast.LENGTH_LONG).show();


        }
    }


    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
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

        Intent loginIntent =  new Intent(getContext(), Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
///        logOutUser();
        startActivity(loginIntent);
       // finish();
    }

}