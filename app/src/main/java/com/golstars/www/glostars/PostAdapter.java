package com.golstars.www.glostars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by edson on 07/02/17.
 * class for handling post object lists
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {



    private List<Post> postsList;
    public  Context context;
    private final OnItemClickListener listener;
    private final OnRatingEventListener ratingListener;




    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username, caption, postTime, totalStars, totalComments;
        public ImageView postImg;
        public ImageView propic;
        public RatingBar ratingBar;

        public MyViewHolder(View view, final OnRatingEventListener ratingListener, final OnItemClickListener listener){
            super(view);
            username=(TextView)view.findViewById(R.id.userNAME);
            caption=(TextView)view.findViewById(R.id.userCAPTION);
            postTime=(TextView)view.findViewById(R.id.uploadTIME);
            postImg = (ImageView)view.findViewById(R.id.userPOST);
            propic = (ImageView)view.findViewById(R.id.userPIC);
            //totalStars=(TextView)view.findViewById(R.id.ratingstarcount);
            //totalComments=(TextView)view.findViewById(R.id.commentcount);
            ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    if(b) ratingListener.onRatingBarChange(postsList.get(getLayoutPosition()), (int) v);

                }
            });

            propic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickPost(postsList.get(getLayoutPosition()));
                }
            });
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickPost(postsList.get(getLayoutPosition()));
                }
            });
        }


    }

    public PostAdapter(List<Post> postsList, Context context, OnRatingEventListener ratingListener, OnItemClickListener listener){
        this.postsList = postsList;
        this.context = context;
        this.ratingListener = ratingListener;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main_feed, parent, false);

        return new MyViewHolder(itemView, ratingListener, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.username.setText(post.getAuthor());
        holder.caption.setText(post.getDescription());
        //holder.totalStars.setText(post.getStarsCount());
        //holder.totalComments.setText(post.getCommentCount());
        Picasso.with(context).load(post.getPicURL()).into(holder.postImg);
        Picasso.with(context).load(post.getProfilePicURL()).into(holder.propic);




    }

    public int getItemCount(){
        return postsList.size();
    }



}
