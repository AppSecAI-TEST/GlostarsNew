package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.CompetitionData;
import com.golstars.www.glostars.adapters.PostAdapter;
import com.golstars.www.glostars.network.NotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class competitionAll extends AppCompatActivity implements AdapterInfomation {



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


    Button gallerybut;
    Button tipsbut;
    Button termsbut;
    Button recogbut;



    RecyclerView gallery;
    LinearLayout tips;
    ScrollView terms;
    ScrollView recog;

    TextView please;
    TextView termstex;
    TextView a;
    TextView b;
    TextView c;
    TextView d;
    TextView e;
    TextView f;
    TextView g;
    TextView h;
    TextView i;
    TextView j;
    TextView k;
    TextView ll;
    TextView m;
    TextView n;
    TextView o;
    TextView p;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    // --------------- recycler view settings ---------
    private ArrayList<Hashtag> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    //-------------------------------------------------
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    GridLayoutManager layoutManager;
    MyUser mUser;
    int pg = 1;
    private Intent homeIntent;


    private ArrayList<Hashtag> gridImages;

    private CompetitionData compAdapt;
    private ArrayList<Hashtag> compPicsUrls;

    Integer unseenNotifs = 0;

    private Handler handler=new Handler();
    PullRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gallery = (RecyclerView) findViewById(R.id.gallerygrid);
        tips = (LinearLayout)findViewById(R.id.tipslin);
        terms = (ScrollView)findViewById(R.id.termslin);
        recog = (ScrollView)findViewById(R.id.recoglin);

        gallerybut = (Button)findViewById(R.id.gallerybut);
        tipsbut = (Button)findViewById(R.id.tipsbut);
        termsbut = (Button)findViewById(R.id.termsbut);
        recogbut = (Button)findViewById(R.id.recogbut);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);

        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        please = (TextView)findViewById(R.id.please);
        termstex = (TextView)findViewById(R.id.termstext);
        a = (TextView)findViewById(R.id.a);
        b = (TextView)findViewById(R.id.b);
        c = (TextView)findViewById(R.id.c);
        d = (TextView)findViewById(R.id.d);
        e = (TextView)findViewById(R.id.e);
        f = (TextView)findViewById(R.id.f);
        g = (TextView)findViewById(R.id.g);
        h = (TextView)findViewById(R.id.h);
        i = (TextView)findViewById(R.id.i);
        j = (TextView)findViewById(R.id.j);
        k = (TextView)findViewById(R.id.k);
        ll = (TextView)findViewById(R.id.ll);
        m = (TextView)findViewById(R.id.m);
        n = (TextView)findViewById(R.id.n);
        o = (TextView)findViewById(R.id.o);
        p = (TextView)findViewById(R.id.p);


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



        gallerybut.setTransformationMethod(null);
        tipsbut.setTransformationMethod(null);
        termsbut.setTransformationMethod(null);
        recogbut.setTransformationMethod(null);




        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        gallerybut.setTypeface(type);
        tipsbut.setTypeface(type);
        termsbut.setTypeface(type);
        recogbut.setTypeface(type);
        a.setTypeface(type);
        b.setTypeface(type);
        c.setTypeface(type);
        d.setTypeface(type);
        e.setTypeface(type);
        f.setTypeface(type);
        g.setTypeface(type);
        h.setTypeface(type);
        i.setTypeface(type);
        j.setTypeface(type);
        k.setTypeface(type);
        ll.setTypeface(type);
        m.setTypeface(type);
        n.setTypeface(type);
        o.setTypeface(type);
        p.setTypeface(type);
        please.setTypeface(type);
        termstex.setTypeface(type);



        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(competitionAll.this,MainFeed.class));
            }
        });



        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, searchResults.class));

            }
        });




        //==========================================================================

        gallerybut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallerybut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                gallery.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);

            }
        });


        termsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termsbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                terms.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);
            }
        });



        tipsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipsbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                tips.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);
            }
        });


        recogbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                recogbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));


                recog.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
            }
        });

        //==========================================================================================



        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, notification.class));
                menuDown.close(true);
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuDown.close(true);
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(homeIntent != null){
                    startActivity(homeIntent);
                }

                menuDown.close(true);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, upload.class));
                menuDown.close(true);
            }
        });

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(competitionAll.this,MainFeed.class));
            }
        });


        //---------------NETWORK AND RECYCLER VIEW --------------------------------
        //recyclerView = (RecyclerView) findViewById(R.id.mainfeedrecycler);

        compPicsUrls = new ArrayList<>();

        gridImages = new ArrayList<>();



        compAdapt = new CompetitionData(this, compPicsUrls,getSupportFragmentManager());
        //compAdapt = new RecyclerGridAdapter(this, gridImages, this);

        int numOfColumns = 3;
        layoutManager = new GridLayoutManager(this, numOfColumns);
        gallery.setLayoutManager(layoutManager);
        gallery.setAdapter(compAdapt);

        //new getUserData().execute("");
        /*mUser.setContext(this);
        homeIntent = new Intent();
        homeIntent.putExtra("USER_ID",mUser.getUserId());
        homeIntent.setClass(getApplicationContext(),user_profile.class);*/



        gallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                } else{
                                    load(false);
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
        if(mUser == null){
            new getUserData().execute("");
        } else{
            getUnseen();
            load(false);
        }

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

                        for (int i = 0; i < compPicsUrls.size(); i++) {
                            System.out.println(compPicsUrls.get(i).getId()+"--"+hashtag.getId());
                            if(compPicsUrls.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");
                                for (Rating rating:hashtag.getRatings()
                                        ) {
                                    if(rating.getRaterId().equalsIgnoreCase(compPicsUrls.get(i).getPoster().getUserId())){
                                        hashtag.setMyStarCount(rating.getStarsCount());
                                        break;
                                    }
                                }

                                List<Comment> comments=compPicsUrls.get(i).getComments();
                                comments.clear();
                                for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                        ) {
                                    comments.add(comment);
                                }
                                hashtag.setComments(comments);

                                compPicsUrls.set(i,hashtag);
                                compAdapt.notifyDataSetChanged();
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
                System.out.println("Add Picture "+o);
                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());

                        if(hashtag.isIsCompeting()){
                            compPicsUrls.add(0,hashtag);
                            compAdapt.notifyDataSetChanged();
                        }
                    }
                });

            }
        },String.class);


        hub.on("RemovePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Remove Picture Id "+o);



                handler.post(new Runnable() {
                    public void run() {

                        int postId=Integer.parseInt(o);

                        for (Hashtag hashtag:compPicsUrls){
                            if(hashtag.getId()==postId){
                                compPicsUrls.remove(hashtag);
                                compAdapt.notifyDataSetChanged();
                                return;
                            }
                        }
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

                        for (Hashtag hashtag:compPicsUrls){
                            if(hashtag.getPoster().getUserId().equalsIgnoreCase(userDetails.id)){
                                hashtag.getPoster().setName(userDetails.name+" "+userDetails.lastName);
                                hashtag.getPoster().setProfilePicURL(userDetails.profilePicURL);
                                Picasso.with(competitionAll.this).invalidate(userDetails.profilePicURL);
                            }
                        }
                        compAdapt.notifyDataSetChanged();
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);


        hub.on("FollowUpdate",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("FollowUpdate "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();

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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorAccent));
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/


                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);


    }
    /*
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    } */

    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }


    public void load(boolean fromRecursive){
        if(fromRecursive && !(pg<5))
            return;
        loading=true;
        String url = ServerInfo.BASE_URL_API+"/images/competition/" + pg;

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


        client.get(getApplicationContext(), url,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    System.out.println("1. "+response.toString());
                    Gson gson=new Gson();
                    ArrayList<Hashtag> getAllPost=gson.fromJson(response.getJSONArray("resultPayload").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());




                    compPicsUrls.addAll(getAllPost);
                    System.out.println("Total Post "+postList.size());
                    compAdapt.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    System.out.println("Loading complete");

                    if(pg<=5){
                        load(true);
                    }

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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionAll.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });




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


            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                System.out.println("MY USER'S GENDER IS : " + mUser.getSex());
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                System.out.println("MY USER'S GENDER IS : " + mUser.getSex());
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);
            getUnseen();
            load(false);


        }
    }

   /* public void callAsyncPopulate(Integer pg, String token) throws Exception{
        JSONObject data = new JSONObject();
        data.put("pg", pg);
        data.put("token", token);
        new populatePageAsync().execute(data);

    }*/


    /*private class populatePageAsync extends AsyncTask<JSONObject, Integer, JSONArray>{

        @Override
        protected JSONArray doInBackground(JSONObject... jsonObjects) {
            PictureService pictureService = new PictureService();
            JSONArray data = null;
            try {
                pictureService.getCompetitionPictures(jsonObjects[0].getInt("pg"), jsonObjects[0].getString("token"));
                while (data == null){
                    data = pictureService.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(JSONArray data) {

            for(int i = 0; i < data.length(); i++){


                try{

                    JSONObject poster = data.getJSONObject(i).getJSONObject("poster");
                    JSONObject pic = data.getJSONObject(i);
                    String name = poster.getString("name");
                    String usrId = poster.getString("userId");
                    String profilePicUrl = poster.getString("profilePicURL");
                    String id = pic.getString("id");
                    String description = pic.getString("description");
                    String picURL = pic.getString("picUrl");

                    Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
                    Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
                    Integer starsCount = Integer.parseInt(pic.getString("starsCount"));
                    System.out.println("POSTER: " + name + " " + usrId + " " + id + " " + description + " " + picURL + " " + isFeatured + " " + isCompeting + " " + starsCount);

                    JSONArray ratings = pic.getJSONArray("ratings");
                    JSONArray comments = pic.getJSONArray("comments");

                    String uploaded = pic.getString("uploaded");
                    *//*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                    LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));*//*
                    String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(uploaded));

                    setmAdapter(name, usrId, id, description, picURL, profilePicUrl , isFeatured, isCompeting, starsCount, comments.length(), ratings, comments, interval);

                } catch (Exception e){
                    e.printStackTrace();
                }



            }

            if(!loading){
                loading = true;
            }

            if(pg == 1){
                pg++;
                try {
                    callAsyncPopulate(pg, mUser.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }*/


   /* private void setmAdapter(String author, String usr, String photoID, String description, String picURL, String profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount,
                             Integer commentCount, JSONArray ratings, JSONArray comments, String uploaded){
        if(description == "null"){
            description = "";
        }
        Hashtag hashtag = new Hashtag(author, usr, photoID, description, picURL, profilePicURL, isFeatured, isCompeting, starsCount, commentCount);
        post.setComments(comments);
        post.setRatings(ratings);
        post.setUploaded(uploaded);
        gridImages.add(post);
        compPicsUrls.add(post.getPicURL());
        compAdapt.notifyDataSetChanged();
    }*/





    @Override
    public ArrayList<Hashtag> getAllData() {
        return compPicsUrls;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return compAdapt;
    }
}
