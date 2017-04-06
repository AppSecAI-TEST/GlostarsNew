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

public class RecyclerSwipeAdapter extends RecyclerView.Adapter<RecyclerSwipeAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Post> gridImages;
    private LayoutInflater layoutInflater;

    public RecyclerSwipeAdapter(Context mContext, ArrayList<Post> gridImages) {
        this.mContext = mContext;
        this.gridImages = gridImages;
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
        Picasso.with(mContext).load(gridImages.get(position).getPicURL()).into(holder.img);


    }

    @Override
    public int getItemCount() {
        return gridImages.size();
    }

    // convenience method for getting data at click position
    public Post getItem(int id) {
        return gridImages.get(id);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.imageView2);
        }

    }
}
