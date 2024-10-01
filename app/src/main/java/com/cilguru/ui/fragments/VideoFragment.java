package com.cilguru.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cilguru.R;
import com.cilguru.adapter.RecyclerAdaptertv;
import com.cilguru.model.YtData;
import com.cilguru.ui.loginreg.Login;
import com.cilguru.ui.web.FullscreenDemoActivity;
import com.cilguru.ui.web.VideoPlayer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class VideoFragment extends Fragment implements RecyclerAdaptertv.OnItemClickListener, View.OnClickListener {
    private SwipeRefreshLayout swpr;
    private RecyclerView mRecyclerView;
    private RecyclerAdaptertv mAdapter;
    private ProgressBar mProgressBar;
    //  private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<YtData> mTvdt;
    private String tag = "Demo";
    private ShimmerFrameLayout mShimmer;
    TextView dmv, blk, adl, kpv, mn,ttltv,wtmore;
    ImageView menubar,livetv;
    public String tagg = null;
    private String oldestPostId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_video, container, false);

        mShimmer = root.findViewById(R.id.shimmerFrameLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        //  swpr=findViewById(R.id.swpr);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        wtmore=root.findViewById(R.id.wtmore);
//        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);

        mTvdt = new ArrayList<>();
        mAdapter = new RecyclerAdaptertv(getContext(), mTvdt);
        livetv=root.findViewById(R.id.livetv);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(VideoFragment.this);

        mAdapter = new RecyclerAdaptertv(getContext(), mTvdt);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);


        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //  mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);


      //  mDatabaseRef = FirebaseDatabase.getInstance().getReference("Demo");
//        dmv = root.findViewById(R.id.dmv);
//        blk = root.findViewById(R.id.blk);
//        adl = root.findViewById(R.id.alk);
//        kpv = root.findViewById(R.id.kpv);
//        mn = root.findViewById(R.id.mn);
        ttltv= root.findViewById(R.id.ttltv);


        loaddt();
        wtmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/channel/UCFCMENaBln3R7uVcHbh9GeA";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        livetv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                DatabaseReference getUserDatabaseReference;
                getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Live");
                getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // retrieve data from db
                        String name = dataSnapshot.child("vid").getValue().toString();
                        //   display_status.setText(status);

                        if (!name.equals(null)&&name.equals("Enter Link")){
                            final Dialog dialog = new Dialog(getContext());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setContentView(R.layout.sorry);
                            TextView txtmsg=(TextView) dialog.findViewById(R.id.txtmsg);
                            txtmsg.setText("No Live Video");
                            dialog.show();
                        }
                        else if(name.equals(null)){
                            final Dialog dialog = new Dialog(getContext());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setContentView(R.layout.sorry);
                            TextView txtmsg=(TextView) dialog.findViewById(R.id.txtmsg);
                            txtmsg.setText("No Live Video");
                            dialog.show();
                        }else {


                          //  String url2 = name;
                            String hi = name;
                            String url2 = hi.replaceAll("https://youtu.be/", "");
                            Intent intent = new Intent(getContext(), FullscreenDemoActivity.class);
                            intent.putExtra("URL", url2);
                            startActivity(intent);
                            //    Toast.makeText(this, movieData.toString(), Toast.LENGTH_LONG).show();
                            Log.d("ITEMSs", url2.toString());

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
        return root;

    }

    public void onItemClick(int position) {
        YtData clickMovie = mTvdt.get(position);//,clickMovie.getCate(),clickMovie.getLang()
        String movieData = clickMovie.getWlink().toString();


        Intent intent = new Intent(getContext(), VideoPlayer.class);
        intent.putExtra("URL", movieData);
        startActivity(intent);
        //    Toast.makeText(this, movieData.toString(), Toast.LENGTH_LONG).show();
        Log.d("ITEMSs", movieData.toString());

    }




    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dmv:
                tagg = "DEMO VIDEO";

                break;
            case R.id.blk:
                tagg = "BLK";
                break;
            case R.id.alk:
                tagg = "ALK";
                break;
            case R.id.mn:
                tagg = "MN";
                break;
            case R.id.kpv:
                tagg = "KPVIDEO";
                break;
        }
     //   Toast.makeText(getContext(), tagg + "is selected", Toast.LENGTH_SHORT).show();
        ttltv.setText(tagg);
        loaddt();
    }


    public void loaddt() {
        String value = tagg;

//        Query q = FirebaseDatabase.getInstance().getReference()
//                .child("Movies")
//                .orderByChild("moviesName")
//                .equalTo(value);

        if (TextUtils.isEmpty(value)) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag).child("DEMO VIDEO");
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mTvdt.clear();

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
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }
            });
            // mDatabaseRef = FirebaseDatabase.getInstance().getReference(String.valueOf(q));
        } else {
            //  mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag);

            //mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag).child("cate");
            Query mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag);
            // .orderByChild(tagg);
            //.equalTo(tagg);
            Log.d("Tagg",mDatabaseRef.toString());
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mTvdt.clear();

                    for (DataSnapshot moviesnapshot : dataSnapshot.getChildren()) {
                        YtData upload = moviesnapshot.getValue(YtData.class);
                        upload.setKey(moviesnapshot.getKey());
                        mTvdt.add(upload);

                    }
                    mAdapter.notifyDataSetChanged();
                    mShimmer.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Log.d("Tagg",tagg);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }
            });
         //   Toast.makeText(getContext(), mDatabaseRef.toString(), Toast.LENGTH_LONG).show();
        }
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