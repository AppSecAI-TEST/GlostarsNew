package com.golstars.www.glostars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by edson on 07/02/17.
 * class for handling post object lists
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    public List<Post> postsList;
    public  Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username, caption, postTime, totalStars, totalComments;
        public ImageView post;
        public ImageButton propic;

        public MyViewHolder(View view){
            super(view);
            username=(TextView)view.findViewById(R.id.userNAME);
            caption=(TextView)view.findViewById(R.id.userCAPTION);
            postTime=(TextView)view.findViewById(R.id.uploadTIME);
            post = (ImageView)view.findViewById(R.id.userPOST);
            propic = (ImageButton)view.findViewById(R.id.userPIC);
            //totalStars=(TextView)view.findViewById(R.id.numStars);
            //totalComments=(TextView)view.findViewById(R.id.numComments);
        }


    }

    public PostAdapter(List<Post> postsList, Context context){
        this.postsList = postsList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main_feed, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.username.setText(post.getAuthor());
        holder.caption.setText(post.getDescription());
        Glide.with(context).load(post.getPicURL()).into(holder.post);
        //holder.post.setImageBitmap(post.getPicURL());
        holder.propic.setImageBitmap(post.getProfilePicURL());
//        holder.totalStars.setText(post.getStarsCount());


    }

    public int getItemCount(){
        return postsList.size();
    }
}
