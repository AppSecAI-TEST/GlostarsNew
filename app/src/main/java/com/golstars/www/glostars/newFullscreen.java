package com.golstars.www.glostars;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Poster;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.CommentData;
import com.golstars.www.glostars.models.Comment;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

import static android.content.ContentValues.TAG;

public class newFullscreen extends AppCompatActivity {


    ImageView newFullscreenPost;
    RecyclerView picturepreview;
    TextView fullscreenUsername;
    TextView fullscreenUploadTime;
    ImageView fullscreenPrivacy;
    ImageView fullscreenProflePic;
    Button newFullscreenFollow;
    TextView newFullscreenCaption;
    SimpleRatingBar newRating;
    TextView newRatingCount;
    ImageView newCommentIcon;
    TextView newCommentCount;
    ListView newFullscreenComments;
    EmojiconEditText newCommentArea;
    TextView newSendComment;
    ImageView emojiButton;
    EmojIconActions emojIcon;
    CoordinatorLayout parentLayout;

    Integer position;
    String token;
    String usrID;
    //private ArrayList<Hashtag> postData;
    private Hashtag postData;
    private Poster poster;

    public CommentData commentData;
    RecyclerView.Adapter postDataAdapter;
    MyUser myUser;
    Typeface type;

    List<com.golstars.www.glostars.ModelData.Comment> commentsList;
    ListAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fullscreen);

        newFullscreenPost = (ImageView)findViewById(R.id.newfullscreenimage);             // image url
        picturepreview = (RecyclerView)findViewById(R.id.previewPictures);               // miniature pics
        fullscreenUsername = (TextView)findViewById(R.id.newFullscreenUsername);        // user name
        fullscreenUploadTime = (TextView)findViewById(R.id.newFullscreenUploadTime);   // timestamp
        fullscreenPrivacy = (ImageView)findViewById(R.id.newFullscreenPrivacy);       // privacy img
        fullscreenProflePic = (ImageView)findViewById(R.id.newFullscreenProfilePic); // profile pic
        newFullscreenFollow = (Button)findViewById(R.id.newFullscreenFollowButton); // follow button
        newFullscreenCaption = (TextView)findViewById(R.id.newFullscreenCaption);  // caption
        newRating = (SimpleRatingBar) findViewById(R.id.newFullscreenRating);                          // rating bar
        newRatingCount = (TextView)findViewById(R.id.newFullscreenRatingCount);                // rating count
        newCommentIcon = (ImageView)findViewById(R.id.newFullscreenCommentIcon);              // comment icon
        newCommentCount = (TextView)findViewById(R.id.newFullscreenCommentCount);            // comments count
        newFullscreenComments = (ListView)findViewById(R.id.newFullscreenComments);         // comments
        newCommentArea = (EmojiconEditText) findViewById(R.id.newFullscreenCommentArea);   //
        newSendComment = (TextView)findViewById(R.id.newFullscreenSendComment);
        emojiButton = (ImageView) findViewById(R.id.emojiButton);
        parentLayout = (CoordinatorLayout) findViewById(R.id.rootViewFullscreen);



        type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        fullscreenUsername.setTypeface(type);
        fullscreenUploadTime.setTypeface(type);
        newFullscreenCaption.setTypeface(type);
        newRatingCount.setTypeface(type);
        newCommentCount.setTypeface(type);
        newSendComment.setTypeface(type);
        newCommentArea.setTypeface(type);

        newFullscreenFollow.setTransformationMethod(null);

        token = getIntent().getExtras().getString("token");
        usrID = getIntent().getExtras().getString("usrID");
        //position = getIntent().getExtras().getInt("position");



        Hashtag h=getIntent().getExtras().getParcelable("post");
        Poster p=getIntent().getExtras().getParcelable("poster");;
        setData(h,p);


        //if((p.getUserId() == null) || usrID == null){
        if(!MainFeed.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "couldn't reach servers", Toast.LENGTH_LONG).show();
        }else if(usrID.equals(p.getUserId())){
            newFullscreenFollow.setVisibility(View.GONE);
        }


        emojIcon = new EmojIconActions(newFullscreen.this,parentLayout,newCommentArea,emojiButton);
        emojIcon.ShowEmojIcon();

        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });



        LoadServer();


    }
    Handler handler = new Handler();
    public void LoadServer(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        HubConnection connection = new HubConnection(ServerInfo.BASE_URL);
        HubProxy hub = connection.createHubProxy("GlostarsHub");

        final MyUser me=MyUser.getmUser();
        System.out.println("server Token "+me.getToken());

        Credentials credentials=new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("Authorization", "Bearer " + me.getToken());

            }
        };
        connection.setCredentials(credentials);
        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
        } catch (InterruptedException e) {
            // Handle ...
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       /* try {
            hub.invoke("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        hub.on("updatePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Update Picture "+o);



                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());
                        System.out.println(postData.getId()+"--"+hashtag.getId());
                        if(postData.getId()==hashtag.getId()){
                            System.out.println("Found...");

                            for (Rating rating:hashtag.getRatings()
                                    ) {
                                if(rating.getRaterId().equalsIgnoreCase(poster.getUserId())){
                                    hashtag.setMyStarCount(rating.getStarsCount());
                                    break;
                                }
                            }

                            List<com.golstars.www.glostars.ModelData.Comment> comments=postData.getComments();
                            comments.clear();
                            for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                    ) {
                                comments.add(comment);
                            }
                            hashtag.setComments(comments);
                            commentAdapter.notifyDataSetChanged();
                            postData=hashtag;
                            //postDataAdapter.notifyDataSetChanged();
                            setData(postData,postData.getPoster());
                        }

                    }
                });

            }
        },String.class);



        //<editor-fold desc="Follow Update">
        hub.on("FollowUpdate",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("FollowUpdate "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        FollowInfo followInfo=gson.fromJson(o,FollowInfo.class);

                        if(followInfo.originatedById.equalsIgnoreCase(me.getUserId()) || followInfo.destinationById.equalsIgnoreCase(me.getUserId())){

                            System.out.println("Check with user id "+poster.getUserId());
                            if(poster.getUserId().equalsIgnoreCase(me.getUserId())){
                                return;
                            }else if(followInfo.isMutual){
                                postData.setIs_mutual(true);
                                postData.setHe_follow(true);
                                postData.setMe_follow(true);
                            }else if(poster.getUserId().equalsIgnoreCase(followInfo.originatedById)){
                                if(followInfo.originateFollowDestination){
                                    postData.setIs_mutual(false);
                                    postData.setHe_follow(false);
                                    postData.setMe_follow(true);
                                }else if(followInfo.destinationFollowOriginate){
                                    postData.setIs_mutual(false);
                                    postData.setHe_follow(true);
                                    postData.setMe_follow(false);
                                }else{
                                    postData.setIs_mutual(false);
                                    postData.setHe_follow(false);
                                    postData.setMe_follow(false);
                                }
                            }else if(poster.getUserId().equalsIgnoreCase(followInfo.destinationById)){

                                if(followInfo.originateFollowDestination){
                                    postData.setIs_mutual(false);
                                    postData.setHe_follow(true);
                                    postData.setMe_follow(false);
                                }else if(followInfo.destinationFollowOriginate){
                                    postData.setIs_mutual(false);
                                    postData.setHe_follow(false);
                                    postData.setMe_follow(true);
                                }else{
                                    postData.setIs_mutual(false);
                                    postData.setHe_follow(false);
                                    postData.setMe_follow(false);
                                }
                            }
                            setData(postData,null);
                        }

                    }
                });

            }
        },String.class);
        //</editor-fold>



        hub.on("EditProfile",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("EditProfile "+o);
                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        UserDetails userDetails=gson.fromJson(o,UserDetails.class);

                        if(poster.getUserId().equalsIgnoreCase(userDetails.id)){
                            poster.setName(userDetails.name+" "+userDetails.lastName);
                            poster.setProfilePicURL(userDetails.profilePicURL);
                            Picasso.with(newFullscreen.this).invalidate(userDetails.profilePicURL);
                        }
                        setData(postData,null);
                    }
                });

            }
        },String.class);


    }

    private void setData(Hashtag post,Poster p) {
        //Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");


        //postData=((AdapterInfomation)getApplicationContext()).getAllData(;
        //postDataAdapter=((AdapterInfomation)getApplicationContext()).getAdapter();
        //new getUserData().execute("");

        /*if(postData == null)
            postData =  getIntent().getExtras().getParcelable("post");
        poster = getIntent().getExtras().getParcelable("poster");*/
        this.postData=post;
        if(p!=null)
            this.poster=p;

        picturepreview.setVisibility(View.GONE);

        commentsList = postData.getComments();
        commentAdapter = new ListAdapter(this, R.layout.content_comment_model, commentsList, new BtnClickListener(){
            @Override
            public void onItemClick(com.golstars.www.glostars.ModelData.Comment com) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", com.getCommenterId());
                intent.setClass(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }

        });

        String totalComments = String.valueOf(postData.getComments().size());

        //newFullscreenComments.setAdapter(commentAdapter);
        newCommentCount.setText(totalComments);


        fullscreenUsername.setText(poster.getName());
        fullscreenUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), user_profile.class);
                intent.putExtra("USER_ID",poster.getUserId());

                getApplicationContext().startActivity(intent);
            }
        });

        fullscreenUploadTime.setText(postData.getUploaded());   // timestamp

        Picasso.with(getApplicationContext()).load(poster.getProfilePicURL()).into(fullscreenProflePic);  // profile pic
        fullscreenProflePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), user_profile.class);
                intent.putExtra("USER_ID",poster.getUserId());

                getApplicationContext().startActivity(intent);
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = width * newFullscreenPost.getMaxHeight() / newFullscreenPost.getMaxWidth();

//        Picasso
//                .with(getApplicationContext())
//                .load(postData.getPicUrl())
//                .placeholder(R.drawable.loading)
//                .resize(width,1000)
//                .centerInside()
//                //.fit()
//                .into(newFullscreenPost); // image url


        Glide
                .with(getApplicationContext())
                .load(postData.getPicUrl())
                .thumbnail(0.5f)
                //.crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(newFullscreenPost);
        //collection.addView(layout);

        newFullscreenCaption.setText(postData.getDescription());  // caption

        /**************** rating bar settings *************************************/
        if(!postData.isIsCompeting()){
            newRating.setNumberOfStars(1);
        }
        else newRating.setNumberOfStars(5);
        newRating.setRating((float)postData.getMyStarCount());
        newRatingCount.setText(String.valueOf(postData.getStarsCount()));

         /*changed here */
        newRating.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final SimpleRatingBar ratingBar, float v, boolean b) {
                System.out.println("Rating change");
            }
        });

        newRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = newRating.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    postData.setMyStarCount(stars);
                    newRating.setRating((float)stars);
                    //postData.setMyStarCount(stars);
                    System.out.println("Call rating touching... - ");
                    //needChange=true;
                    v.setPressed(false);
                    changeRating(newRating);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});
        /*************************************************************************/



        /**************** privacy icon settings ***********************************/
        if(postData.isIsCompeting()){
            fullscreenPrivacy.setImageResource(R.drawable.privacy_competition_photo);
        } else {
            if(postData.getPrivacy().equals("public")) {
                fullscreenPrivacy.setImageResource(R.drawable.privacy_public_photo);
            } else if (postData.getPrivacy().equals("followers") | postData.getPrivacy().equals("friends")) {
                fullscreenPrivacy.setImageResource(R.drawable.privacy_mutual_follower_photo);
            }
        }
        /*************************************************************************/

        /**************** follow button settings ***********************************/

        if(postData.is_mutual()){
            newFullscreenFollow.setText("Mutual");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }else if(postData.isMe_follow()){
            newFullscreenFollow.setText("Following");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }else if(postData.isHe_follow()){
            newFullscreenFollow.setText("Follower");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }else{
            newFullscreenFollow.setText("Follow");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }

        newFullscreenFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final Integer selected=new Integer(position);


                if(newFullscreenFollow.getText().toString().equalsIgnoreCase("follower") || newFullscreenFollow.getText().toString().equalsIgnoreCase("follow")){
                    String url = ServerInfo.BASE_URL_FOLLOWER_API+"Following/"+poster.getUserId();
                    AsyncHttpClient client=new AsyncHttpClient();
                    try {
                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);
                        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                        client.setSSLSocketFactory(sf);
                    }
                    catch (Exception e) {}
                    //MyUser myUser=MyUser.getmUser();
                    client.addHeader("Authorization", "Bearer " + token);
                    RequestParams requestParams=new RequestParams();

                    client.post(getApplicationContext(), url,requestParams,new JsonHttpResponseHandler(){


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                System.out.println("1. "+response.toString());
                                if(response.getJSONObject("resultPayload").getBoolean("result")){
                                    if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                        newFullscreenFollow.setText("Mutual");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(true);
                                        postData.setHe_follow(true);
                                        postData.setMe_follow(true);

                                    }
                                    else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                        newFullscreenFollow.setText("Following");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(false);
                                        postData.setHe_follow(false);
                                        postData.setMe_follow(true);

                                    }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                        newFullscreenFollow.setText("Follower");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(false);
                                        postData.setHe_follow(true);
                                        postData.setMe_follow(false);
                                    }else{
                                        newFullscreenFollow.setText("Follow");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(false);
                                        postData.setHe_follow(false);
                                        postData.setMe_follow(false);
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
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
                                String url = ServerInfo.BASE_URL_FOLLOWER_API+"Unfollowing/"+poster.getUserId();
                                AsyncHttpClient client=new AsyncHttpClient();
                                try {
                                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                    trustStore.load(null, null);
                                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                    client.setSSLSocketFactory(sf);
                                }
                                catch (Exception e) {}
                                // MyUser myUser=MyUser.getmUser();
                                client.addHeader("Authorization", "Bearer " + token);
                                RequestParams requestParams=new RequestParams();

                                client.post(getApplicationContext(), url,requestParams,new JsonHttpResponseHandler(){


                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            System.out.println("1. "+response.toString());
                                            if(response.getJSONObject("resultPayload").getBoolean("result")) {
                                                if (response.getJSONObject("resultPayload").getBoolean("is_mutual")) {
                                                    newFullscreenFollow.setText("Mutual");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(true);
                                                    postData.setHe_follow(true);
                                                    postData.setMe_follow(true);

                                                } else if (response.getJSONObject("resultPayload").getBoolean("me_follow")) {
                                                    newFullscreenFollow.setText("Following");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(false);
                                                    postData.setHe_follow(false);
                                                    postData.setMe_follow(true);

                                                } else if (response.getJSONObject("resultPayload").getBoolean("he_follow")) {
                                                    newFullscreenFollow.setText("Follower");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(false);
                                                    postData.setHe_follow(true);
                                                    postData.setMe_follow(false);
                                                } else {
                                                    newFullscreenFollow.setText("Follow");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(false);
                                                    postData.setHe_follow(false);
                                                    postData.setMe_follow(false);
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(newFullscreen.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Unfollow", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();

                }
            }

        });
        /*************************************************************************/

        /**************** comments settings ***********************************/

        final List<com.golstars.www.glostars.ModelData.Comment> listAllComment=postData.getComments();
        Collections.reverse(listAllComment);
                /*final CommentData */ commentData=new CommentData(getApplicationContext(),listAllComment);
        newFullscreenComments.setAdapter(commentData);

//        emojIcon.setUseSystemEmoji(true);
        newCommentArea.setUseSystemDefault(true);

//        emojIcon.ShowEmojIcon();

        newFullscreenComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int p, long id) {

                if(!usrID.equalsIgnoreCase(poster.getUserId())){
                    return false;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                builder.setTitle("Delete Comment");
                builder.setMessage("Are you sure want to delete this comment?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();



                        final ProgressDialog progressDialog = ProgressDialog.show(getApplicationContext(), "",
                                "Comment deleting. Please wait...", true);
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.show();

                        String url = ServerInfo.BASE_URL + "api/images/DeleteComment?commentId="+listAllComment.get(p).getCommentId();
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
                        client.addHeader("Authorization", "Bearer " + token);
                        final Integer integer=new Integer(p);


                        client.get(getApplicationContext(), url,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    if(response.getJSONObject("resultPayload").getBoolean("res")){
                                        Toast.makeText(getApplicationContext(), "Successfully delete comment", Toast.LENGTH_SHORT).show();

                                        listAllComment.remove(integer.intValue());



                                        postData.setComments(listAllComment);
                                        commentData.notifyDataSetChanged();
                                        //notifyDataSetChanged();


                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                progressDialog.dismiss();
                            }

                        });
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // I do not need any action here you might
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



                return true;
            }
        });


        newSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final RelativeLayout root = new RelativeLayout(getApplicationContext());
                //root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //final Dialog dialog = new Dialog(getApplicationContext());
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setContentView(root);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                //dialog.getWindow().setContentView(R.layout.commentdialog);
                //final ListView commentrecycler = (ListView) dialog.findViewById(R.id.commentrecycler);
                //final EmojiconEditText commentbox = (EmojiconEditText) dialog.findViewById(R.id.commentBox);
                //final TextView sendcomment = (TextView) dialog.findViewById(R.id.sendcomment);


                final List<com.golstars.www.glostars.ModelData.Comment> listAllComment=postData.getComments();
                /*final CommentData */ commentData=new CommentData(getApplicationContext(),listAllComment);
                newFullscreenComments.setAdapter(commentData);

                //commentrecycler.setAdapter(commentData);
                commentData.notifyDataSetChanged();






                final String comment = String.valueOf(newCommentArea.getText());

                if(comment.equals("") || comment == null){
                    Toast.makeText(getApplicationContext(), "Please enter a comment", Toast.LENGTH_SHORT).show();

                }else {
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
                        client.addHeader("Authorization", "Bearer " + token);
                        msg.add("CommentText", comment);
                        msg.add("PhotoId", postData.getId()+"");

                        client.post(getApplicationContext(), url,msg,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    JSONObject comment=response.getJSONObject("resultPayload");
                                    com.golstars.www.glostars.ModelData.Comment c=new com.golstars.www.glostars.ModelData.Comment(comment.getInt("commentId"),comment.getString("commentMessage"),comment.getString("commenterUserName"),comment.getString("commentUserNameId"),comment.getString("commentTime"),comment.getString("profilePicUrl"),comment.getString("firstName"),comment.getString("lastName"));
                                    listAllComment.add(c);
                                    commentData.notifyDataSetChanged();
                                    postData.setComments(listAllComment);
                                    //notifyDataSetChanged();
                                    newFullscreenComments.setSelection(listAllComment.size()-1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                newCommentArea.setText("");













                /*
//               //imageView emoji_btn= (ImageView) dialog.findViewById(R.id.emoji_btn);
//
//                ImageView dialogClose= (ImageView) dialog.findViewById(R.id.imageView4);
//
//                dialogClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                EmojIconActions emojIcon = new EmojIconActions(getApplicationContext(), rootView, newCommentArea, emoji_btn);
//
//                emojIcon.setUseSystemEmoji(true);
//                newCommentArea.setUseSystemDefault(true);
//
//                emojIcon.ShowEmojIcon();
////

//                emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
                    @Override
                    public void onKeyboardOpen() {
                        Log.e(TAG, "Keyboard opened!");
                    }

                    @Override
                    public void onKeyboardClose() {
                        Log.e(TAG, "Keyboard closed");
                    }
                });*/


//
//                sendcomment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        final String comment = String.valueOf(newCommentArea.getText());
//
//                        try {
//                            String url = ServerInfo.BASE_URL + "api/images/comment";
//                            AsyncHttpClient client=new AsyncHttpClient();
//                            try {
//                                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                                trustStore.load(null, null);
//                                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//                                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                                client.setSSLSocketFactory(sf);
//                            }
//                            catch (Exception e) {}
//                            RequestParams msg=new RequestParams();
//                            client.addHeader("Authorization", "Bearer " + mUser.getToken());
//                            msg.add("CommentText", comment);
//                            msg.add("PhotoId", postData.getId()+"");
//
//                            client.post(getApplicationContext(), url,msg,new JsonHttpResponseHandler(){
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                    try {
//                                        JSONObject comment=response.getJSONObject("resultPayload");
//                                        com.golstars.www.glostars.ModelData.Comment c=new com.golstars.www.glostars.ModelData.Comment(comment.getInt("commentId"),comment.getString("commentMessage"),comment.getString("commenterUserName"),comment.getString("commentUserNameId"),comment.getString("commentTime"),comment.getString("profilePicUrl"),comment.getString("firstName"),comment.getString("lastName"));
//                                        listAllComment.add(c);
//                                        commentData.notifyDataSetChanged();
//                                        postData.setComments(listAllComment);
//                                        //notifyDataSetChanged();
//                                        newFullscreenComments.setSelection(listAllComment.size()-1);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        newCommentArea.setText("");
//                    }
//                });

               /* mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                //dialog.show();
            }
        });
        /*************************************************************************/





        //picturepreview = (RecyclerView)findViewById(R.id.previewPictures);               // miniature pics

//        newRating = (RatingBar)findViewById(R.id.newFullscreenRating);                          // rating bar

//        newCommentIcon = (ImageView)findViewById(R.id.newFullscreenCommentIcon);              // comment icon
//        newCommentCount = (TextView)findViewById(R.id.newFullscreenCommentCount);            // comments count
//        newFullscreenComments = (ListView)findViewById(R.id.newFullscreenComments);         // comments




    }


    public class ListAdapter extends ArrayAdapter<com.golstars.www.glostars.ModelData.Comment> {

        //private List<Comment>;
        //public Context context;

        private BtnClickListener mClickListener = null;

        public ListAdapter(Context context, int resource, List<com.golstars.www.glostars.ModelData.Comment> items, BtnClickListener btnClickListener) {
            super(context, resource, items);
            mClickListener = btnClickListener;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.comment_model, null);
            }

            final com.golstars.www.glostars.ModelData.Comment comm = getItem(position);

            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");


            if (comm != null) {
                TextView namecomment = (TextView) v.findViewById(R.id.namecomment);
                ImageView commentPic = (ImageView) v.findViewById(R.id.commentpic);
                EmojiconTextView comment = (EmojiconTextView) v.findViewById(R.id.comment);
                TextView hours = (TextView) v.findViewById(R.id.hourcomment);

                namecomment.setTypeface(type);
                comment.setTypeface(type);
                //hours.setTypeface(type);

                if(comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/male.jpg") || comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/Male.jpg")){
                    commentPic.setImageResource(R.drawable.nopicmalegrey);

                } else if(comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/female.jpg") || comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/Female.jpg")){
                    commentPic.setImageResource(R.drawable.nopicfemalegrey);
                }else{

                    Picasso.with(getApplicationContext()).load(comm.getProfilePicUrl()).into(commentPic);

                }


                //Picasso.with(getApplicationContext()).load(comm.getProfilePicUrl()).into(commentPic);//

                comment.setText(comm.getCommentMessage());
                //hours.setText("2");
                namecomment.setText(comm.getFirstName() + " " + comm.getLastName());
                hours.setText("something");

                commentPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("pic clicked");
                        if (mClickListener != null){
                            mClickListener.onItemClick(comm);
                        }

                        //

                    }
                });

                namecomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("pic clicked");
                        if (mClickListener != null){
                            mClickListener.onItemClick(comm);
                        }
                    }
                });


            }

            return v;
        }

    }

    public interface BtnClickListener{
        void onItemClick(com.golstars.www.glostars.ModelData.Comment com);
    }


    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            myUser = MyUser.getmUser();
            myUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {

            setData(postData,poster);


        }
    }

    //modified function
    public void changeRating(final SimpleRatingBar ratingBar){

        //System.out.println("Change Call "+position);

        String url = ServerInfo.BASE_URL_API+"images/rating";

        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + token);
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {}

        RequestParams params=new RequestParams();
        params.add("NumOfStars",(int)newRating.getRating()+"");
        params.add("PhotoId",postData.getId()+"");
        client.post(getApplicationContext(), url,params,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    try {
                        postData.setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                        postData.setMyStarCount((int)newRating.getRating());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //notifyDataSetChanged();
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
}
