package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.MyUser;
import com.golstars.www.glostars.R;
import com.golstars.www.glostars.ServerInfo;
import com.golstars.www.glostars.hashtagResults;
import com.golstars.www.glostars.models.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Arif on 4/21/2017.
 */

/*public class PostData {
    
}*/
public class PostData extends RecyclerView.Adapter<PostData.MyViewHolder> {
    private ArrayList<Hashtag> data;
    MyUser mUser = MyUser.getmUser();
    Context context;
    public Integer screenWidth = 0;
    private boolean onBind;
    public PostData(ArrayList<Hashtag> data,Context context,Integer screenWidth) {
        this.data=data;
        this.context=context;
        this.screenWidth=screenWidth;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main_feed,parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Integer pos = position;
        final Hashtag post = data.get(position);
        holder.username.setText(post.getPoster().getName());

        //Use for hash tag
        HashTagHelper mTextHashTagHelper;
        mTextHashTagHelper = HashTagHelper.Creator.create(context.getResources().getColor(R.color.hashtag), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                context.startActivity(new Intent(context,hashtagResults.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("data",hashTag));
            }
        });

        // pass a TextView or any descendant of it (incliding EditText) here.
        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
        holder.caption.setText(post.getDescription());
        mTextHashTagHelper.handle(holder.caption);

        holder.postTime.setText(post.getUploaded());
        holder.uselessTextView.setText("");
        if(!post.isIsCompeting()){
            holder.ratingBar.setNumStars(1);
        }
        else holder.ratingBar.setNumStars(5);

        holder.ratingBar.setRating((float)post.getMyStarCount());
        holder.totalStars.setText(String.valueOf(post.getStarsCount()));
        holder.totalComments.setText(post.getComments().size()+"");

        Picasso.with(context)
                .load(post.getPicUrl())
                .placeholder(R.drawable.loading)
                .resize(screenWidth,1000)
                .centerInside()
                .into(holder.postImg);
        Picasso.with(context).load(post.getPoster().getProfilePicURL()).into(holder.propic);

        if(post.isIsfeatured()){
            holder.featuredFlag.setVisibility(View.VISIBLE);
            holder.seeAllcomp.setVisibility(View.VISIBLE);
        } else{
            holder.featuredFlag.setVisibility(View.GONE);
            holder.seeAllcomp.setVisibility(View.GONE);
        }


        onBind = false;

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ServerInfo.BASE_URL_API+"images/rating";

                AsyncHttpClient client=new AsyncHttpClient();
                client.addHeader("Authorization", "Bearer " + mUser.getToken());
                try {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    client.setSSLSocketFactory(sf);
                }
                catch (Exception e) {}

                RequestParams params=new RequestParams();
                params.add("NumOfStars","0");
                params.add("PhotoId",data.get(position).getId()+"");
                client.post(context, url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("1. "+response.toString());
                            try {
                                data.get(position).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                                data.get(position).setMyStarCount(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            notifyDataSetChanged();
                            System.out.println("Loading complete");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });


        /****************Rating bar Change ***********************************/
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float v, boolean b) {

                String url = ServerInfo.BASE_URL_API+"images/rating";

                AsyncHttpClient client=new AsyncHttpClient();
                client.addHeader("Authorization", "Bearer " + mUser.getToken());
                try {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    client.setSSLSocketFactory(sf);
                }
                catch (Exception e) {}

                RequestParams params=new RequestParams();
                params.add("NumOfStars",(int)ratingBar.getRating()+"");
                params.add("PhotoId",data.get(position).getId()+"");
                client.post(context, url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("1. "+response.toString());
                            try {
                                data.get(position).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                                data.get(position).setMyStarCount((int)holder.ratingBar.getRating());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            notifyDataSetChanged();
                            System.out.println("Loading complete");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });



            }
        });

        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = holder.ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    holder.ratingBar.setRating(stars);


                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});
        /******************************************************************/


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username, caption, postTime, totalStars, totalComments, uselessTextView,comptext;
        public ImageView postImg;
        public ImageView propic;
        public RatingBar ratingBar;
        public ImageView commentsBtn;
        public RelativeLayout featuredFlag,seeAllcomp;
        public ImageView deleteIcon;
        public MyViewHolder(View view) {
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
            seeAllcomp = (RelativeLayout)view.findViewById(R.id.seeAllcomppost);

            username.setTypeface(type);
            caption.setTypeface(type);
            postTime.setTypeface(type);
            totalStars.setTypeface(type);
            totalComments.setTypeface(type);
            comptext.setTypeface(type);
        }
    }
}
