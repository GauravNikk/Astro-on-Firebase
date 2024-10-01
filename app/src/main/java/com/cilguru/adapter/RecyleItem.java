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

import androidx.recyclerview.widget.RecyclerView;

import com.cilguru.R;
import com.cilguru.model.Req;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public  class RecyleItem extends RecyclerView.Adapter<RecyleItem.RecyclerViewHolder> {
    private Context mContext;
    private List<Req> rData;

    public RecyleItem(Context context, List<Req> uploads) {
        mContext = context;
        rData = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.prhitem, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String imgurll;
        Req currentFile = rData.get(position);
        holder.pname.setText(currentFile.getPname());
        imgurll = currentFile.getPimgurl();
        holder.pprice.setText(currentFile.getPprice());
        holder.pymwnt.setText(currentFile.getPayst());

        Picasso.get()
                .load(imgurll)
                .placeholder(R.drawable.itemimg)

                .into(holder.imgfetch);
        Log.d("Bshop", imgurll);
    }

    @Override
    public int getItemCount() {
        return rData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView pname, pymwnt, uname, pprice, descriptionTextView, dateTextView, langtv, ftrtv;
        public ImageView imgfetch, download;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.pname);
            pprice = itemView.findViewById(R.id.pprice);
            pymwnt = itemView.findViewById(R.id.pymwnt);
            imgfetch = itemView.findViewById(R.id.imgfetch);


        }


    }
}