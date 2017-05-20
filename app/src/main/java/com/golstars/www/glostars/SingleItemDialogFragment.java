package com.golstars.www.glostars;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.adapters.CommentData;
import com.golstars.www.glostars.adapters.ImagePagerAdapter;
import com.golstars.www.glostars.adapters.PostData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.content.ContentValues.TAG;


/**
 * Created by edson on 20/03/17.
 */

public class SingleItemDialogFragment extends DialogFragment {

    //private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Hashtag> postData;
    private TextView lblCount, lblTitle, lblDate,captionfullscreen,ratingcountfullscreen,commentcountfullscreen;
    private RatingBar ratingfullscreen;
    private ImageView commentfullscreen,sharefullscreen,clearRating;
    private Button followfullscreen;
    private int selectedPosition = 0;
    private boolean onBind = false;
    private String token;
    private String usrID;
    MyUser mUser;
    //PostData postDataAdapter;
    RecyclerView.Adapter postDataAdapter;

    LinearLayout captionContainer;

    Context context;
    ViewPager pager;
    ImagePagerAdapter pagerAdapter;


    private ImageView imageViewPreview;

    public static SingleItemDialogFragment newInstance(){
        SingleItemDialogFragment f = new SingleItemDialogFragment();
        return f;
    }


    boolean isShowCaption=false;
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        captionContainer= (LinearLayout) v.findViewById(R.id.captionContainer);

        captionContainer.setVisibility(View.GONE);

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



        //images = (ArrayList<Hashtag>) getArguments().getSerializable("images");
        postData=((AdapterInfomation)context).getAllData();
        postDataAdapter=((AdapterInfomation)context).getAdapter();

        System.out.println("Get total data "+postData.size());
        selectedPosition = getArguments().getInt("position");
        token = getArguments().getString("token");
        usrID = getArguments().getString("usrID");

        mUser = MyUser.getmUser(getContext());

        return v;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        postData=((AdapterInfomation)context).getAllData();
        postDataAdapter=((AdapterInfomation)context).getAdapter();

        System.out.println("Get total data "+postData.size());
        selectedPosition = getArguments().getInt("position");
        token = getArguments().getString("token");
        usrID = getArguments().getString("usrID");

        mUser = MyUser.getmUser();


        final RelativeLayout root = new RelativeLayout(getActivity());
        //View v = LayoutInflater.from(context).inflate(R.layout.fragment_image_slider,null);

        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        //root.addView(v);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setContentView(R.layout.fragment_image_slider);



        pagerAdapter=new ImagePagerAdapter(context,postData,this);


        pager= (ViewPager) dialog.findViewById(R.id.viewpager);

        pager.setAdapter(pagerAdapter);

        pager.setCurrentItem(selectedPosition);






        captionContainer= (LinearLayout) dialog.findViewById(R.id.captionContainer);
        captionContainer.setVisibility(View.GONE);



        //captionContainer.setVisibility(View.GONE);
       /* captionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captionContainer.setVisibility(View.VISIBLE);
            }
        });*/
        /*viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(captionContainer.getVisibility()== View.VISIBLE){
                    captionContainer.setVisibility(View.GONE);
                }else{
                    captionContainer.setVisibility(View.VISIBLE);
                }
            }
        });*/
        // viewpager.setSi


        //lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) dialog.findViewById(R.id.title);
        lblTitle.setText(postData.get(selectedPosition).getPoster().getName());
        lblTitle.setTextColor(ContextCompat.getColor(context,R.color.lightViolate));
        lblDate = (TextView) dialog.findViewById(R.id.date);
        lblDate.setText(postData.get(selectedPosition).getUploaded());
        captionfullscreen = (TextView)dialog.findViewById(R.id.captionFullscreen);
        captionfullscreen.setText(postData.get(selectedPosition).getDescription());
        followfullscreen = (Button)dialog.findViewById(R.id.followfullscreen);

        final Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Light.ttf");
        lblTitle.setTypeface(type);
        lblDate.setTypeface(type);
        captionfullscreen.setTypeface(type);
        followfullscreen.setTypeface(type);
        followfullscreen.setTransformationMethod(null);



        lblTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, user_profile.class);
                intent.putExtra("USER_ID",postData.get(selectedPosition).getPoster().getUserId());

                context.startActivity(intent);
            }
        });



        if(postData.get(selectedPosition).is_mutual()){
            followfullscreen.setText("Mutual");
            followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
            followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
            followfullscreen.setTransformationMethod(null);
            followfullscreen.setTypeface(type);
        }else if(postData.get(selectedPosition).isMe_follow()){
            followfullscreen.setText("Following");
            followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
            followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
            followfullscreen.setTransformationMethod(null);
            followfullscreen.setTypeface(type);
        }else if(postData.get(selectedPosition).isHe_follow()){
            followfullscreen.setText("follower");
            followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
            followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
            followfullscreen.setTransformationMethod(null);
            followfullscreen.setTypeface(type);
        }else{
            followfullscreen.setText("follow");
            followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
            followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
            followfullscreen.setTransformationMethod(null);
            followfullscreen.setTypeface(type);
        }




        LinearLayout commentContainer= (LinearLayout) dialog.findViewById(R.id.commentContainer);
        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout root = new RelativeLayout(context);
                root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(root);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setContentView(R.layout.commentdialog);
                final ListView commentrecycler = (ListView) dialog.findViewById(R.id.commentrecycler);
                final EmojiconEditText commentbox = (EmojiconEditText) dialog.findViewById(R.id.commentBox);
                final TextView sendcomment = (TextView) dialog.findViewById(R.id.sendcomment);
                final List<Comment> listAllComment=postData.get(selectedPosition).getComments();
                final CommentData commentData=new CommentData(context,listAllComment);
                commentrecycler.setAdapter(commentData);
                commentData.notifyDataSetChanged();

                ImageView emoji_btn= (ImageView) dialog.findViewById(R.id.emoji_btn);

                ImageView dialogClose= (ImageView) dialog.findViewById(R.id.imageView4);

                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                EmojIconActions emojIcon = new EmojIconActions(context, root, commentbox, emoji_btn);

                emojIcon.setUseSystemEmoji(true);
                commentbox.setUseSystemDefault(true);

                emojIcon.ShowEmojIcon();
//

                emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
                    @Override
                    public void onKeyboardOpen() {
                        Log.e(TAG, "Keyboard opened!");
                    }

                    @Override
                    public void onKeyboardClose() {
                        Log.e(TAG, "Keyboard closed");
                    }
                });






                sendcomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String comment = String.valueOf(commentbox.getText());

                        try {
                            String url = ServerInfo.BASE_URL + "api/images/comment";
                            AsyncHttpClient client=new AsyncHttpClient();
                            try {
                                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                trustStore.load(null, null);
                                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                client.setSSLSocketFactory(sf);
                            }
                            catch (Exception e) {}
                            RequestParams msg=new RequestParams();
                            client.addHeader("Authorization", "Bearer " + mUser.getToken());
                            msg.add("CommentText", comment);
                            msg.add("PhotoId", postData.get(selectedPosition).getId()+"");

                            client.post(context, url,msg,new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        JSONObject comment=response.getJSONObject("resultPayload");
                                        Comment c=new Comment(comment.getInt("commentId"),comment.getString("commentMessage"),comment.getString("commenterUserName"),comment.getString("commentUserNameId"), Timestamp.getInterval(Timestamp.getOwnZoneDateTime(comment.getString("commentTime"))),comment.getString("profilePicUrl"),comment.getString("firstName"),comment.getString("lastName"));
                                        listAllComment.add(c);
                                        commentData.notifyDataSetChanged();
                                        postData.get(selectedPosition).setComments(listAllComment);
                                        postDataAdapter.notifyDataSetChanged();
                                        commentrecycler.setSelection(listAllComment.size()-1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        commentbox.setText("");
                    }
                });

               /* mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                dialog.show();
            }
        });



        commentcountfullscreen = (TextView)dialog.findViewById(R.id.commentcountfullscreen);
        commentcountfullscreen.setText(postData.get(selectedPosition).getComments().size()+"");
        //sharefullscreen = (ImageView) dialog.findViewById(R.id.sharefullscreen);

        clearRating = (ImageView)dialog.findViewById(R.id.clearRatingfullscreen);
        ratingcountfullscreen = (TextView)dialog.findViewById(R.id.ratingstarcountfullscreen);
        ratingcountfullscreen.setText(postData.get(selectedPosition).getStarsCount()+"");
        ratingfullscreen = (RatingBar)dialog.findViewById(R.id.ratingBarfullscreen);
        commentfullscreen = (ImageView)dialog.findViewById(R.id.commenticonfullscreen);

        ratingfullscreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingfullscreen.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    ratingfullscreen.setRating(stars);
                    System.out.println("Call rating touching...");
                    //needChange=true;
                    v.setPressed(false);
                    changeRating(ratingfullscreen,selectedPosition);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});




        if(!postData.get(selectedPosition).isIsCompeting()){
            ratingfullscreen.setNumStars(1);
        }
        else ratingfullscreen.setNumStars(5);

        ratingfullscreen.setRating((float)(postData.get(selectedPosition).getMyStarCount()));

        postDataAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                pagerAdapter.notifyDataSetChanged();
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}


            public void onPageSelected(int position) {
                //which item currently view

                lblTitle.setText(postData.get(position).getPoster().getName());
                lblDate.setText(postData.get(position).getUploaded());
                captionfullscreen.setText(postData.get(position).getDescription());
                commentcountfullscreen.setText(postData.get(position).getComments().size()+"");
                ratingcountfullscreen.setText(postData.get(position).getStarsCount()+"");
                if(!postData.get(position).isIsCompeting()){
                    ratingfullscreen.setNumStars(1);
                }
                else ratingfullscreen.setNumStars(5);

                ratingfullscreen.setRating((float)(postData.get(position).getMyStarCount()));
                selectedPosition=position;



                if(postData.get(position).is_mutual()){
                    followfullscreen.setText("Mutual");
                    followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                    followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                    followfullscreen.setTransformationMethod(null);
                    followfullscreen.setTypeface(type);
                }else if(postData.get(position).isMe_follow()){
                    followfullscreen.setText("Following");
                    followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                    followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                    followfullscreen.setTransformationMethod(null);
                    followfullscreen.setTypeface(type);
                }else if(postData.get(position).isHe_follow()){
                    followfullscreen.setText("follower");
                    followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                    followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                    followfullscreen.setTransformationMethod(null);
                    followfullscreen.setTypeface(type);
                }else{
                    followfullscreen.setText("follow");
                    followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                    followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                    followfullscreen.setTransformationMethod(null);
                    followfullscreen.setTypeface(type);
                }



            }
        });





        followfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Integer selected=new Integer(selectedPosition);


                if(followfullscreen.getText().toString().equalsIgnoreCase("follower") || followfullscreen.getText().toString().equalsIgnoreCase("follow")){
                    String url = ServerInfo.BASE_URL_FOLLOWER_API+"Following/"+postData.get(selectedPosition).getPoster().getUserId();
                    AsyncHttpClient client=new AsyncHttpClient();
                    try {
                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);
                        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                        client.setSSLSocketFactory(sf);
                    }
                    catch (Exception e) {}
                    MyUser myUser=MyUser.getmUser();
                    client.addHeader("Authorization", "Bearer " + myUser.getToken());
                    RequestParams requestParams=new RequestParams();

                    client.post(context, url,requestParams,new JsonHttpResponseHandler(){


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                System.out.println("1. "+response.toString());
                                if(response.getJSONObject("resultPayload").getBoolean("result")){
                                    if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                        followfullscreen.setText("Mutual");
                                        followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                                        followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                                        followfullscreen.setTransformationMethod(null);
                                        followfullscreen.setTypeface(type);

                                        postData.get(selected).setIs_mutual(true);
                                        postData.get(selected).setHe_follow(true);
                                        postData.get(selected).setMe_follow(true);

                                    }
                                    else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                        followfullscreen.setText("Following");
                                        followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                                        followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                                        followfullscreen.setTransformationMethod(null);
                                        followfullscreen.setTypeface(type);

                                        postData.get(selected).setIs_mutual(false);
                                        postData.get(selected).setHe_follow(false);
                                        postData.get(selected).setMe_follow(true);

                                    }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                        followfullscreen.setText("follower");
                                        followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                                        followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                                        followfullscreen.setTransformationMethod(null);
                                        followfullscreen.setTypeface(type);

                                        postData.get(selected).setIs_mutual(false);
                                        postData.get(selected).setHe_follow(true);
                                        postData.get(selected).setMe_follow(false);
                                    }else{
                                        followfullscreen.setText("follow");
                                        followfullscreen.setBackground(ContextCompat.getDrawable(context,R.drawable.roundedbuttongrey));
                                        followfullscreen.setTextColor(ContextCompat.getColor(context,R.color.white));
                                        followfullscreen.setTransformationMethod(null);
                                        followfullscreen.setTypeface(type);

                                        postData.get(selected).setIs_mutual(false);
                                        postData.get(selected).setHe_follow(false);
                                        postData.get(selected).setMe_follow(false);
                                    }
                                }else{
                                    Toast.makeText(context, response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(DialogInterface.BUTTON_POSITIVE==which){
                                String url = ServerInfo.BASE_URL_FOLLOWER_API+"Unfollowing/"+postData.get(selectedPosition).getPoster().getUserId();
                                AsyncHttpClient client=new AsyncHttpClient();
                                try {
                                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                    trustStore.load(null, null);
                                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                    client.setSSLSocketFactory(sf);
                                }
                                catch (Exception e) {}
                                MyUser myUser=MyUser.getmUser();
                                client.addHeader("Authorization", "Bearer " + myUser.getToken());
                                RequestParams requestParams=new RequestParams();

                                client.post(context, url,requestParams,new JsonHttpResponseHandler(){


                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            System.out.println("1. "+response.toString());
                                            if(response.getJSONObject("resultPayload").getBoolean("result")) {
                                                if (response.getJSONObject("resultPayload").getBoolean("is_mutual")) {
                                                    followfullscreen.setText("Mutual");
                                                    followfullscreen.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedbuttongrey));
                                                    followfullscreen.setTextColor(ContextCompat.getColor(context, R.color.white));
                                                    followfullscreen.setTransformationMethod(null);
                                                    followfullscreen.setTypeface(type);

                                                    postData.get(selected).setIs_mutual(true);
                                                    postData.get(selected).setHe_follow(true);
                                                    postData.get(selected).setMe_follow(true);

                                                } else if (response.getJSONObject("resultPayload").getBoolean("me_follow")) {
                                                    followfullscreen.setText("Following");
                                                    followfullscreen.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedbuttongrey));
                                                    followfullscreen.setTextColor(ContextCompat.getColor(context, R.color.white));
                                                    followfullscreen.setTransformationMethod(null);
                                                    followfullscreen.setTypeface(type);

                                                    postData.get(selected).setIs_mutual(false);
                                                    postData.get(selected).setHe_follow(false);
                                                    postData.get(selected).setMe_follow(true);

                                                } else if (response.getJSONObject("resultPayload").getBoolean("he_follow")) {
                                                    followfullscreen.setText("follower");
                                                    followfullscreen.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedbuttongrey));
                                                    followfullscreen.setTextColor(ContextCompat.getColor(context, R.color.white));
                                                    followfullscreen.setTransformationMethod(null);
                                                    followfullscreen.setTypeface(type);

                                                    postData.get(selected).setIs_mutual(false);
                                                    postData.get(selected).setHe_follow(true);
                                                    postData.get(selected).setMe_follow(false);
                                                } else {
                                                    followfullscreen.setText("follow");
                                                    followfullscreen.setBackground(ContextCompat.getDrawable(context, R.drawable.roundedbuttongrey));
                                                    followfullscreen.setTextColor(ContextCompat.getColor(context, R.color.white));
                                                    followfullscreen.setTransformationMethod(null);
                                                    followfullscreen.setTypeface(type);

                                                    postData.get(selected).setIs_mutual(false);
                                                    postData.get(selected).setHe_follow(false);
                                                    postData.get(selected).setMe_follow(false);
                                                }
                                            }else{
                                                Toast.makeText(context, response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                dialog.dismiss();
                            }

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Unfollow", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();

                }
            }

        });
        



        clearRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Delete Call "+selectedPosition);
                String url = ServerInfo.BASE_URL_API+"images/rating";

                AsyncHttpClient client=new AsyncHttpClient();
                client.addHeader("Authorization", "Bearer " + mUser.getToken());
                System.out.println("Token "+mUser.getToken());
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
                params.add("PhotoId",postData.get(selectedPosition).getId()+"");

                final HashMap<Integer,Integer> hashMap=new HashMap<Integer, Integer>(); //use for hold selected position
                hashMap.put(postData.get(selectedPosition).getId(),selectedPosition);


                client.post(context, url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("1. "+response.toString());
                            try {
                                int picId=response.getJSONObject("resultPayload").getInt("picId");
                                postData.get(hashMap.get(picId)).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                                postData.get(hashMap.get(picId)).setMyStarCount(0);

                                if (hashMap.get(picId)==selectedPosition) {
                                    ratingcountfullscreen.setText(response.getJSONObject("resultPayload").getInt("totalRating")+"");
                                    ratingcountfullscreen.setText(postData.get(hashMap.get(picId)).getStarsCount()+"");
                                    ratingfullscreen.setRating((float)(postData.get(hashMap.get(picId)).getMyStarCount()));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            postDataAdapter.notifyDataSetChanged();
                            System.out.println("Loading complete");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("2 "+responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        System.out.println("3 "+errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        System.out.println("4 "+errorResponse.toString());
                    }

                });
            }
        });





        return dialog;
    }

    public void SwitchCaption(){
        if(captionContainer.getVisibility()== View.VISIBLE){
            captionContainer.setVisibility(View.GONE);
        }else{
            captionContainer.setVisibility(View.VISIBLE);
        }
    }
    public void changeRating(final RatingBar ratingBar, final int position){
        System.out.println("Change Call "+position);

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
        params.add("PhotoId",postData.get(position).getId()+"");

        final HashMap<Integer,Integer> hashMap=new HashMap<Integer, Integer>(); //use for hold selected position
        hashMap.put(postData.get(position).getId(),position);

        client.post(context, url,params,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    try {
                        int picId=response.getJSONObject("resultPayload").getInt("picId");
                        postData.get(hashMap.get(picId)).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                        postData.get(hashMap.get(picId)).setMyStarCount(response.getJSONObject("resultPayload").getInt("userRating"));

                        if (hashMap.get(picId)==selectedPosition) {
                            ratingcountfullscreen.setText(response.getJSONObject("resultPayload").getInt("totalRating")+"");
                            ratingcountfullscreen.setText(postData.get(hashMap.get(picId)).getStarsCount()+"");
                            ratingfullscreen.setRating(response.getJSONObject("resultPayload").getInt("userRating"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postDataAdapter.notifyDataSetChanged();
                    System.out.println("Loading complete");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("2 "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println("3 "+errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                System.out.println("4 "+errorResponse.toString());
            }

        });
    }

    public void setContent(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}