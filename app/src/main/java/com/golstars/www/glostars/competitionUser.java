package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.RecyclerGridAdapter;
import com.golstars.www.glostars.interfaces.OnSinglePicClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.squareup.picasso.Picasso;

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

public class competitionUser extends AppCompatActivity implements OnSinglePicClick,AdapterInfomation {

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

    ImageView slogo;
    EditText search;
    TextView gl;
    boolean showingFirst = true;


    private ArrayList<Hashtag> compPicsUrls=new ArrayList<Hashtag>();

    CoordinatorLayout parentLayout;
    RecyclerView competitionusergrid;

    private ArrayList<String> targetList;
    private RecyclerGridAdapter targetAdapter;
    private boolean loading=false;
    MyUser mUser;
    String guestUserId="";
    String target = "";
    private Intent homeIntent;


    int pastVisiblesItems, visibleItemCount, totalItemCount;


    int pg = 1;
    private GridLayoutManager layoutManager;
//
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        competitionusergrid = (RecyclerView) findViewById(R.id.competitionusergrid);
        parentLayout = (CoordinatorLayout) findViewById(R.id.seeallroot);

        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        String token;
        String usrId;

        gl = (TextView) findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);

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



        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        gl.setTypeface(type);


        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);



        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this,searchResults.class));

            }
        });


        //===========================================================================================

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, notificationNew.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(homeIntent);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, competition_page.class));
            }
        });


        //======================== DATA HANDLING =====================================
        //mUser = MyUser.getmUser(getApplicationContext());
        //new getUserDataComp().execute("");

//        Picasso.with(this).load(mUser.getProfilePicURL()).into(profileFAB);
        target = this.getIntent().getStringExtra("LOAD_TARGET");
        guestUserId = this.getIntent().getStringExtra("user_id");
        String guestUserName = this.getIntent().getStringExtra("guestUser");

        mUser = MyUser.getmUser(getApplicationContext());
        if(mUser == null){
            new getUserDataComp().execute("");
        } else{
            String[] firstName = guestUserName.split(" ", 2);
            System.out.println("MY USER IS " + target );
            System.out.println("THIS USER IS "  + guestUserId);

            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }

            if(target == null) {
                System.out.println("something else happened here");
            }

            else if(target.equals("COMPETITION")){
                gl.setText(firstName[0] +"'s Competition posts");


            }else if(target.equals("PUBLIC")){
                gl.setText(firstName[0] +"'s Public posts");

            }else if(target.equals("MUTUAL")){
                gl.setText(firstName[0] +"'s Mutual posts");

            }else if(target == null) {
                System.out.println("something else happened here");
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);
            load(target);
        }

        System.out.println(target);

        targetList = new ArrayList<>();

        //targetAdapter = new GridAdapter(getApplicationContext(), targetList);
        targetAdapter = new RecyclerGridAdapter(this, compPicsUrls, getSupportFragmentManager());
        int numOfColumns = 3;

        layoutManager=new GridLayoutManager(this, numOfColumns);

        competitionusergrid.setLayoutManager(layoutManager);
        competitionusergrid.setAdapter(targetAdapter);



        //mUser = MyUser.getmUser(getApplicationContext());
        //System.out.println("useris: " + mUser.getName());

        //load(target);




        /*if(target == null) {
            System.out.println("something else happened here");
        }

        else if(target.equals("COMPETITION")){


        }else if(target.equals("PUBLIC")){


        }else if(target.equals("MUTUAL")){


        }else if(target == null) {
            System.out.println("something else happened here");
        }*/

        competitionusergrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                if(searchResults.isConnected(getApplicationContext())){
                                    if(mUser.getUserId() == null){
                                        new getUserDataComp().execute("");
                                    } else{
                                        load(target);
                                    }
                                }else{
                                    noConnectionMsg();
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

//        if(!isConnected()){
//            startActivity(new Intent(this, noInternet.class));
//        }

        LoadServer();
    }

    private void noConnectionMsg() {
        Snackbar noInternetSnackBar = Snackbar.make(parentLayout,"No Internet Connection",Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.lightViolate))
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.putExtra("LOAD_TARGET", "COMPETITION");
                        intent.putExtra("user_id",target);
                        Bundle b = new Bundle();
                        b.putParcelable("user", mUser);
                        intent.putExtras(b);

                        intent.setClass(getApplicationContext(), competitionUser.class);
                        startActivity(intent);


                    }
                });
        noInternetSnackBar.show();
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
                                targetAdapter.notifyDataSetChanged();
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
                            targetAdapter.notifyDataSetChanged();
                        }
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
                        
                        for (Hashtag hashtag:compPicsUrls){
                            if(hashtag.getId()==postId){
                                compPicsUrls.remove(hashtag);
                                targetAdapter.notifyDataSetChanged();
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
                                Picasso.with(competitionUser.this).invalidate(userDetails.profilePicURL);
                            }
                        }
                        targetAdapter.notifyDataSetChanged();
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
                        FollowInfo followInfo=gson.fromJson(o,FollowInfo.class);

                        //Here data recieved originate user based
                        if(followInfo.originatedById.equalsIgnoreCase(guestUserId)){
                            System.out.println("In 1");
                            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
                            if(followInfo.isMutual){

                                followStatusUpdate("Mutual");
                            }
                            else if(followInfo.destinationFollowOriginate){

                                followStatusUpdate("follower");
                            }else if(followInfo.originateFollowDestination){

                                followStatusUpdate("Following");

                            }else{

                                followStatusUpdate("follow");
                            }
                        }else if(followInfo.destinationById.equalsIgnoreCase(guestUserId)){
                            System.out.println("In 2");
                            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
                            if(followInfo.isMutual){

                                followStatusUpdate("Mutual");
                            }
                            else if(followInfo.originateFollowDestination){

                                followStatusUpdate("follower");
                            }else if(followInfo.destinationFollowOriginate){

                                followStatusUpdate("Following");

                            }else{

                                followStatusUpdate("follow");
                            }
                        }
                    }

                    public void followStatusUpdate(String followType){
                        for (Hashtag hashtag:compPicsUrls
                                ) {
                            if(followType.equalsIgnoreCase("mutual")){
                                hashtag.setMe_follow(true);
                                hashtag.setHe_follow(true);
                                hashtag.setIs_mutual(true);
                            }else if(followType.equalsIgnoreCase("following")){
                                hashtag.setMe_follow(true);
                                hashtag.setHe_follow(false);
                                hashtag.setIs_mutual(false);
                            }else if(followType.equalsIgnoreCase("follower")){
                                hashtag.setMe_follow(false);
                                hashtag.setHe_follow(true);
                                hashtag.setIs_mutual(false);
                            }else{
                                hashtag.setMe_follow(false);
                                hashtag.setHe_follow(false);
                                hashtag.setIs_mutual(false);
                            }
                        }
                        targetAdapter.notifyDataSetChanged();


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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorAccent));
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorAccent));
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/


                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(competitionUser.this,R.color.colorPrimary));
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
    }*/

    private class getUserDataComp extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            System.out.println("MY USER IS " + mUser.getUserId() );
            System.out.println("THIS USER IS "  + guestUserId);

            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }

            if(target == null) {
                System.out.println("something else happened here");
            }

            else if(target.equals("COMPETITION")){
                gl.setText(mUser.getName()+"'Competition posts");


            }else if(target.equals("PUBLIC")){
                gl.setText(mUser.getName()+"'Public posts");

            }else if(target.equals("MUTUAL")){
                gl.setText(mUser.getName()+"'Mutual posts");

            }else if(target == null) {
                System.out.println("something else happened here");
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);
            load(target);

        }
    }

    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }

    public void load(final String type){

        //mUser = MyUser.getmUser();
        //mUser.setContext(getApplicationContext());



        loading=true;
        String url = ServerInfo.BASE_URL_API+"/images/user/"+guestUserId+"/"+pg;

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
                    String txt="";
                    if(type.equals("COMPETITION")){
                        txt="competitionPictures";
                    }else if(type.equals("PUBLIC")){
                        txt="publicPictures";
                    }else if(type.equals("MUTUAL")){
                        txt="mutualFollowerPictures";
                    }
                    ArrayList<Hashtag> getAllPost=gson.fromJson(response.getJSONObject("resultPayload").getJSONObject("model").getJSONArray(txt).toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
                    compPicsUrls.addAll(getAllPost);
                    System.out.println("Total Post "+getAllPost.size());
                    targetAdapter.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    System.out.println("Loading complete");

                    if(pg<=5){
                        load2(type);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }


    public void load2(final String type){
        loading=true;
        String url = ServerInfo.BASE_URL_API+"/images/user/"+guestUserId+"/"+pg;

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
                    String txt="";
                    if(type.equals("COMPETITION")){
                        txt="competitionPictures";
                    }else if(type.equals("PUBLIC")){
                        txt="publicPictures";
                    }else if(type.equals("MUTUAL")){
                        txt="mutualFollowerPictures";

                    }
                    ArrayList<Hashtag> getAllPost=gson.fromJson(response.getJSONObject("resultPayload").getJSONObject("model").getJSONArray(txt).toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
                    compPicsUrls.addAll(getAllPost);
                    System.out.println("Total Post "+getAllPost.size());
                    targetAdapter.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    System.out.println("Loading complete");

                    if(pg<=5){
                        load2(type);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }




    @Override
    public void onItemClick(String url, Integer pos) {

    }

    @Override
    public ArrayList<Hashtag> getAllData() {
        return compPicsUrls;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return targetAdapter;
    }
}


