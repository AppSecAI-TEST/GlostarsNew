package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.golstars.www.glostars.MyUser;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.interfaces.OnRatingEventListener;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by edson on 07/02/17.
 * class for handling post object lists
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    private ArrayList<Post> postsList;
    public  Context context;
    private final OnItemClickListener listener;
    private final OnItemClickListener postImgListener;
    private final OnItemClickListener commentsListener;
    private final OnItemClickListener deleteListener;
    private final OnRatingEventListener ratingListener;
    public Integer screenWidth = 0;
    public String usrId = "";
    private boolean onBind;
    private int resource;
    MyUser mUser = MyUser.getmUser();



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username, caption, postTime, totalStars, totalComments, uselessTextView,comptext;
        public ImageView postImg;
        public ImageView propic;
        public RatingBar ratingBar;
        public ImageView commentsBtn;
        public RelativeLayout featuredFlag;
        public ImageView deleteIcon;

        public MyViewHolder(View view, final OnRatingEventListener ratingListener, final OnItemClickListener listener, final OnItemClickListener postImgListener,
                            final OnItemClickListener commentsListener){
            super(view);
            Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Ubuntu-Light.ttf");

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
            comptext = (TextView)view.findViewById(R.id.comptext);
            featuredFlag = (RelativeLayout)view.findViewById(R.id.compflag);
            deleteIcon = (ImageView)view.findViewById(R.id.clearRating);

            username.setTypeface(type);
            caption.setTypeface(type);
            postTime.setTypeface(type);
            totalStars.setTypeface(type);
            totalComments.setTypeface(type);
            comptext.setTypeface(type);


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

            /*
            postImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postImgListener.onItemClickPost(postsList.get(getLayoutPosition()));
                }
            }); */


            commentsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentsListener.onItemClickPost(postsList.get(getLayoutPosition()));
                }
            });


        }

    }

    public PostAdapter(ArrayList<Post> postsList, Integer width, String usrId, Context context, OnRatingEventListener ratingListener, OnItemClickListener listener,
                       OnItemClickListener postImgListener, OnItemClickListener commentsListener, OnItemClickListener deleteListener, int resource){
        this.postsList = postsList;
        this.context = context;
        this.ratingListener = ratingListener;
        this.listener = listener;
        this.postImgListener = postImgListener;
        this.screenWidth = width;
        this.usrId = usrId;
        this.commentsListener = commentsListener;
        this.deleteListener = deleteListener;
        this.resource = resource;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);


                //.inflate(R.layout.content_main_feed, parent, false);


        return new MyViewHolder(itemView, ratingListener, listener, postImgListener, commentsListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        onBind = true; // this flag warns as to if the viewHolder is still binding data

        final Integer pos = position;
        final Post post = postsList.get(position);
        holder.username.setText(post.getAuthor());
        holder.caption.setText(post.getDescription());
        holder.postTime.setText(post.getUploaded());
        holder.uselessTextView.setText("");
        if(!post.isCompeting()){
            holder.ratingBar.setNumStars(1);
        } else holder.ratingBar.setNumStars(5);

        holder.ratingBar.setRating((float)getUserRating(post));
        holder.totalStars.setText(String.valueOf(post.getStarsCount()));
        holder.totalComments.setText(String.valueOf(post.getCommentCount()));

        Picasso.with(context)
                .load(post.getPicURL())
                .resize(screenWidth,1000)
                .centerInside()
                .into(holder.postImg);
        Picasso.with(context).load(post.getProfilePicURL()).into(holder.propic);

        if(post.isFeatured()){
            holder.featuredFlag.setVisibility(View.VISIBLE);
        } else holder.featuredFlag.setVisibility(View.GONE);

        onBind = false;

        holder.postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImgListener.onItemClickPost(post);
            }
        });


        /*
        holder.postImg.setOnTouchListener(new View.OnTouchListener() {
            android.os.Handler handler = new android.os.Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()){
                            //it was not a tap
                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                        }

                        if(numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()){
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if(numberOfTaps == 2){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap

                                    if(getUserRating(post) == 0){ //only make any change if the user has never rated this pic
                                        //METHOD BELOW RATES PIC ON SINGLE STAR
                                        postImgListener.onItemClickPost(post);


                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                        JSONArray ratings = post.getRatings();
                                        JSONObject rating = new JSONObject();
                                        try {
                                            rating.put("starsCount", 1); /// on double tap, only one star is added
                                            rating.put("raterId", mUser.getUserId());
                                            rating.put("ratingTime", ((sdf.format(new Date())).toString()));
                                            ratings.put(rating);
                                            System.out.println(ratings);
                                            post.setRatings(ratings);
                                            post.setStarsCount(post.getStarsCount() + 1);
                                            System.out.println("my id is " + mUser.getUserId());
                                            System.out.println(post.getRatings());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        holder.ratingBar.setRating((float)1);
                                        //Toast.makeText(context, "post " + pos + " 's rating changed to " + 1, Toast.LENGTH_SHORT).show();
                                        postsList.set(pos,post);
                                        notifyDataSetChanged();
                                    }



                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }
        });
        */



        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /* this big and clumsy method cleans the previously
                   given rating by the user on referred post picture
                   - it first sends data back to the activity so it can be
                   unrated on the server
                   - then it searches the local rating JSONArray list for the
                   current user's rating object and gets its position
                   - then it makes a string list with all the rating objects
                   that belong to the current post picture
                   - removes the user's rating object
                   - brings the list back its JSONArray state and sets it as
                   updated rating list of the current post pic
                * */
                if(!onBind){
                    System.out.println("my user is " + mUser.getUserId());
                    deleteListener.onItemClickPost(post);
                    JSONArray ratings = post.getRatings();
                    int ratingToRemove = 0;
                    int itempos = -1;
                    for(int i = 0; i < ratings.length(); i++){
                        try {
                            if(ratings.getJSONObject(i).getString("raterId").equals(mUser.getUserId())){
                                ratingToRemove = ratings.getJSONObject(i).getInt("starsCount");
                                itempos = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayList<String> list = new ArrayList<String>();
                    int len = ratings.length();

                    if(ratings != null){
                        for(int i =0; i < len; i++){
                            try {
                                list.add(ratings.get(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(itempos > -1){
                        list.remove(itempos);
                    }



                    JSONArray newRatings = new JSONArray();

                    for(int i = 0; i < list.size(); i++){
                        try {
                            JSONObject rating = new JSONObject(list.get(i));

                            newRatings.put(rating);
                            System.out.println("LIST ITEM ADDED " + newRatings);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    System.out.println("string array resultant(list) :" + list);
                    System.out.println("string array resultant(method) :" + newRatings);
                    Post mPost = postsList.get(pos);
                    mPost.setRatings(newRatings);
                    mPost.setStarsCount(mPost.getStarsCount() - ratingToRemove);
                    //postsList.get(pos).setStarsCount(post.getStarsCount() - ratingToRemove);
                    postsList.set(pos,mPost);


                    System.out.println("string array resultant(post) :" + mPost.getRatings());
                    System.out.println("string array resultant(postslist) :" + postsList.get(pos).getRatings());
                    holder.ratingBar.setRating((float)0);
                    notifyDataSetChanged();

                }


            }
        });


        holder.ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(!onBind){
                    if(getUserRating(post) == 0){ //only make any change if the user has never rated this pic
                        ratingListener.onRatingBarChange(post, v, pos);


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        JSONArray ratings = post.getRatings();
                        JSONObject rating = new JSONObject();
                        try {
                            rating.put("starsCount", (int)v);
                            rating.put("raterId", mUser.getUserId());
                            rating.put("ratingTime", ((sdf.format(new Date())).toString()));
                            ratings.put(rating);
                            System.out.println(ratings);
                            post.setRatings(ratings);
                            post.setStarsCount(post.getStarsCount() + (int)v);
                            System.out.println("my id is " + mUser.getUserId());
                            System.out.println(post.getRatings());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(context, "post " + pos + " 's rating changed to " + (int)v, Toast.LENGTH_SHORT).show();
                        postsList.set(pos,post);
                        notifyDataSetChanged();
                    }
                } else ratingListener.onRatingBarChange(null, v, pos);



            }
        });

        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!onBind){
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        float touchPositionX = event.getX();
                        float width = holder.ratingBar.getWidth();
                        float starsf = (touchPositionX / width) * 5.0f;
                        int stars = (int)starsf + 1;

                        if((int)holder.ratingBar.getRating() == 0){
                            System.out.println("CURRENT RATING IS " + holder.ratingBar.getRating());
                            holder.ratingBar.setRating(stars);
                            notifyDataSetChanged();
                        }



                        v.setPressed(false);
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setPressed(true);
                    }

                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        v.setPressed(false);
                    }

                }







                return true;
            }});




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

    private int getUserRating(Post post){
        /*this method searches the list of ratings in this post to find whether
          our current user has rated this or not. If so, return the rating number,
          if not, return 0
        */
        JSONArray data = post.getRatings();
        for(int i = 0; i < data.length(); i++){
            try {
                String raterId = data.getJSONObject(i).getString("raterId");
                if(raterId.equals(usrId)){
                    System.out.println("DIRTY PIC");
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
        System.out.println("PRISTINE PIC");
        return 0;
    }
    //





}