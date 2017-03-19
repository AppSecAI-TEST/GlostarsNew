package com.golstars.www.glostars;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.golstars.www.glostars.R.id.view;

/**
 * Created by edson on 13/03/17.
 */

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> ImgUrls;
    private OnSinglePicClick listener;
    private LayoutInflater layoutInflater;

    public RecyclerGridAdapter(Context mContext, ArrayList<String> imgUrls, OnSinglePicClick listener) {
        this.mContext = mContext;
        ImgUrls = imgUrls;
        this.listener = listener;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.postimages, parent, false);
        //RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view);


        return new MyViewHolder(view); //listener);
        //return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(mContext).load(ImgUrls.get(position)).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return ImgUrls.size();
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return ImgUrls.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnSinglePicClick itemClickListener) {
        this.listener = itemClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.imageView2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener != null) listener.onItemClick(ImgUrls.get(getAdapterPosition()), getAdapterPosition());

        }
    }
}
