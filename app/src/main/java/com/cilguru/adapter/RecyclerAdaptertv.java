package com.cilguru.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


//import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.cilguru.model.ShopData;
import com.cilguru.ui.details.CourseVideo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.cilguru.R;
import com.cilguru.model.YtData;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;


public  class RecyclerAdaptertv extends RecyclerView.Adapter<RecyclerAdaptertv.RecyclerViewHolder>{
    private Context mContext;
    private List<YtData> livetv;
    private OnItemClickListener mListener;
    private final int limit = 2;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String vall="false";
    //Firebase
    private DatabaseReference mDataref;
    public String tagg = null;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public RecyclerAdaptertv(Context context, List<YtData> uploads) {
        mContext = context;
        livetv = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ytvdo, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String imgurll,imgurl,link;
        //clickMovie.getMoviesName(),clickMovie.getDesc(),clickMovie.getImgurl()
        YtData currentTv = livetv.get(position);
        holder.nameTextView.setText(currentTv.getChName());
       // link=currentTv.getWlink();
        imgurl=currentTv.getWlink().toString();
        //holder.dateTextView.setText(currentTeacher.d());



        String imgu="http://img.youtube.com/vi/";
        String imgur="/3.jpg";
        imgurll=imgu+imgurl+imgur;
        String imgg="http://img.youtube.com/vi/QQvB6QaTjRY/3.jpg";
        Log.d("THUMB",imgu+imgurl+imgur);

        Picasso.get()
                .load("http://img.youtube.com/vi/QQvB6QaTjRY/3.jpg")
                 .placeholder(R.drawable.bnnrcil)
                .fit()
               // .centerCrop()
               //  .load(imgurll)
                .into(holder.teacherImageView);

    }

//    @Override
//    public int getItemCount() {
//        return livetv.size();
//
//    }


    //only 3 video with purchase
    @Override
    public int getItemCount() {
        return livetv.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
             {

        public TextView nameTextView,descriptionTextView,dateTextView;
        public ImageView teacherImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById ( R.id.nameTextView );

            teacherImageView = itemView.findViewById(R.id.teacherImageView);
         //   langtv=itemView.findViewById(R.id.langtv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
      //  void onShowItemClick(int position);
      //  void onDeleteItemClick(int position);

        void onClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
}
