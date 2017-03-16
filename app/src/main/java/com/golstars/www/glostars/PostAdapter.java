package com.golstars.www.glostars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by edson on 07/02/17.
 * class for handling post object lists
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {



    private List<Post> postsList;
    public  Context context;
    private final OnItemClickListener listener;
    private final OnItemClickListener postImgListener;
    private final OnItemClickListener commentsListener;
    private final OnRatingEventListener ratingListener;
    public Integer screenWidth = 0;
    public String usrId = "";




    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username, caption, postTime, totalStars, totalComments, uselessTextView;
        public ImageView postImg;
        public ImageView propic;
        public RatingBar ratingBar;
        public ImageView commentsBtn;

        public MyViewHolder(View view, final OnRatingEventListener ratingListener, final OnItemClickListener listener, final OnItemClickListener postImgListener,
                            final OnItemClickListener commentsListener){
            super(view);
            username=(TextView)view.findViewById(R.id.userNAME);
            caption=(TextView)view.findViewById(R.id.userCAPTION);
            postTime=(TextView)view.findViewById(R.id.uploadTIME);
            postImg = (ImageView)view.findViewById(R.id.userPOST);
            propic = (ImageView)view.findViewById(R.id.userPIC);
            totalStars=(TextView)view.findViewById(R.id.ratingstarcount);
            totalComments=(TextView)view.findViewById(R.id.commentcount);
            ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
            commentsBtn = (ImageView)view.findViewById(R.id.commenticon);
            uselessTextView = (TextView)view.findViewById(R.id.timedigit);

            /*ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            }); */

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

            postImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postImgListener.onItemClickPost(postsList.get(getLayoutPosition()));
                }
            });
            
            commentsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentsListener.onItemClickPost(postsList.get(getLayoutPosition()));
                }
            });
        }



    }

    public PostAdapter(List<Post> postsList, Integer width, String usrId, Context context, OnRatingEventListener ratingListener, OnItemClickListener listener,
                       OnItemClickListener postImgListener, OnItemClickListener commentsListener){
        this.postsList = postsList;
        this.context = context;
        this.ratingListener = ratingListener;
        this.listener = listener;
        this.postImgListener = postImgListener;
        this.screenWidth = width;
        this.usrId = usrId;
        this.commentsListener = commentsListener;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main_feed, parent, false);

        return new MyViewHolder(itemView, ratingListener, listener, postImgListener, commentsListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.username.setText(post.getAuthor());
        holder.caption.setText(post.getDescription());
        holder.postTime.setText(post.getUploaded());
        holder.uselessTextView.setText("");
        holder.ratingBar.setOnRatingBarChangeListener(onRatingBarChangeListener(holder, position, ratingListener));
        holder.ratingBar.setRating((float)getUserRating(post));
        holder.totalStars.setText(String.valueOf(post.getStarsCount()));
        holder.totalComments.setText(String.valueOf(post.getCommentCount()));

        Picasso.with(context)
                .load(post.getPicURL())
                .resize(screenWidth,1000)
                .centerInside()
                .into(holder.postImg);
        Picasso.with(context).load(post.getProfilePicURL()).into(holder.propic);




    }

    private RatingBar.OnRatingBarChangeListener onRatingBarChangeListener(final RecyclerView.ViewHolder holder, final int pos, final OnRatingEventListener ratingListener){
        return new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                JSONObject newRating = new JSONObject();
                try {
                    newRating.put("starsCount", (int)v);
                    newRating.put("raterId", usrId);
                    newRating.put("ratingTime", (new Date()).toString());
                    postsList.get(pos).setRatings(postsList.get(pos).getRatings().put(newRating));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(b) ratingListener.onRatingBarChange(postsList.get(pos), v, pos);


            }
        };
//
    }

    public int getItemCount(){
        return postsList.size();
    }

    public int findPosByPhotoURL(String photoURL){
        for(int i = 0; i < postsList.size(); i++){
            if(postsList.get(i).getPicURL().equals(photoURL)){
                return i;
            }
        }
        return -1;
    }

    public int getUserRating(Post post){
        /*this method searches the list of ratings in this post to find whether
          our current user has rated this or not. If so, return the rating number,
          if not, return 0
        */
        JSONArray data = post.getRatings();
        for(int i = 0; i < data.length(); i++){
            try {
                String raterId = data.getJSONObject(i).getString("raterId");
                if(raterId.equals(usrId)){
                    System.out.println("i liked this pic: ");
                    return data.getJSONObject(i).getInt("starsCount");
                } else{
                    /*System.out.println("poster id is " + data.getJSONObject(i).getString("raterId"));
                    System.out.println("my id is " + usrId);
                    System.out.println("i didnt rate this pic: ");
                    */
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }



}
