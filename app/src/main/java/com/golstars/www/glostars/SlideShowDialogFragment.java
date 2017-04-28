package com.golstars.www.glostars;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.golstars.www.glostars.adapters.CommentAdapter;
import com.golstars.www.glostars.models.Comment;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.PictureService;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


/**
 * Created by edson on 20/03/17.
 */

public class SlideShowDialogFragment extends DialogFragment {

    //private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Post> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate,captionfullscreen,ratingcountfullscreen,commentcountfullscreen;
    private RatingBar ratingfullscreen;
    private ImageView commentfullscreen,sharefullscreen,clearRating;
    private int selectedPosition = 0;
    private boolean onBind = false;
    private String token;
    private String usrID;
    MyUser mUser;

    LinearLayout captionContainer;
    private ImageView imageViewPreview;

    public static SlideShowDialogFragment newInstance(){
        SlideShowDialogFragment f = new SlideShowDialogFragment();
        return f;
    }


    boolean isShowCaption=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);

        captionContainer= (LinearLayout) v.findViewById(R.id.captionContainer);

        captionContainer.setVisibility(View.GONE);



        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        //lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        captionfullscreen = (TextView)v.findViewById(R.id.captionFullscreen);

        commentcountfullscreen = (TextView)v.findViewById(R.id.commentcountfullscreen);
        sharefullscreen = (ImageView) v.findViewById(R.id.sharefullscreen);

        clearRating = (ImageView)v.findViewById(R.id.clearRatingfullscreen);
        ratingcountfullscreen = (TextView)v.findViewById(R.id.ratingstarcountfullscreen);
        captionfullscreen = (TextView)v.findViewById(R.id.captionFullscreen);
        ratingfullscreen = (RatingBar)v.findViewById(R.id.ratingBarfullscreen);
        commentfullscreen = (ImageView)v.findViewById(R.id.commenticonfullscreen);



        images = (ArrayList<Post>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");
        token = getArguments().getString("token");
        usrID = getArguments().getString("usrID");


        //Log.e(TAG, "position: " + selectedPosition);
        //Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        mUser = MyUser.getmUser(getContext());

        return v;
    }


    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        //lblCount.setText((position + 1) + " of " + images.size());

        String description = "";


        final Post gridImage = images.get(position);

        if(!gridImage.getDescription().isEmpty()){
            description = gridImage.getDescription();
        }

        final Integer sCount = gridImage.getStarsCount();
        final Integer cCount = gridImage.getCommentCount();
        String starsCount = sCount.toString();
        String commentsCount = cCount.toString();

        captionfullscreen.setText(description);
        lblTitle.setText(gridImage.getAuthor());
        lblDate.setText(gridImage.getUploaded());
        ratingcountfullscreen.setText(starsCount);
        commentcountfullscreen.setText(commentsCount);
        ratingfullscreen.setRating((float)getUserRating(gridImage));

        if(!gridImage.isCompeting()){
            ratingfullscreen.setNumStars(1);
        } else {
            ratingfullscreen.setNumStars(5);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }




    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            onBind = true; // this flag warns as to if the viewHolder is still binding data

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            //instantiating view items
            imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            imageViewPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
                    if(isShowCaption){
                        captionContainer.setVisibility(View.GONE);
                        isShowCaption=false;
                    }else{
                        captionContainer.setVisibility(View.VISIBLE);
                        isShowCaption=true;
                    }
                    //captionContainer.setVisibility(View.GONE);
                }
            });

            //biding post elements to those items
            final Post image = images.get(position);
            final Integer pos = position;


            //String starsCount = "";
            //String commentsCount = "";



            final Integer sCount = image.getStarsCount();
            final Integer cCount = image.getCommentCount();
            //starsCount = sCount.toString();
            //commentsCount = cCount.toString();

            //System.out.println(image.getDescription());
            //System.out.println(image.getStarsCount());









            Glide.with(getActivity()).load(image.getPicURL())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);


            onBind = false;



            commentfullscreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Post item = image;

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View mView = getActivity().getLayoutInflater().inflate(R.layout.commentdialog,null);

                    //TextView commentsbanner = (TextView)mView.findViewById(R.id.commentbannerdialog);
                    ListView commentrecycler  = (ListView)mView.findViewById(R.id.commentrecycler);
                    //ImageView emojibtn = (ImageView)mView.findViewById(R.id.emoji_btn);
                    final EmojiconEditText commentbox = (EmojiconEditText)mView.findViewById(R.id.commentBox);
                    final TextView sendcomment  = (TextView)mView.findViewById(R.id.sendcomment);


                    final ArrayList<Comment> commentsList = new ArrayList<>();
                   /* final CommentAdapter commentAdapter = new CommentAdapter(getContext(), R.layout.content_comment_model, commentsList,
                            new commentModel.BtnClickListener() {
                                @Override
                                public void onItemClick(Comment com) {
                                    Intent intent = new Intent();
                                    intent.putExtra("USER_ID", com.getCommenterId());
                                    intent.setClass(getContext(), user_profile.class);
                                    startActivity(intent);
                                }
                            });*/
                    //commentrecycler.setAdapter(commentAdapter);

                    for(int i = 0; i < item.getComments().length(); i++){
                        try{
                            JSONObject com = item.getComments().getJSONObject(i);
                            Integer commentId = com.getInt("commentId");
                            String  commentMessage = com.getString("commentMessage");
                            String commenterUserName = com.getString("commenterUserName");
                            String commenterID = com.getString("commenterId");
                            String commentTime = com.getString("commentTime");
                            String profilePicUrl = com.getString("profilePicUrl");
                            String firstName = com.getString("firstName");
                            String lastName = com.getString("lastName");
                            Comment comment = new Comment(commentMessage, commenterUserName, commenterID, commentTime, profilePicUrl, firstName, lastName, commentId);
                            commentsList.add(comment);
                            //commentAdapter.notifyDataSetChanged();

                        } catch (JSONException e){
                            e.printStackTrace();
                        }


                    }


                    sendcomment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String comment = String.valueOf(commentbox.getText());
                            try {
                                //postComment(item.getPhotoId(), comment, commentsList, commentAdapter, sendcomment);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            commentbox.setText(""); ///
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();




                }


            });

            /*imageViewPreview.setOnTouchListener(new View.OnTouchListener() {
                android.os.Handler handler = new android.os.Handler();

                int numberOfTaps = 0;
                long lastTapTimeMs = 0;
                long touchDownMs = 0;


                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
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

                                        if(getUserRating(image) == 0){ //only make any change if the user has never rated this pic
                                            singleRate(image);


                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                            JSONArray ratings = image.getRatings();
                                            JSONObject rating = new JSONObject();
                                            try {
                                                rating.put("starsCount", 1); /// on double tap, only one star is added
                                                rating.put("raterId", usrID);
                                                rating.put("ratingTime", ((sdf.format(new Date())).toString()));
                                                ratings.put(rating);
                                                System.out.println(ratings);
                                                image.setRatings(ratings);
                                                image.setStarsCount(image.getStarsCount() + 1);
                                                Integer newCount = sCount + 1;
                                                ratingcountfullscreen.setText(newCount.toString());
                                                System.out.println("my id is " + mUser.getUserId());
                                                System.out.println(image.getRatings());

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            ratingfullscreen.setRating((float)1);
                                            //Toast.makeText(context, "post " + pos + " 's rating changed to " + 1, Toast.LENGTH_SHORT).show();
                                            images.set(pos,image);
                                            notifyDataSetChanged();
                                        }



                                    }
                                }, ViewConfiguration.getDoubleTapTimeout());
                            }
                    }

                    return true;
                }
            });*/


            ratingfullscreen.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    if(!onBind) {
                        if (getUserRating(image) == 0) { //only make any change if the user has never rated this pic
                            onRatingBarChange(image, v, pos);


                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            JSONArray ratings = image.getRatings();
                            JSONObject rating = new JSONObject();
                            try {
                                rating.put("starsCount", (int) v);
                                rating.put("raterId", usrID);
                                rating.put("ratingTime", ((sdf.format(new Date())).toString()));
                                ratings.put(rating);
                                System.out.println("RATINGS ARE " + ratings);
                                image.setRatings(ratings);
                                image.setStarsCount(image.getStarsCount() + (int) v);
                                System.out.println("my id is " + usrID);
                                System.out.println(image.getRatings());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Toast.makeText(context, "post " + pos + " 's rating changed to " + (int)v, Toast.LENGTH_SHORT).show();
                            images.set(pos, image);
                            notifyDataSetChanged();
                        }
                        //} else onRatingBarChange(null, v, pos);

                    }

                }
            });


            //method that handles touch on rating bar
            ratingfullscreen.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(!onBind){
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            float touchPositionX = event.getX();
                            float width = ratingfullscreen.getWidth();
                            float starsf = (touchPositionX / width) * 5.0f;
                            int stars = (int)starsf + 1;
                            if(ratingfullscreen.getRating() == (float)0){
                                ratingfullscreen.setRating(stars);
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


            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
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
                if(raterId.equals(usrID)){
                    return data.getJSONObject(i).getInt("starsCount");
                } else{
                    System.out.println("poster id is " + data.getJSONObject(i).getString("raterId"));
                    System.out.println("my id is " + usrID);
                    System.out.println("i didnt rate this pic: ");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void singleRate(Post item) {
        // double click method that rates only one star to photo
        if(item != null){
            JSONObject msg = new JSONObject();
            try {
                msg.put("NumOfStars", 1);
                msg.put("PhotoId", item.getPhotoId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringEntity entity = null;
            try {
                entity = new StringEntity(msg.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            PictureService.ratePicture(getContext(), token, entity, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println(response);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println(errorResponse);
                }
            });

        }


    }


    public void onRatingBarChange(Post item, float value, int postPosition){
        //method that posts new ratings into server
        if(item != null){
            JSONObject msg = new JSONObject();
            try {
                msg.put("NumOfStars", (int)value);
                msg.put("PhotoId", item.getPhotoId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringEntity entity = null;
            try {
                entity = new StringEntity(msg.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            PictureService.ratePicture(getContext(), token, entity, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println(response);
                }
            });

        }




    }

    void postComment(String picID,  String comment, ArrayList commentsList, CommentAdapter commentAdapter, TextView commentbox) throws Exception{

        if(!comment.isEmpty()){
            PictureService pictureService = new PictureService();
            pictureService.commentPicture(picID, comment, mUser.getToken());
            JSONObject res = null;
            while(res == null){
                res = pictureService.getDataObject();
            }

            Comment dummyComment = new Comment(
                    comment,
                    mUser.getName(),
                    mUser.getUserId(),
                    (new Date()).toString(),
                    mUser.getProfilePicURL(),
                    mUser.getName(),
                    "", 0);



            if(res.getInt("responseCode") == 1){
                commentsList.add(dummyComment);
                commentAdapter.notifyDataSetChanged();
                commentbox.setText("");

            } else Toast.makeText(getContext(), "couldn't connect to the servers", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "write a message before sending", Toast.LENGTH_LONG).show();
        }


    }






}