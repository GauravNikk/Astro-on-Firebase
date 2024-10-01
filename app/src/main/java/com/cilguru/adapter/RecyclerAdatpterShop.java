package com.cilguru.adapter;

//public class RecyclerAdatpterShop {
//}

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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


import androidx.recyclerview.widget.RecyclerView;

import com.cilguru.R;
import com.cilguru.model.ShopData;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



//RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder
public  class RecyclerAdatpterShop extends RecyclerView.Adapter<RecyclerAdatpterShop.RecyclerViewHolder>{
    private Context mContext;
    private List<ShopData> shopData;
    private OnItemClickListener mListener;

    public RecyclerAdatpterShop(Context context, List<ShopData> uploads) {
        mContext = context;
        shopData = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.shopmodel, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String imgurll;
        ShopData currentFile = shopData.get(position);
        holder.nameTextView.setText(currentFile.getpName());
        imgurll=currentFile.getPimgurl();
        holder.ftrtv.setText(currentFile.getPfeature());

        Picasso.get()
                .load(imgurll)
                .placeholder(R.drawable.bnnrcil)
                //.fit()
                // .centerCrop()
                // .load(imgurll)
                .into(holder.imgfetch);
        Log.d("Bshop",imgurll);
    }

    @Override
    public int getItemCount() {
        return shopData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameTextView,descriptionTextView,dateTextView,langtv,ftrtv;
        public ImageView imgfetch,download;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById(R.id.nameTextView);
            ftrtv = itemView.findViewById(R.id.ftrtv);
            imgfetch = itemView.findViewById(R.id.imgfetch);
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
        //void onShowItemClick(int position);
       // void onDeleteItemClick(int position);

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
