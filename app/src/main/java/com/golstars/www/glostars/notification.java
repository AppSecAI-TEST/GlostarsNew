package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Notification;
import com.golstars.www.glostars.adapters.NotificationAdapter;
import com.golstars.www.glostars.adapters.PostData;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.NotificationService;
import com.golstars.www.glostars.network.PictureService;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class notification extends AppCompatActivity implements OnItemClickListener,AdapterInfomation {


    //===========================FABS=========================================

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;


    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;
    com.github.clans.fab.FloatingActionButton homeFAB;

    FloatingActionMenu menuDown;


    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    //===================================================================

    RelativeLayout notirelative;

    ImageView propic;
    ImageView postpreview;

    TextView name;
    TextView surname;
    TextView notibanner;
    TextView timebanner;
    TextView minsbanner;
    TextView hoursbanner;

    Button notification;
    Button followers;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;
    String tempToken;

    RecyclerView noti;
    RecyclerView foll;

    List<NotificationObj> notifs;
    List<NotificationObj> follNotifs;
    NotificationAdapter mAdapter;
    NotificationAdapter follAdapter;
    Integer unseenNotifs = 0;

    MyUser mUser;
    Intent homeIntent;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        notirelative = (RelativeLayout)findViewById(R.id.notimodelRL);

        propic = (ImageView) findViewById(R.id.propicNOTI);
        postpreview = (ImageView)findViewById(R.id.postpreviewNOTI);

        name = (TextView)findViewById(R.id.nameNOTI);
        surname = (TextView)findViewById(R.id.surnameNOTI);
        notibanner = (TextView)findViewById(R.id.notiBanner);
        timebanner = (TextView)findViewById(R.id.timeNOTI);
        minsbanner = (TextView)findViewById(R.id.minbannerNOTI);
        hoursbanner = (TextView)findViewById(R.id.hourbannerNOTI);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);

        notification = (Button)findViewById(R.id.notificationbut);
        followers = (Button)findViewById(R.id.followersbut);

        noti = (RecyclerView)findViewById(R.id.notificationRecycler);
        foll = (RecyclerView)findViewById(R.id.followersRecycler);

        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);

        //=============Notification Badges===============================================
//        homebadge = (TextView)findViewById(R.id.homebadge);
//        notificationbadge = (TextView)findViewById(R.id.notificationbadge);
//        profilebadge = (TextView)findViewById(R.id.profilebadge);
//        camerabadge = (TextView)findViewById(R.id.uploadbadge);
//        mainbadge = (TextView)findViewById(R.id.mainbadge);
//        competitionbadge = (TextView)findViewById(R.id.competitionbadge);


        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");

        notification.setTypeface(type);
        followers.setTypeface(type);


        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notification.this,notification.class));
            }
        });

        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notification.this,searchResults.class));

            }
        });

        notification.setTransformationMethod(null);
        followers.setTransformationMethod(null);




        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(homeIntent);
                menuDown.close(true);
            }
        });


        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notification.this,notification.class));
            }
        });

        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notification.this, upload.class));
                menuDown.close(true);
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notification.this, competitionAll.class));
                menuDown.close(true);
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuDown.close(true);
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                followers.setTextColor(getResources().getColor(R.color.darkGrey));

                noti.setVisibility(View.VISIBLE);
                foll.setVisibility(View.GONE);

            }
        });


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.setTextColor(getResources().getColor(R.color.darkGrey));

                followers.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                noti.setVisibility(View.GONE);
                foll.setVisibility(View.VISIBLE);

                setUserNotifSeen();

            }
        });



        //============================== NETWORK SERVICE HANDLING ========================================
        notifs = new ArrayList<>();
        follNotifs = new ArrayList<>();

        //mUser = MyUser.getmUser(this);

        mAdapter = new NotificationAdapter(notifs, this, this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", notif.getOriginID());
                intent.setClass(getApplicationContext(),user_profile.class);
                startActivity(intent);
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        noti.setLayoutManager(layoutManager);
        noti.setItemAnimator(new DefaultItemAnimator());
        noti.setAdapter(mAdapter);

        follAdapter = new NotificationAdapter(follNotifs, this, this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", notif.getOriginID());
                intent.setClass(getApplicationContext(),user_profile.class);
                startActivity(intent);
            }
        });


        RecyclerView.LayoutManager layoutM = new LinearLayoutManager(getApplicationContext());
        foll.setLayoutManager(layoutM);
        foll.setItemAnimator(new DefaultItemAnimator());
        foll.setAdapter(follAdapter);



//        RecyclerView noti;
//        RecyclerView foll;
        new getUserData().execute("");
//
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
                // request.addHeader("authorization","bearer rLQd1-q-5CdheRQ5l5envaEZfdTWpPX4RIzvTDelURIw6ITegpEbD1U6XZrVrYcODUGYA8pH16vc4MXA_XHONtvJDkCEQahDDksw-oxBENuZH4k0F7vm0rtgdoRm89xwqSlu119JA2BBuHTg1apVo9vv8YSN3ke7SAR8Hzz_QFJ3m4tu-PFlatsrANLdRQAWxxuMYlPUSMG_Crfd46JJVa-h9Yvgz0pPs2oYDFsOJqp54wUsFLPOhnSGD-kp2rOvm16kOx9Uz3qxBai_pYYPmbzvr_e5d-pvRxGqQFMVtXr2wl8Ar-2_eUjqwCMDNmh3AMEF5s7lUOxSn9q3c59Qaf7cSd6KWfop9pclbMqJFQITwK9bXe_5V676_r1cHwEdY-nf97gM8t0TuGCxmJlV2RvgRx1oYMHpeS1NNZcHVITu2bpyP1eoE-9lrx80-Sd8gRGYeAC0QwpNHG8BQRbSmv3D0B683f1Z_r1EkgTjwGs");
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
        /*hub.on("updatePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println(o);



                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());
                        for (int i = 0; i < postList.size(); i++) {
                            System.out.println(postList.get(i).getId()+"--"+hashtag.getId());
                            if(postList.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");
                                postList.set(i,hashtag);
                                mAdapter.notifyDataSetChanged();



                                break;
                            }
                        }

                    }
                });

            }
        },String.class);*/


        hub.on("picNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("picNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                           /* JSONObject singleNotif = new JSONObject(o);
                            String description = singleNotif.getString("description");
                            String profilePicURL = singleNotif.getString("profilePicURL");
                            String name = singleNotif.getString("name");
                            String id = singleNotif.getString("id");
                            String usrId = singleNotif.getString("userId");
                            String originatedById = singleNotif.getString("originatedById");
                            String pictureId = singleNotif.getString("pictureId");
                            Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                            Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                            String Seen = singleNotif.getString("seen");
                            String picURL = singleNotif.getString("picUrl");

                            String date = singleNotif.getString("date");
                            *//*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);*//*
                            String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));*/
                            System.out.println("Enter to thread...");
                            Gson gson=new Gson();
                            Notification notification=gson.fromJson(o,Notification.class);

                            NotificationObj notif = new NotificationObj(notification.originatedById+"", notification.pictureId+"", notification.description,
                                    notification.name, notification.profilePicURL, notification.picUrl, notification.seen, notification.checked);
                            notif.setDate(notification.date);
                            notifs.add(0,notif);
                            System.out.println("After Added notification "+notif.toString());
                            mAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            //System.out.println("Exception in pic notification");
                            e.printStackTrace();
                        }
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


                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notification.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notification.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        try {
                            JSONObject singleNotif = new JSONObject(o);
                            String description = "started following you";
                            String profilePicURL = singleNotif.getString("profilePicURL");
                            String name = singleNotif.getString("name");
                            String usrId = singleNotif.getString("userId");
                            String originatedById = singleNotif.getString("originatedById");
                            Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                            Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                            String Seen = singleNotif.getString("seen");

                            String date = singleNotif.getString("date");
                            /*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));*/
                            String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));

                            NotificationObj notif = new NotificationObj(originatedById, null, description, name, profilePicURL, null, seen, checked);
                            notif.setDate(date);
                            follNotifs.add(0,notif);
                            System.out.println("After Added notification "+notif.toString());
                            follAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            System.out.println("Exception in follower notification");
                        }


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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
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

    ArrayList<Hashtag> hashtags=new ArrayList<Hashtag>();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    int Width = displayMetrics.widthPixels;
    private PostData postDataAdapter=new PostData(hashtags, notification.this,Width,getSupportFragmentManager());
    @Override
    public ArrayList<Hashtag> getAllData() {
        return this.hashtags;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this.postDataAdapter;
    }

    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
//            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
            tempToken = mUser.getToken();
            try {
                populateNotificationsList(mUser.getUserId(), mUser.getToken());
                setActivityNotifSeen();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);

            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }
        }
    }

    private void setActivityNotifSeen(){

        StringEntity entity = null;
        try {
            entity = new StringEntity("{}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationService.activityNotifSeen(getApplicationContext(), mUser.getToken(),entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
        } );
    }


    private void setUserNotifSeen(){
        StringEntity entity = null;
        try {
            entity = new StringEntity("{}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationService.userNotifsSeen(getApplicationContext(), mUser.getToken(), entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
        });

    }


    private void populateNotificationsList(String userId, String token) throws JSONException {

        NotificationService.getNotifications(this,userId, token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject data = response.getJSONObject("resultPayload");
                    System.out.println(response);
                    JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                    JSONArray followerNotifications = data.getJSONArray("followerNotifications");
                    System.out.println(activityNotifications);
                    System.out.println(followerNotifications);

                    for(int i = 0; i < activityNotifications.length(); ++i){
                        JSONObject singleNotif = activityNotifications.getJSONObject(i);
                        String description = singleNotif.getString("description");
                        String profilePicURL = singleNotif.getString("profilePicURL");
                        String name = singleNotif.getString("name");
                        String id = singleNotif.getString("id");
                        String usrId = singleNotif.getString("userId");
                        String originatedById = singleNotif.getString("originatedById");
                        String pictureId = singleNotif.getString("pictureId");
                        Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                        Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                        String Seen = singleNotif.getString("seen");
                        String picURL = singleNotif.getString("picUrl");

                        String date = singleNotif.getString("date");
                            /*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);*/
                        String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));

                        if (Seen.equals("false")){
                            unseenNotifs ++;
                        }

                        setActivityNotifsAdapter(description, profilePicURL, name, id, usrId, originatedById, pictureId, seen, interval, picURL, checked);
                    }

                    for(int i = 0; i < followerNotifications.length(); ++i){
                        JSONObject singleNotif = followerNotifications.getJSONObject(i);
                        String description = "started following you";
                        String profilePicURL = singleNotif.getString("profilePicURL");
                        String name = singleNotif.getString("name");
                        String usrId = singleNotif.getString("userId");
                        String originatedById = singleNotif.getString("originatedById");
                        Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                        Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                        String Seen = singleNotif.getString("seen");

                        String date = singleNotif.getString("date");
                            /*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));*/
                        String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));

                        if (Seen.equals("false")){
                            unseenNotifs ++;
                        }

                        //setActivityNotifSeen();

                        setFollowerNotifsAdapter(description, profilePicURL, name, "", usrId, originatedById, null, seen, interval, checked);
                    }

                    if(unseenNotifs > 0){
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            }




        });

        /*NotificationService notif = new NotificationService();
        JSONObject data = null;
        try {
            notif.getNotifications(userId, token);
            while (data == null){
                data = notif.getDataObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/









    }

    private void setFollowerNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, Boolean checked) {
        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, null, seen, checked);
        notif.setDate(date);
        follNotifs.add(notif);
        follAdapter.notifyDataSetChanged();
    }

    private void setActivityNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, String picURL, Boolean checked) {
        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, picURL, seen, checked);
        notif.setDate(date);
        notifs.add(notif);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickPost(Post item) {

    }

    @Override
    public void onItemClickNotif(NotificationObj notif) {
        if(!(notif.getDescription() == "started following you")){

            System.out.println("PIC NOTIFICATION TO OPEN: " + notif.getPicID());
            System.out.println("USER TOKEN: " + tempToken); // im using this tempToken string because there was some error with tokens within this class
            PictureService.getSinglePic(tempToken, notif.getPicID(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println("SINGLE PICTURE SERVICE");
                    System.out.println(response);
                    ArrayList<Post> gridImages = new ArrayList();

                    JSONObject pic = null;
                    Hashtag hashtag = null;
                    try {
                        pic = response.getJSONObject("resultPayload");

                        Gson gson=new Gson();
                        hashtag=gson.fromJson(pic.toString(),Hashtag.class);


                        JSONObject poster = pic.getJSONObject("poster");
                        String name = poster.getString("name");
                        String usrId = poster.getString("userId");
                        String profilePicUrl = poster.getString("profilePicURL");
                        String id = pic.getString("id");
                        String description = pic.getString("description");
                        String privacy = pic.getString("privacy");
                        String picURL = pic.getString("picUrl");

                        Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
                        Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
                        Integer starsCount = Integer.parseInt(pic.getString("starsCount"));

                        JSONArray ratings = pic.getJSONArray("ratings");
                        JSONArray comments = pic.getJSONArray("comments");

                        String uploaded = pic.getString("uploaded");
                        //String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                        //LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));
                        //String interval = Timestamp.getInterval(localDateTime);

                        Post post = new Post(name,usrId,id, description,picURL,profilePicUrl, isFeatured, isCompeting, starsCount,comments.length());
                        post.setUploaded(uploaded);
                        post.setRatings(ratings);
                        post.setComments(comments);
                        post.setPrivacy(privacy);


                        gridImages.add(0, post);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    Bundle bundle = new Bundle();
                    bundle.putInt("position",0);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());


                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
                    newFragment.setArguments(bundle);

                    hashtags.clear();

                    hashtags.add(hashtag);
                    postDataAdapter.notifyDataSetChanged();

                    newFragment.show(ft, "slideshow");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println("FAILURE AT SINGLE PIC SERVICE");
                    System.out.println(errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("FAILURE AT SINGLE PIC SERVICE");
                    System.out.println(responseString);
                }
            });





            /*
            Intent intent = new Intent();
            intent.putExtra("IMAGE_SAUCE",notif.getPicURL());
            intent.putExtra("IMAGE_AUTHOR", mUser.getName());
            intent.putExtra("IMAGE_CAPTION","");
            intent.setClass(getApplicationContext(), imagefullscreen.class);
            startActivity(intent); */
        }
    }
}
