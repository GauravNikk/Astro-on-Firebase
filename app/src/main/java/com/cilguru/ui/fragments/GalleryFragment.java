package com.cilguru.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cilguru.R;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cilguru.adapter.RecyclerAdatpterShop;
import com.cilguru.adapter.RecyclerGallery;

import com.cilguru.model.GalleryData;
import com.cilguru.model.HomeData;
import com.cilguru.ui.details.FullImage;
import com.cilguru.ui.details.ShopDetail;
import com.cilguru.ui.other.Nikkimg;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iriis.libzoomableimageview.ZoomableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class GalleryFragment extends Fragment implements RecyclerGallery.OnItemClickListener, View.OnClickListener {
    private SwipeRefreshLayout swpr;
    private RecyclerView mRecyclerView;
    private RecyclerGallery mAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ProgressBar mProgressBar;
    //  private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<GalleryData> fHome;
    private String tag = "Gallery";
    EditText search;
    ImageView menubar;
    public String tagg = null;
    private String oldestPostId;
    private ShimmerFrameLayout mShimmer;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //state.putParcelable(LIST_STATE_KEY, layoutManager.onSaveInstanceState());
        mRecyclerView = root.findViewById(R.id.recyclerView);

        fHome = new ArrayList<>();
        mAdapter = new RecyclerGallery(getContext(), fHome);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(GalleryFragment.this);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager (getContext()));
        mShimmer=root.findViewById(R.id.shimmerFrameLayout);
//        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);
        loaddt();
        return root;
    }

    public void onItemClick(int position) {
        GalleryData clickFile = fHome.get(position);//,clickMovie.getCate(),clickMovie.getLang()
       String fData = clickFile.getWlink();

        Log.d("ITEMSs", fData);
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.custom_box);
        ZoomableImageView zoomableImageView = (ZoomableImageView) dialog.findViewById(R.id.iv_zoomable);
        zoomableImageView.setPath(fData);

        dialog.show();
    }

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(getContext(), FullImage.class);
        intent.putExtra("PName", data[0]);

    }



    @Override
    public void onClick(View v) {

    }

    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    public void loaddt() {
        String value = tagg;

        if (TextUtils.isEmpty(value)) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag);
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    fHome.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        GalleryData upload = noticenapshot.getValue(GalleryData.class);
                        upload.setKey(noticenapshot.getKey());
                        fHome.add(upload);
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
            // mDatabaseRef = FirebaseDatabase.getInstance().getReference(String.valueOf(q));
        } else {
            //  mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag);

            //mDatabaseRef = FirebaseDatabase.getInstance().getReference(tag).child("cate");
            Query mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child(tag)
                    .orderByChild("bnnr");
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fHome.clear();
                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        GalleryData upload = noticenapshot.getValue(GalleryData.class);
                        upload.setKey(noticenapshot.getKey());
                        fHome.add(upload); }
                    mAdapter.notifyDataSetChanged();
                    mShimmer.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE); }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mShimmer.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
        //    Toast.makeText(getContext(), mDatabaseRef.toString(), Toast.LENGTH_LONG).show();
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


}