package com.golstars.www.glostars;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.CommentAdapter;
import com.golstars.www.glostars.adapters.PostData;
import com.golstars.www.glostars.models.Comment;
import com.golstars.www.glostars.network.NotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import io.github.rockerhieu.emojiconize.Emojiconize;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;


public class MainFeed extends AppCompatActivity  implements AdapterInfomation  {


    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;


    RelativeLayout postRelative;

    TextView username;
    TextView caption;
    TextView postTime;
    TextView totalStars;
    TextView totalComments;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    ImageView propic;
    ImageView post;
    ImageView privacyIcon;

    com.github.clans.fab.FloatingActionButton homeFAB;
    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;

    FloatingActionMenu menuDown;


    View rootView;

    EmojIconActions emojIcon;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;

    boolean isOpen = false;

    // --------------- recycler view settings ---------
    private ArrayList<Hashtag> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostData mAdapter;
    //-------------------------------------------------
    LinearLayoutManager layoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private MyUser mUser;
    int pg = 1;
    Integer unseenNotifs = 0;
    //-------------------------------------------------
    Intent userProfileIntent = new Intent();
    PullRefreshLayout layout;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Emojiconize.activity(this).go();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootView = findViewById(R.id.myRoot);
        layout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList.clear();
                pg=1;
                try {
                    //callAsyncPopulate(pg);
                    pg = 1;
                    loading=false;
                    if(mUser == null){
                        new getUserData().execute("");
                    }else{
                        loadFeed();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final String TAG = MainFeed.class.getSimpleName();



        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        //---------------NETOWORK AND RECYCLER VIEW --------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.mainfeedrecycler);




        //getUserData("");
        //mUser.setContext(this);




        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int Height = displayMetrics.heightPixels;
        int Width = displayMetrics.widthPixels;

        layoutManager = new LinearLayoutManager(this);

        mAdapter = new PostData(postList,MainFeed.this,Width,getSupportFragmentManager());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        if(mUser == null){
            new getUserData().execute("");
        }else{
            loadFeed();
        }


        //populateFeed(mUser.getUserId(), pg, mUser.getToken());

        /* checks whether the user has reached the end of the view
           and calls another page
        * */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                System.out.println("Scrolling "+dx+" "+dy);
                if(dy > 0){ //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    System.out.println("Total Item "+totalItemCount+" Loading "+loading);
                    if(!loading){
                        if((visibleItemCount + pastVisiblesItems) >= totalItemCount-2){
                            loading = true;
                            //pg++;
                            try {
                                //callAsyncPopulate(pg);
                                if(mUser == null){
                                    new getUserData().execute("");
                                }else{
                                    loadFeed();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //populateFeed(mUser.getUserId(), pg, mUser.getToken());
                        }
                    }
                }
            }
        });




        //--------------------------------------------------------------------------


        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);



        username=(TextView)findViewById(R.id.userNAME);
        caption=(TextView)findViewById(R.id.userCAPTION);
        postTime=(TextView)findViewById(R.id.uploadTIME);
        post = (ImageView)findViewById(R.id.userPOST);
        propic = (ImageView)findViewById(R.id.userPIC);
        privacyIcon = (ImageView)findViewById(R.id.privacy);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);

        postRelative =(RelativeLayout)findViewById(R.id.postRelative);

        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this,searchResults.class));

            }
        });

//        gl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this,introductory.class));
                menuDown.close(true);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this, upload.class));
                menuDown.close(true);
            }
        });


        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(userProfileIntent);
                menuDown.close(true);
            }
        });

        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this, notificationNew.class));
                menuDown.close(true);
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this, competition_page.class));
                menuDown.close(true);

            }
        });

        //CHECK IS THE PHONE CONNECTED TO THE INTERNET
//        if(!isConnected()){
//            startActivity(new Intent(this, noInternet.class));
//        }



        LoadServer();


    }

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
                        for (int i = 0; i < postList.size(); i++) {
                            System.out.println(postList.get(i).getId()+"--"+hashtag.getId());
                            if(postList.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");

                                for (Rating rating:hashtag.getRatings()
                                        ) {
                                    if(rating.getRaterId().equalsIgnoreCase(postList.get(i).getPoster().getUserId())){
                                        hashtag.setMyStarCount(rating.getStarsCount());
                                        break;
                                    }
                                }

                                List<com.golstars.www.glostars.ModelData.Comment> comments=postList.get(i).getComments();
                                comments.clear();
                                for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                        ) {
                                    comments.add(comment);
                                }
                                hashtag.setComments(comments);
                                if(mAdapter.commentData!=null){
                                    mAdapter.commentData.notifyDataSetChanged();
                                }
                                postList.set(i,hashtag);
                                mAdapter.notifyDataSetChanged();
                                break;
                            }
                        }

                    }
                });

            }
        },String.class);

        hub.on("AddPicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println(o);



                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());
                        postList.add(0,hashtag);
                        mAdapter.notifyDataSetChanged();

                    }
                });

            }
        },String.class);


        hub.on("RemovePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println(o);



                handler.post(new Runnable() {
                    public void run() {

                        int postId=Integer.parseInt(o);

                        for (Hashtag hashtag:postList){
                            if(hashtag.getId()==postId){
                                postList.remove(hashtag);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });

            }
        },String.class);


        hub.on("EditProfile",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("EditProfile "+o);
                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        UserDetails userDetails=gson.fromJson(o,UserDetails.class);

                        for (Hashtag hashtag:postList){
                            if(hashtag.getPoster().getUserId().equalsIgnoreCase(userDetails.id)){
                                hashtag.getPoster().setName(userDetails.name+" "+userDetails.lastName);
                                hashtag.getPoster().setProfilePicURL(userDetails.profilePicURL);
                                Picasso.with(MainFeed.this).invalidate(userDetails.profilePicURL);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        },String.class);


        hub.on("picNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("picNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);





        hub.on("followerNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("followerNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);

        hub.on("SeenPictureNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("SeenPictureNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/


                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);

        hub.on("SeenFollowerNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("SeenFollowerNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);


    }



    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }

    public void loadFeed(){

        String url = ServerInfo.BASE_URL_API+"images/mutualpic/" + mUser.getUserId() + "/" + pg;

        System.out.println(url);

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
        StringEntity stringEntity=null;
        try {
            stringEntity=new StringEntity("{ListPhoto:[]}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(getApplicationContext(), url,stringEntity,"application/json",new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    System.out.println("1. "+response.toString());
                    Gson gson=new Gson();
                    ArrayList<Hashtag> getAllPost=gson.fromJson(response.getJSONObject("resultPayload").getJSONArray("data").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
                    postList.addAll(getAllPost);
                    System.out.println("Total Post "+postList.size());
                    mAdapter.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    layout.setRefreshing(false);
                    System.out.println("Loading complete");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public void getUnseen(){


        NotificationService.getNotifications(getApplicationContext(), mUser.getUserId(), mUser.getToken(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("resultPayload");
                    System.out.println(response);
                    JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                    JSONArray followerNotifications = data.getJSONArray("followerNotifications");
                    System.out.println(activityNotifications);
                    System.out.println(followerNotifications);


                    for(int i = 0; i < activityNotifications.length(); ++i){
                        if(activityNotifications.getJSONObject(i).getString("seen").equals("false")){
                            unseenNotifs++;
                        }
                    }

                    for(int i = 0; i < followerNotifications.length(); ++i){
                        if(followerNotifications.getJSONObject(i).getString("seen").equals("false")){
                            unseenNotifs++;
                        }

                    }

                    if(unseenNotifs > 0){
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(MainFeed.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });




    }




    // Setup a recurring alarm every fifteen minutes
    private void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra("token", mUser.getToken());
        intent.putExtra("usrId", mUser.getUserId());

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    }

    @Override
    public ArrayList<Hashtag> getAllData() {
        return postList;
    }

    @Override
    public PostData getAdapter() {
        return mAdapter;
    }


    private class getUserData extends AsyncTask<String, Integer, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            //userProfileIntent = new Intent();
            userProfileIntent.putExtra("USER_ID",mUser.getUserId());
            userProfileIntent.setClass(getApplicationContext(),user_profile.class);
            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }
            getUnseen();
            //setting up alarm to service
            scheduleAlarm();
            loadFeed();

        }
    }




    void postComment(String picID, final String comment, final ArrayList commentsList, final CommentAdapter commentAdapter, final TextView commentbox) throws Exception{

        if(!comment.isEmpty()){
            //PictureService pictureService = new PictureService();
            //pictureService.commentPicture(picID, comment, mUser.getToken());
            final JSONObject res = null;
            /*while(res == null){
                res = pictureService.getDataObject();
            }*/

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
            msg.add("PhotoId", picID);

            client.post(getApplicationContext(), url,msg,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Comment dummyComment = new Comment(
                            comment,
                            mUser.getName(),
                            mUser.getUserId(),
                            (new Date()).toString(),
                            mUser.getProfilePicURL(),
                            mUser.getName(),
                            "", 0);


                    try {
                        if(response.getInt("responseCode") == 1){
                            commentsList.add(dummyComment);
                            commentAdapter.notifyDataSetChanged();
                            commentbox.setText("");

                        } else Toast.makeText(MainFeed.this, "couldn't connect to the servers", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });



        } else {
            Toast.makeText(this, "write a message before sending", Toast.LENGTH_LONG).show();
        }


    }

    //-------------CAMERA AND GALLERY CALLERS------------------------------------------
    /**
     *  In this method we'll create a dialog box with three options
     *  for either camera, gallery or cancelling actions
     */

    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainFeed.this);
        builder.setTitle("Select Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                boolean result = Utility.checkPermission(MainFeed.this);

                if(items[item].equals("Take Photo")){
                    userChoosenTask = "Take Photo";
                    if(result)
                        cameraIntent();
                } else if(items[item].equals("Choose from Library")){
                    userChoosenTask = "Choose from Library";
                    if(result)
                        galleryIntent();
                } else if(items[item].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,  REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        File imageFile = null;
        if (data != null) {
            try {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if(selectedImagePath!=null){
                    System.out.println(selectedImagePath);
                    imageFile = new File(selectedImagePath);
                }
                else System.out.println("selectedImagePath is null");
                if(filemanagerstring!=null){
                    System.out.println(filemanagerstring);
                    imageFile = new File(filemanagerstring);
                    System.out.println(imageFile);
                }

                else System.out.println("filemanagerstring is null");





                //imageFile = new File(imagePath);
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Bundle filePath = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", bm);
        filePath.putSerializable("FILEPATH", imageFile);
        intent.putExtras(bundle);
        intent.putExtras(filePath);
        intent.setClass(this, upload.class);
        startActivity(intent);

        //ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Bundle filePath = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", thumbnail);
        filePath.putSerializable("FILEPATH", destination);
        intent.putExtras(bundle);
        intent.putExtras(filePath);
        intent.setClass(this, upload.class);
        startActivity(intent);

        //ivImage.setImageBitmap(thumbnail);
    }


    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }








    //-------------/CAMERA AND GALLERY CALLERS<end>------------------------------------------

}