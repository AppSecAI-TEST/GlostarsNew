package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.MyUser;
import com.golstars.www.glostars.R;
import com.golstars.www.glostars.SingleItemDialogFragment;
import com.golstars.www.glostars.interfaces.OnSinglePicClick;
import com.golstars.www.glostars.newFullscreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arif on 5/1/2017.
 */

public class CompetitionData extends RecyclerView.Adapter<CompetitionData.MyViewHolder> {
    private Context mContext;
    private ArrayList<Hashtag> ImgUrls;
    private LayoutInflater layoutInflater;
    MyUser mUser = MyUser.getmUser();
    FragmentManager fm;

    public CompetitionData(Context mContext, ArrayList<Hashtag> imgUrls,FragmentManager fm) {
        this.mContext = mContext;
        this.ImgUrls = imgUrls;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.fm=fm;
    }

    @Override
    public CompetitionData.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.postimages, parent, false);
        return new CompetitionData.MyViewHolder(view); //listener);
        //return null;
    }

    @Override
    public void onBindViewHolder(CompetitionData.MyViewHolder holder, final int position) {
        //Picasso.with(mContext).load(ImgUrls.get(position).getPicUrl()).into(holder.img);
        Glide
                .with(mContext)
                .load(ImgUrls.get(position).getPicUrlMedium())
                .placeholder(R.drawable.loading_new)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putString("token", mUser.getToken());
                bundle.putString("usrID", mUser.getUserId());


                FragmentTransaction ft = fm.beginTransaction();
                SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");*/

                Bundle bundle = new Bundle();
                bundle.putParcelable("post", ImgUrls.get(position));
                bundle.putString("token", mUser.getToken());
                bundle.putString("usrID", mUser.getUserId());
                bundle.putParcelable("poster", ImgUrls.get(position).getPoster());



                Intent intent = new Intent(mContext, newFullscreen.class);
                intent.putExtras(bundle);
                //intent.putExtra("post", (Serializable) post);
                mContext.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return ImgUrls.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.imageView2);
        }
    }
}
