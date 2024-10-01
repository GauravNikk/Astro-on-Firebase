package com.cilguru.ui.fragments;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cilguru.adapter.RecyclerAdatpterShop;
import com.cilguru.model.ShopData;
import com.cilguru.ui.details.ShopDetail;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class ShopFragment extends Fragment implements RecyclerAdatpterShop.OnItemClickListener, View.OnClickListener {
    private SwipeRefreshLayout swpr;
    private RecyclerView mRecyclerView;
    //  private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<ShopData> fShop;
    private String tag = "Shop";
    private RecyclerAdatpterShop mAdapter;
    private ProgressBar mProgressBar;
    EditText search;
    ImageView menubar;
    public String tagg = null;
    private String oldestPostId;
    private ShimmerFrameLayout mShimmer;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView);

        fShop = new ArrayList<>();
        mAdapter = new RecyclerAdatpterShop(getContext(), fShop);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ShopFragment.this);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
     //   mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager (getContext(),LinearLayoutManager.HORIZONTAL,false));
        mShimmer=root.findViewById(R.id.shimmerFrameLayout);
//        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mShimmer.setVisibility(View.VISIBLE);
        loaddt();
        return root;
    }

    public void onItemClick(int position) {
        ShopData clickFile = fShop.get(position);//,clickMovie.getCate(),clickMovie.getLang()
        String[] fData = {clickFile.getpName(), clickFile.getPdesc(), clickFile.getPimgurl(),
                clickFile.getPprice(),clickFile.getPfeature(),clickFile.getPjoin(),clickFile.getKey()};

        //    Toast.makeText(this, movieData.toString(), Toast.LENGTH_LONG).show();
        Log.d("ITEMSs", fData.toString());

        openDetailActivity(fData);
    }

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(getContext(), ShopDetail.class);
        intent.putExtra("PName", data[0]);
        intent.putExtra("PDesc", data[1]);
        intent.putExtra("PImg", data[2]);
        intent.putExtra("PPrice", data[3]);
        intent.putExtra("PFtr", data[4]);
        intent.putExtra("PJion", data[5]);
        intent.putExtra("PKey", data[6]);
        // intent.putExtra("DATE", data[3]);
        startActivity(intent);
        Log.d("fileintent","done");
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

                    fShop.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        ShopData upload = noticenapshot.getValue(ShopData.class);
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

                    fShop.clear();

                    for (DataSnapshot noticenapshot : dataSnapshot.getChildren()) {
                        ShopData upload = noticenapshot.getValue(ShopData.class);
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
         //   Toast.makeText(getContext(), mDatabaseRef.toString(), Toast.LENGTH_LONG).show();
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