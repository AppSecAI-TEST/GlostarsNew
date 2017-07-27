package com.golstars.www.glostars;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.RecyclerGridAdapterMul;
import com.golstars.www.glostars.interfaces.OnSinglePicClick;
import com.golstars.www.glostars.models.GuestUser;
import com.golstars.www.glostars.network.FollowerService;
import com.golstars.www.glostars.network.NotificationService;
import com.golstars.www.glostars.network.PictureService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class user_profile extends AppCompatActivity implements OnSinglePicClick,AdapterInfomation {

    //===========================FABS=========================================

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;

    LinearLayout followinglin;

    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;

    FloatingActionMenu menuDown;
    com.github.clans.fab.FloatingActionButton homeFAB;



    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;

    TextView compno,publicno,mutualno;

    RelativeLayout parentLayout;
    View divider;
    //===================================================================



    TextView usernameProfile;
    TextView userLocationProfile;
    TextView aboutMeBannerProfile;
    TextView compBanner;
    TextView publicBanner;
    TextView mutualBanner;
    TextView aboutMeTextProfile;
    TextView interestBannerProfile;
    TextView interestTextProfile;
    TextView recognitionBannerProfile;
    TextView numPhotosProfile;
    TextView numPhotosCountProfile;
    TextView numFollowingProfile;
    TextView numFollowingCountProfile;
    TextView numFollowersProfile;
    TextView numFollowersCountProfile;
    TextView seeAllCompetitionProfile;
    TextView seeAllPublicProfile;
    TextView seeAllMutualProfile;
    TextView weeklyPrizeCountProfile;
    TextView monthlyPrizeCountProfile;
    TextView exhibitionPrizeCountProfile;
    TextView settingsuser;
    TextView numPhotosCount;
    TextView grandPrizeCountProfile;

    ImageView userPicProfile;
    ImageView weeklyPrizeProfile;
    ImageView monthlyPrizeProfile;
    ImageView exhibitionPrizeProfile;

    ImageView editprofile;



    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;


    RelativeLayout publicnopost,mutualnopost,compnopost;

    Button follow;


    RecyclerView competitiongrid;
    RecyclerView publicgrid;
    RecyclerView mutualgrid;


    private ArrayList<Hashtag> compImgsUrls;
    //private GridAdapter compAdapter;
    private RecyclerGridAdapterMul comAdapter;

    private ArrayList<Hashtag> publicImgsUrls;
    //private GridAdapter publicAdapter;
    private RecyclerGridAdapterMul publicAdapter;

    private ArrayList<Hashtag> mutualImgsUrls;
    //private GridAdapter mutualAdapter;
    private RecyclerGridAdapterMul mutualAdapter;

    private GuestUser guestUser;

    private MyUser mUser;
    private FollowerService fService;
    private Intent homeIntent;
    Integer unseenNotifs = 0;

    private static final String glostarsUsrdetails = "glostarsUsrdetails";



    PullRefreshLayout layout;
    private Handler handler=new Handler();
    String finalTarget;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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

        System.out.println("Loading.........");

        final String TAG = user_profile.class.getName();


        publicnopost = (RelativeLayout)findViewById(R.id.publicnopost);
        compnopost = (RelativeLayout)findViewById(R.id.compnopost);
        mutualnopost = (RelativeLayout)findViewById(R.id.mutualnopost);

        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.homeFAB);

        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        parentLayout = (RelativeLayout) findViewById(R.id.content_user_profile);

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


        divider = (View)findViewById(R.id.divider);

        // competitiongrid.setNestedScrollingEnabled(false);

        settingsuser = (TextView)findViewById(R.id.settingsbutton);
        usernameProfile = (TextView)findViewById(R.id.profileuserNAME);
        userLocationProfile = (TextView)findViewById(R.id.profileuserLOCATION);
        aboutMeBannerProfile = (TextView)findViewById(R.id.useraboutBANNER);
        aboutMeTextProfile = (TextView)findViewById(R.id.useraboutTEXT);
        interestBannerProfile = (TextView)findViewById(R.id.userinterestBANNER);
        interestTextProfile = (TextView)findViewById(R.id.userinterestTEXT);
        recognitionBannerProfile =(TextView)findViewById(R.id.recognitionBanner);
        compBanner = (TextView)findViewById(R.id.competitionBanner);
        publicBanner = (TextView)findViewById(R.id.publicBanner);
        mutualBanner = (TextView)findViewById(R.id.mutualBanner);
        weeklyPrizeCountProfile = (TextView)findViewById(R.id.weeklyPrizeCount);
        monthlyPrizeCountProfile = (TextView)findViewById(R.id.monthlyPrizeCount);
        exhibitionPrizeCountProfile = (TextView)findViewById(R.id.exhibitionPrizeCount);
        numPhotosProfile = (TextView)findViewById(R.id.numberofposts);
        numPhotosCountProfile = (TextView)findViewById(R.id.numberofpostsCount);
        numFollowersProfile = (TextView)findViewById(R.id.numberoffollowers);
        numFollowersCountProfile = (TextView)findViewById(R.id.numberoffollowersCount);
        numFollowingProfile = (TextView)findViewById(R.id.numberoffollowing);
        numFollowingCountProfile = (TextView)findViewById(R.id.numberoffollowingCount);
        seeAllCompetitionProfile = (TextView)findViewById(R.id.seeAllCompetition);
        seeAllPublicProfile = (TextView)findViewById(R.id.seeAllPublic);
        seeAllMutualProfile = (TextView)findViewById(R.id.seeAllMutual);
        numPhotosCount = (TextView)findViewById(R.id.numberofpostsCount);
        grandPrizeCountProfile = (TextView)findViewById(R.id.grandprizecount);


        compno = (TextView)findViewById(R.id.compno);
        publicno = (TextView)findViewById(R.id.publicno);
        mutualno = (TextView)findViewById(R.id.mutualno);

        followinglin = (LinearLayout)findViewById(R.id.followinglin);

        editprofile = (ImageView)findViewById(R.id.editprofile);


        //===============================SEARCH BAR OPTIONS=============================================
        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);


        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_profile.this,MainFeed.class));
            }
        });


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(user_profile.this,searchResults.class));

            }
        });


        //===================================================================================================
        final Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        usernameProfile.setTypeface(type);
        userLocationProfile.setTypeface(type);
        aboutMeBannerProfile.setTypeface(type);
        aboutMeTextProfile.setTypeface(type);
        interestBannerProfile.setTypeface(type);
        interestTextProfile.setTypeface(type);
        recognitionBannerProfile.setTypeface(type);
        weeklyPrizeCountProfile.setTypeface(type);
        monthlyPrizeCountProfile.setTypeface(type);
        exhibitionPrizeCountProfile.setTypeface(type);
        numPhotosProfile.setTypeface(type);
        numFollowersProfile.setTypeface(type);
        numFollowingCountProfile.setTypeface(type);
        numPhotosCountProfile.setTypeface(type);
        numFollowingCountProfile.setTypeface(type);
        numFollowingProfile.setTypeface(type);
        seeAllPublicProfile.setTypeface(type);
        seeAllCompetitionProfile.setTypeface(type);
        settingsuser.setTypeface(type);
        compBanner.setTypeface(type);
        publicBanner.setTypeface(type);
        mutualBanner.setTypeface(type);
        grandPrizeCountProfile.setTypeface(type);
        publicno.setTypeface(type);
        compno.setTypeface(type);
        mutualno.setTypeface(type);
        numFollowersCountProfile.setTypeface(type);
        seeAllMutualProfile.setTypeface(type);

//        follow.setTypeface(type);






        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this,edit_profile.class));
            }
        });




//        userPicProfile = (ImageView) findViewById(R.id.userPIC);
        userPicProfile = (ImageView) findViewById(R.id.profileuserPIC);

        weeklyPrizeProfile = (ImageView)findViewById(R.id.weeklyPrize);
        monthlyPrizeProfile = (ImageView)findViewById(R.id.monthlyPrize);
        exhibitionPrizeProfile = (ImageView)findViewById(R.id.exhibitionPrize);

        follow = (Button)findViewById(R.id.profileuserFOLLOW);
        follow.setTransformationMethod(null);


        settingsuser.setVisibility(View.GONE);
        editprofile.setVisibility(View.GONE);
        follow.setVisibility(View.GONE);



        final String  target = this.getIntent().getStringExtra("USER_ID");


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(follow.getText().toString().equalsIgnoreCase("follower") || follow.getText().toString().equalsIgnoreCase("follow")){
                    String url = ServerInfo.BASE_URL_FOLLOWER_API+"Following/"+target;
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

                    client.post(getApplicationContext(), url,requestParams,new JsonHttpResponseHandler(){


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                System.out.println("1. "+response.toString());
                                if(response.getJSONObject("resultPayload").getBoolean("result")){
                                    if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                        follow.setText("Mutual");
                                        follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.mutualfollowerbutton));
                                        follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                        follow.setTransformationMethod(null);
                                        follow.setTypeface(type);
                                    }
                                    else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                        follow.setText("Following");
                                        follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followingbutton));
                                        follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                        follow.setTransformationMethod(null);
                                        follow.setTypeface(type);
                                    }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                        follow.setText("follower");
                                        follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbackbutton));
                                        follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                        follow.setTransformationMethod(null);
                                        follow.setTypeface(type);
                                    }else{
                                        follow.setText("follow");
                                        follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbutton));
                                        follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                        follow.setTransformationMethod(null);
                                        follow.setTypeface(type);
                                    }
                                }else{
                                    Toast.makeText(user_profile.this, response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
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
                                String url = ServerInfo.BASE_URL_FOLLOWER_API+"Unfollowing/"+target;
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

                                client.post(getApplicationContext(), url,requestParams,new JsonHttpResponseHandler(){


                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            System.out.println("1. "+response.toString());
                                            if(response.getJSONObject("resultPayload").getBoolean("result")){
                                                if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                                    follow.setText("Mutual");
                                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.mutualfollowerbutton));
                                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                                    follow.setTransformationMethod(null);
                                                    follow.setTypeface(type);
                                                }
                                                else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                                    follow.setText("Following");
                                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followingbutton));
                                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                                    follow.setTransformationMethod(null);
                                                    follow.setTypeface(type);
                                                }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                                    follow.setText("follower");
                                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbackbutton));
                                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                                    follow.setTransformationMethod(null);
                                                    follow.setTypeface(type);
                                                }else{
                                                    follow.setText("follow");
                                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbutton));
                                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                                    follow.setTransformationMethod(null);
                                                    follow.setTypeface(type);
                                                }
                                            }else{
                                                Toast.makeText(user_profile.this, response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(user_profile.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Unfollow", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();

                }
            }

        });



        //-------------------------- ADAPTER AND NETWORK SETTINGS ---------------------------------//
        competitiongrid = (RecyclerView) findViewById(R.id.competitionPosts);
        publicgrid = (RecyclerView) findViewById(R.id.publicPosts);
        mutualgrid = (RecyclerView) findViewById(R.id.mutualPosts);


        compImgsUrls = new ArrayList<>();
        publicImgsUrls = new ArrayList<>();
        mutualImgsUrls = new ArrayList<>();

        //compGridView = (GridView) findViewById(R.id.competitionPosts);
        //compAdapter = new GridAdapter(this, compImgsUrls);

        comAdapter = new RecyclerGridAdapterMul(this, compImgsUrls, getSupportFragmentManager()); // the last "this" means the onItemClick method is
        // implemented somewhere in this class
        publicAdapter = new RecyclerGridAdapterMul(this, publicImgsUrls, getSupportFragmentManager());
        mutualAdapter = new RecyclerGridAdapterMul(this, mutualImgsUrls, getSupportFragmentManager());

        int numOfColumns = 3;
        CustomGridLayout publicLayout = new CustomGridLayout(this, numOfColumns);
        publicLayout.setScrollEnabled(false);

        CustomGridLayout compLayout = new CustomGridLayout(this, numOfColumns);
        compLayout.setScrollEnabled(false);

        CustomGridLayout mutualLayout = new CustomGridLayout(this, numOfColumns);
        mutualLayout.setScrollEnabled(false);

        //setting the recyclerviews to exhibit grid views
        competitiongrid.setLayoutManager(compLayout);
        publicgrid.setLayoutManager(publicLayout);
        mutualgrid.setLayoutManager(mutualLayout);


        //assigning the adapter to the recycler views
        competitiongrid.setAdapter(comAdapter); //adapter for competition pics
        publicgrid.setAdapter(publicAdapter);
        mutualgrid.setAdapter(mutualAdapter);


        //publicgrid = (GridView) findViewById(R.id.publicPosts);
        //publicAdapter = new GridAdapter(this, publicImgsUrls);
        //mutualAdapter = new GridAdapter(this, mutualImgsUrls);


        mUser = MyUser.getmUser(getApplicationContext());

        // mUser.setContext(context);


        fService = new FollowerService();

        if(target != null){
            try {

                if(searchResults.isConnected(getApplicationContext())){

                    if(mUser.getUserId().equals(target)){
                        getCachedUser(target);
                    }else{
                        loadUserInformation(target);
                    }

                    new getUserAndSetData().execute(target);
                }else{
                    noConnectionMsg();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        settingsuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(user_profile.this,settingsPage.class));
                }
                catch (Exception d){
                    Log.e(TAG,"ERROR HERE",d);
                }
            }
        });



        //usernameProfile.setText(mUser.getName());
        /*try {
            populateGallery(target, 1, mUser.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        finalTarget = target;
        numFollowersProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("guestUserId", finalTarget);
                intent.putExtra("myUserId", mUser.getUserId());
                intent.putExtra("myUserPic", mUser.getProfilePicURL());
                intent.putExtra("token", mUser.getToken());
                intent.setClass(user_profile.this,newFollowersPage.class);
                startActivity(intent);
                System.out.println("Calling");

            }
        });

        numFollowingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("guestUserId", finalTarget);
                intent.putExtra("myUserId", mUser.getUserId());
                intent.putExtra("myUserPic", mUser.getProfilePicURL());
                intent.putExtra("token", mUser.getToken());
                intent.setClass(user_profile.this,newFollowersPage.class);
                startActivity(intent);
                System.out.println("Calling");
            }
        });

        seeAllCompetitionProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!compImgsUrls.isEmpty()){
                    Intent intent = new Intent();
                    intent.putExtra("LOAD_TARGET", "COMPETITION");
                    intent.putExtra("user_id",finalTarget);
                    Bundle b = new Bundle();
                    b.putParcelable("user", mUser);
                    intent.putExtras(b);

                    intent.setClass(getApplicationContext(), competitionUser.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "this user does not have competition pictures", Toast.LENGTH_LONG).show();
                }


                //startActivity(new Intent(user_profile.this, competitionUser.class));

            }
        });

        seeAllPublicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!publicImgsUrls.isEmpty()){
                    Intent intent = new Intent();
                    intent.putExtra("LOAD_TARGET", "PUBLIC");
                    intent.putExtra("user_id",finalTarget);
                    intent.setClass(getApplicationContext(), competitionUser.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "this user does not have public pictures", Toast.LENGTH_LONG).show();
                }

            }
        });



        seeAllMutualProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mutualImgsUrls.isEmpty()){
                    Intent intent = new Intent();
                    intent.putExtra("LOAD_TARGET", "MUTUAL");
                    intent.putExtra("user_id",finalTarget);
                    intent.setClass(getApplicationContext(), competitionUser.class);
                    startActivity(intent);

                    startActivity(new Intent(user_profile.this, competitionUser.class));
                } else{
                    Toast.makeText(getApplicationContext(), "this user does not have mutual pictures", Toast.LENGTH_LONG).show();
                }


            }
        });

        //settingsuser = (Button)findViewById(R.id.settingsbutton);

        final String finalTarget1 = target;
        settingsuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalTarget1 != null){
                    if(finalTarget1.equals(mUser.getUserId())){
                        Intent intent = new Intent();
                        intent.putExtra("usrId", mUser.getUserId());
                        intent.putExtra("token", mUser.getToken());
                        intent.setClass(getApplicationContext(), settingsPage.class);
                        startActivity(intent);
                    }
                }

            }
        });



        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(user_profile.this, notificationNew.class));
                menuDown.close(true);
            }
        });


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
                startActivity(new Intent(user_profile.this,MainFeed.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(user_profile.this, upload.class));
                menuDown.close(true);
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(user_profile.this, competition_page.class));
                menuDown.close(true);
            }
        });



        if(!isConnected()){
            Snackbar noInternetSnackBar = Snackbar.make(parentLayout,"No Internet Connection",Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.lightViolate))
                    .setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            noInternetSnackBar.show();
        }

        onResume();



        getUnseen();
        LoadServer();


    }

    private void noConnectionMsg() {
        if(!isConnected()){
            Snackbar noInternetSnackBar = Snackbar.make(parentLayout,"No Internet Connection",Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.lightViolate))
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
            noInternetSnackBar.show();
        }

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


                        boolean isFound=false;
                        //region Mutual photo search
                        for (int i = 0; i < mutualImgsUrls.size(); i++) {
                            System.out.println(mutualImgsUrls.get(i).getId()+"--"+hashtag.getId());
                            if(mutualImgsUrls.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");
                                isFound=true;
                                for (Rating rating:hashtag.getRatings()
                                        ) {
                                    if(rating.getRaterId().equalsIgnoreCase(mutualImgsUrls.get(i).getPoster().getUserId())){
                                        hashtag.setMyStarCount(rating.getStarsCount());
                                        break;
                                    }
                                }

                                List<Comment> comments=mutualImgsUrls.get(i).getComments();
                                comments.clear();
                                for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                        ) {
                                    comments.add(comment);
                                }
                                hashtag.setComments(comments);

                                mutualImgsUrls.set(i,hashtag);
                                mutualAdapter.notifyDataSetChanged();

                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            }
                        }
                        //endregion

                        if(isFound)
                            return;
                        else{
                            for (int i = 0; i < compImgsUrls.size(); i++) {
                                System.out.println(compImgsUrls.get(i).getId()+"--"+hashtag.getId());
                                if(compImgsUrls.get(i).getId()==hashtag.getId()){
                                    System.out.println("Found...");
                                    isFound=true;
                                    for (Rating rating:hashtag.getRatings()
                                            ) {
                                        if(rating.getRaterId().equalsIgnoreCase(compImgsUrls.get(i).getPoster().getUserId())){
                                            hashtag.setMyStarCount(rating.getStarsCount());
                                            break;
                                        }
                                    }

                                    List<Comment> comments=compImgsUrls.get(i).getComments();
                                    comments.clear();
                                    for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                            ) {
                                        comments.add(comment);
                                    }
                                    hashtag.setComments(comments);

                                    compImgsUrls.set(i,hashtag);
                                    comAdapter.notifyDataSetChanged();

                                    if(adapter!=null){
                                        adapter.notifyDataSetChanged();
                                    }
                                    break;
                                }
                            }
                        }


                        if(isFound)
                            return;
                        else{
                            for (int i = 0; i < publicImgsUrls.size(); i++) {
                                System.out.println(publicImgsUrls.get(i).getId()+"--"+hashtag.getId());
                                if(publicImgsUrls.get(i).getId()==hashtag.getId()){
                                    System.out.println("Found...");
                                    isFound=true;
                                    for (Rating rating:hashtag.getRatings()
                                            ) {
                                        if(rating.getRaterId().equalsIgnoreCase(publicImgsUrls.get(i).getPoster().getUserId())){
                                            hashtag.setMyStarCount(rating.getStarsCount());
                                            break;
                                        }
                                    }

                                    List<Comment> comments=publicImgsUrls.get(i).getComments();
                                    comments.clear();
                                    for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                            ) {
                                        comments.add(comment);
                                    }
                                    hashtag.setComments(comments);

                                    publicImgsUrls.set(i,hashtag);
                                    publicAdapter.notifyDataSetChanged();
                                    if(adapter!=null){
                                        adapter.notifyDataSetChanged();
                                    }
                                    break;
                                }
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

                        if(hashtag.getPoster().getUserId().equalsIgnoreCase(finalTarget)){
                            if(hashtag.isIsCompeting()){
                                compImgsUrls.add(0,hashtag);
                                comAdapter.notifyDataSetChanged();
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            }else if(hashtag.getPrivacy().equalsIgnoreCase("friends")){
                                mutualImgsUrls.add(0,hashtag);
                                mutualAdapter.notifyDataSetChanged();
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
                                publicImgsUrls.add(0,hashtag);
                                publicAdapter.notifyDataSetChanged();
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            UiRecycleControl();
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


                        for (Hashtag hashtag:mutualImgsUrls){
                            if(hashtag.getId()==postId){
                                mutualImgsUrls.remove(hashtag);
                                mutualAdapter.notifyDataSetChanged();
                                UiRecycleControl();
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                                return;
                            }
                        }

                        for (Hashtag hashtag:compImgsUrls){
                            if(hashtag.getId()==postId){
                                compImgsUrls.remove(hashtag);
                                comAdapter.notifyDataSetChanged();
                                UiRecycleControl();
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                                return;
                            }
                        }

                        for (Hashtag hashtag:publicImgsUrls){
                            if(hashtag.getId()==postId){
                                publicImgsUrls.remove(hashtag);
                                publicAdapter.notifyDataSetChanged();
                                UiRecycleControl();
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
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

                        if(userDetails.id.equalsIgnoreCase(finalTarget)){

                            Glide.with(getApplicationContext()).load(userDetails.profilePicURL).into(userPicProfile);
                            usernameProfile.setText(userDetails.name+" "+userDetails.lastName);
                            userLocationProfile.setText(userDetails.location);
                            aboutMeTextProfile.setText(userDetails.aboutMe);
                            interestTextProfile.setText(userDetails.interests);
                            weeklyPrizeCountProfile.setText(userDetails.recogprofile.weekly);
                            monthlyPrizeCountProfile.setText(userDetails.recogprofile.monthly);
                            grandPrizeCountProfile.setText(userDetails.recogprofile.grand);
                            exhibitionPrizeCountProfile.setText(userDetails.recogprofile.exhibition);
                            numPhotosCountProfile.setText(userDetails.totalPhoto+"");
                            numFollowingCountProfile.setText(userDetails.followingCount+"");
                            numFollowersCountProfile.setText(userDetails.followerCount+"");


                            /*if(hashtag.isIsCompeting()){
                                compImgsUrls.add(0,hashtag);
                                comAdapter.notifyDataSetChanged();
                            }else if(hashtag.is_mutual()){
                                mutualImgsUrls.add(0,hashtag);
                                mutualAdapter.notifyDataSetChanged();
                            }else{
                                publicImgsUrls.add(0,hashtag);
                                publicAdapter.notifyDataSetChanged();
                            }*/
                        }


                        /*for (Hashtag hashtag:postList){
                            if(hashtag.getPoster().getUserId().equalsIgnoreCase(userDetails.id)){
                                hashtag.getPoster().setName(userDetails.name+" "+userDetails.lastName);
                                hashtag.getPoster().setProfilePicURL(userDetails.profilePicURL);
                                Picasso.with(user_profile.this).invalidate(userDetails.profilePicURL);
                            }
                        }
                        mAdapter.notifyDataSetChanged();*/
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
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
                        FollowInfo followInfo=gson.fromJson(o,FollowInfo.class);

                        //Here data recieved originate user based
                        if(followInfo.originatedById.equalsIgnoreCase(me.getUserId()) || followInfo.destinationById.equalsIgnoreCase(me.getUserId())){
                            if(followInfo.originatedById.equalsIgnoreCase(finalTarget)){
                                System.out.println("In 1");
                                Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
                                if(followInfo.isMutual){
                                    follow.setText("Mutual");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.mutualfollowerbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("Mutual");
                                }
                                else if(followInfo.destinationFollowOriginate){
                                    follow.setText("follower");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbackbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("follower");
                                }else if(followInfo.originateFollowDestination){
                                    follow.setText("Following");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followingbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("Following");

                                }else{
                                    follow.setText("follow");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("follow");
                                }
                            }else if(followInfo.destinationById.equalsIgnoreCase(finalTarget)){
                                System.out.println("In 2");
                                Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
                                if(followInfo.isMutual){
                                    follow.setText("Mutual");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.mutualfollowerbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("Mutual");
                                }
                                else if(followInfo.originateFollowDestination){
                                    follow.setText("follower");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbackbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("follower");
                                }else if(followInfo.destinationFollowOriginate){
                                    follow.setText("Following");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followingbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("Following");

                                }else{
                                    follow.setText("follow");
                                    follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbutton));
                                    follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                                    follow.setTransformationMethod(null);
                                    follow.setTypeface(type);
                                    followStatusUpdate("follow");
                                }
                            }
                        }


                    }

                    public void followStatusUpdate(String followType){
                        for (Hashtag hashtag:compImgsUrls
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
                        comAdapter.notifyDataSetChanged();

                        for (Hashtag hashtag:mutualImgsUrls
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
                        mutualAdapter.notifyDataSetChanged();

                        for (Hashtag hashtag:publicImgsUrls
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
                        publicAdapter.notifyDataSetChanged();

                        if(adapter!=null){
                            adapter.notifyDataSetChanged();
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorAccent));
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/


                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorAccent));
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);


    }




    public void UiRecycleControl(){
        //region Recycle view control
        int totalmutualFollowerPics=mutualImgsUrls.size();
        int totalCompetitionPic=compImgsUrls.size();
        int totalpublicPictures=publicImgsUrls.size();

        if(totalmutualFollowerPics>0){
            mutualnopost.setVisibility(View.GONE);
            mutualgrid.setVisibility(View.VISIBLE);
        }else {
            mutualnopost.setVisibility(View.VISIBLE);
            mutualgrid.setVisibility(View.GONE);
        }

        if(totalCompetitionPic>0){
            compnopost.setVisibility(View.GONE);
            competitiongrid.setVisibility(View.VISIBLE);
        }else {
            compnopost.setVisibility(View.VISIBLE);
            competitiongrid.setVisibility(View.GONE);
        }


        if(totalpublicPictures>0){
            publicnopost.setVisibility(View.GONE);
            publicgrid.setVisibility(View.VISIBLE);
        }else {
            publicnopost.setVisibility(View.VISIBLE);
            publicgrid.setVisibility(View.GONE);
        }

    }


    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }

    private void getCachedUser(final String target) {
        String filename = "glostarsUsrDetails";
        InputStream inputStream;
        String data = "";

        try {
            inputStream = openFileInput(filename);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receive = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receive = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receive);
                }

                inputStream.close();
                data = stringBuilder.toString();
                System.out.println(data);

                /** handling cached data */
                settingsuser.setVisibility(View.VISIBLE);
                editprofile.setVisibility(View.VISIBLE);
                try {

                    homeIntent = new Intent();
                    homeIntent.putExtra("USER_ID", mUser.getUserId());
                    homeIntent.setClass(getApplicationContext(), user_profile.class);

                    JSONObject response = new JSONObject(data);
                    JSONObject jsonObject = response.getJSONObject("resultPayload");

                    weeklyPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("weekly"));
                    monthlyPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("monthly"));
                    grandPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("grand"));
                    exhibitionPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("exhibition"));

                    usernameProfile.setText(jsonObject.getString("name") + " " + jsonObject.getString("lastName"));

                    String location = jsonObject.getString("location");
                    System.out.println("LOCATION: " + location);
                    System.out.println("LOCATION: " + location);
                    System.out.println("LOCATION: " + location);
                    System.out.println("LOCATION: " + location);
                    if (location.equals("null")) {
                        userLocationProfile.setText("");
                    } else if (location == null) {
                        userLocationProfile.setText("");
                    } else {
                        userLocationProfile.setText(jsonObject.getString("location"));
                    }
//
                    String aboutMe = jsonObject.getString("aboutMe");
                    if (aboutMe != "null") aboutMeTextProfile.setText(aboutMe);
                    else aboutMeTextProfile.setText("");

                    String interests = jsonObject.getString("interests");
                    if (interests != "null") interestTextProfile.setText(interests);
                    else interestTextProfile.setText("");


                    String pic = jsonObject.getString("profilePicURL");
                    if (pic.equals("/Content/Profile/Thumbs/male.jpg") || pic.equals("/Content/Profile/Thumbs/Male.jpg")) {
                        userPicProfile.setImageResource(R.drawable.nopicmalegrey);

                    } else if (pic.equals("/Content/Profile/Thumbs/female.jpg") || pic.equals("/Content/Profile/Thumbs/Female.jpg")) {
                        userPicProfile.setImageResource(R.drawable.nopicfemalegrey);
                    } else {
                        Glide.with(getApplicationContext()).load(pic).into(userPicProfile);
                        //
                    }

                    //setting user default pic on FAB MENU
                    if (mUser.getSex().equals("Male")) {
                        profileFAB.setImageResource(R.drawable.nopicmale);
                    } else if (mUser.getSex().equals("Female")) {
                        profileFAB.setImageResource(R.drawable.nopicfemale);
                    }


                    if (!target.equals(mUser.getUserId())) {
                        followinglin.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                    }
                    Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Light.ttf");
                    if (jsonObject.getBoolean("isMutual")) {
                        follow.setText("Mutual");
                        follow.setBackground(ContextCompat.getDrawable(user_profile.this, R.drawable.mutualfollowerbutton));
                        follow.setTextColor(ContextCompat.getColor(user_profile.this, R.color.white));
                        follow.setTransformationMethod(null);
                        follow.setTypeface(type);
                    } else if (jsonObject.getBoolean("heFollow")) {
                        follow.setText("follower");
                        follow.setBackground(ContextCompat.getDrawable(user_profile.this, R.drawable.followbackbutton));
                        follow.setTextColor(ContextCompat.getColor(user_profile.this, R.color.white));
                        follow.setTransformationMethod(null);
                        follow.setTypeface(type);
                    } else if (jsonObject.getBoolean("meFollow")) {
                        follow.setText("Following");
                        follow.setBackground(ContextCompat.getDrawable(user_profile.this, R.drawable.followingbutton));
                        follow.setTextColor(ContextCompat.getColor(user_profile.this, R.color.white));
                        follow.setTransformationMethod(null);
                        follow.setTypeface(type);

                    } else {
                        follow.setText("follow");
                        follow.setBackground(ContextCompat.getDrawable(user_profile.this, R.drawable.followbutton));
                        follow.setTextColor(ContextCompat.getColor(user_profile.this, R.color.white));
                        follow.setTransformationMethod(null);
                        follow.setTypeface(type);
                    }
                    numPhotosCount.setText(jsonObject.getInt("totalPicCount") + "");
                    if (mUser.getUserId().equals(target)) {
                        follow.setVisibility(View.GONE);
                        numFollowersCountProfile.setText(jsonObject.getInt("followersCount") + "");
                        numFollowingCountProfile.setText(jsonObject.getInt("followingCount") + "");
                    } else {
                        follow.setVisibility(View.VISIBLE);
                        numFollowersCountProfile.setText(jsonObject.getInt("followersCount") + "");
                    }


                    //calling populateGallery() method using data from user
                    populateGallery(jsonObject.getString("id"), 1, mUser.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                loadUserInformation(target);
            }


        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            loadUserInformation(target);
        } catch (IOException e) {
            System.out.println("cannot read file");
            loadUserInformation(target);
        }
    }

    private void loadUserInformation(final String target) {
        String url = ServerInfo.BASE_URL_API+"account/GetUserDetails?userId="+target;

        final String filename = "glostarsUsrDetails";
        final FileOutputStream[] outputStream = new FileOutputStream[1];

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
        client.get(this, url,new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. " + response.toString());
                    if (mUser.getUserId().equals(target)) {
                        settingsuser.setVisibility(View.VISIBLE);
                        editprofile.setVisibility(View.VISIBLE);

                        /**saving user details to file*/
                        outputStream[0] = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream[0].write(response.toString().getBytes());
                        outputStream[0].close();
                        System.out.println("user saved");

                    } else {
                        settingsuser.setVisibility(View.GONE);
                        editprofile.setVisibility(View.GONE);

                    }
                    //setting UI rss with user data
                    try {

                        homeIntent = new Intent();
                        homeIntent.putExtra("USER_ID", mUser.getUserId());
                        homeIntent.setClass(getApplicationContext(), user_profile.class);



                        JSONObject jsonObject = response.getJSONObject("resultPayload");

                        weeklyPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("weekly"));
                        monthlyPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("monthly"));
                        grandPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("grand"));
                        exhibitionPrizeCountProfile.setText(jsonObject.getJSONObject("recogprofile").getString("exhibition"));

                        usernameProfile.setText(jsonObject.getString("name") + " " + jsonObject.getString("lastName"));

                        String location = jsonObject.getString("location");
                        System.out.println("LOCATION: " + location);
                        System.out.println("LOCATION: " + location);
                        System.out.println("LOCATION: " + location);
                        System.out.println("LOCATION: " + location);
                        if (location.equals("null")) {
                            userLocationProfile.setText("");
                        } else if(location == null){
                            userLocationProfile.setText("");
                        } else {
                            userLocationProfile.setText(jsonObject.getString("location"));
                        }
//
                        String aboutMe = jsonObject.getString("aboutMe");
                        if (aboutMe != "null") aboutMeTextProfile.setText(aboutMe);
                        else aboutMeTextProfile.setText("");

                        String interests = jsonObject.getString("interests");
                        if (interests != "null") interestTextProfile.setText(interests);
                        else interestTextProfile.setText("");



                        String pic = jsonObject.getString("profilePicURL");
                        if(pic.equals("/Content/Profile/Thumbs/male.jpg") || pic.equals("/Content/Profile/Thumbs/Male.jpg")){
                            userPicProfile.setImageResource(R.drawable.nopicmalegrey);

                        } else if(pic.equals("/Content/Profile/Thumbs/female.jpg") || pic.equals("/Content/Profile/Thumbs/Female.jpg")){
                            userPicProfile.setImageResource(R.drawable.nopicfemalegrey);
                        }else{
                            Glide.with(getApplicationContext()).load(pic).into(userPicProfile);
                            //
                        }

                        //setting user default pic on FAB MENU
                        if(mUser.getSex().equals("Male")){
                            profileFAB.setImageResource(R.drawable.nopicmale);
                        } else if(mUser.getSex().equals("Female")){
                            profileFAB.setImageResource(R.drawable.nopicfemale);
                        }


                        if(!target.equals(mUser.getUserId())){
                            followinglin.setVisibility(View.GONE);
                            divider.setVisibility(View.GONE);
                        }
                        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
                        if(jsonObject.getBoolean("isMutual")){
                            follow.setText("Mutual");
                            follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.mutualfollowerbutton));
                            follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                            follow.setTransformationMethod(null);
                            follow.setTypeface(type);
                        }
                        else if(jsonObject.getBoolean("heFollow")){
                            follow.setText("follower");
                            follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbackbutton));
                            follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                            follow.setTransformationMethod(null);
                            follow.setTypeface(type);
                        }else if(jsonObject.getBoolean("meFollow")){
                            follow.setText("Following");
                            follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followingbutton));
                            follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                            follow.setTransformationMethod(null);
                            follow.setTypeface(type);

                        }else{
                            follow.setText("follow");
                            follow.setBackground(ContextCompat.getDrawable(user_profile.this,R.drawable.followbutton));
                            follow.setTextColor(ContextCompat.getColor(user_profile.this,R.color.white));
                            follow.setTransformationMethod(null);
                            follow.setTypeface(type);
                        }
                        numPhotosCount.setText(jsonObject.getInt("totalPicCount")+"");
                        if (mUser.getUserId().equals(target)) {
                            follow.setVisibility(View.GONE);
                            numFollowersCountProfile.setText(jsonObject.getInt("followersCount") + "");
                            numFollowingCountProfile.setText(jsonObject.getInt("followingCount")+"");
                        }else{
                            follow.setVisibility(View.VISIBLE);
                            numFollowersCountProfile.setText(jsonObject.getInt("followersCount") + "");
                        }




                        //calling populateGallery() method using data from user
                        populateGallery(jsonObject.getString("id"), 1, mUser.getToken());
                    } catch (Exception e) {
                        e.printStackTrace();
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(user_profile.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });




    }

    private void populateGallery(String userId, int pg, String token) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("usrId", userId);
        data.put("pg", pg);
        data.put("token", token);

        new downloadData().execute(data);

    }

    @Override
    public void onItemClick(String url, Integer pos) {

    }

    ArrayList<Hashtag> hashtags=new ArrayList<Hashtag>();
    RecyclerView.Adapter adapter;
    public void setList(ArrayList<Hashtag> list){
        this.hashtags=list;
    }
    public void setAdapter(RecyclerView.Adapter adapter){
        this.adapter=adapter;
    }

    @Override
    public ArrayList<Hashtag> getAllData() {
        return hashtags;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }


    private class downloadData extends AsyncTask<JSONObject, Integer, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            PictureService pictureService = new PictureService();
            JSONObject data = null;
            try {
                pictureService.getUserPictures(jsonObjects[0].getString("usrId"), jsonObjects[0].getInt("pg"), jsonObjects[0].getString("token"));
                while(data == null){
                    data = pictureService.getDataObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //super.onPostExecute(jsonObject);
            Log.i("downloadData", "data from onPostExecute is " + jsonObject);
            try {
                bindDatatoUI(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    private class getUserAndSetData extends AsyncTask<String, Integer, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser.setContext(getApplicationContext());
            //SearchUser searchUser = new SearchUser();

            JSONObject data = new JSONObject();
            /*
            try {
                guestUser = searchUser.getGuestUser(strings[0], mUser.getToken());

                data.put("guestUsrId", guestUser.getUserId());
                data.put("guestName", guestUser.getName());
                data.put("guestPic", guestUser.getProfilePicURL());
                data.put("myUsrId", mUser.getUserId());
                data.put("myUsrPic", mUser.getProfilePicURL());
                data.put("token", mUser.getToken());

                searchUser.findUserInfo(data.getString("guestUsrId"), mUser.getToken());
                JSONObject dat = null;
                while(dat == null){
                    dat = searchUser.getDataObj();
                }

                data.put("aboutMe", dat.getString("aboutMe"));
                data.put("interests", dat.getString("interests"));
                data.put("location", dat.getString("location"));
                data.put("original_Location", dat.getString("original_Location"));
                data.put("ocupation", dat.getString("ocupation"));
                data.put("ocupationOther", dat.getString("ocupationOther"));

                //setting an intent to user profile with user data






            } catch (Exception e) {
                e.printStackTrace();
            } */

            return data;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //setting user default pic on FAB MENU
            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID", mUser.getUserId());
            homeIntent.setClass(getApplicationContext(), user_profile.class);


            if(mUser.getSex().equals("male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }
            /*
            try {

                if(mUser.getUserId().equals(guestUser.getUserId())){
                    settingsuser.setVisibility(View.VISIBLE);
                    editprofile.setVisibility(View.VISIBLE);
                } else {
                    settingsuser.setVisibility(View.GONE);
                    editprofile.setVisibility(View.GONE);

                }
                //setting UI rss with user data
                usernameProfile.setText(jsonObject.getString("guestName"));
                //fService.LoadFollowers(jsonObject.getString("guestUsrId"), jsonObject.getString("token"));

                String location = jsonObject.getString("location");
                if(location != "null"){
                    userLocationProfile.setText(jsonObject.getString("location"));
                } else userLocationProfile.setText("");

                String aboutMe = jsonObject.getString("aboutMe");
                if(aboutMe != "null") aboutMeTextProfile.setText(aboutMe);
                else aboutMeTextProfile.setText("");

                String interests = jsonObject.getString("interests");
                if(interests != "null")interestTextProfile.setText(interests);
                else interestTextProfile.setText("");


                //setting an intent to user profile with user data
                homeIntent = new Intent();
                homeIntent.putExtra("USER_ID", jsonObject.getString("myUsrId"));
                homeIntent.setClass(getApplicationContext(), user_profile.class);


//                Glide.with(getApplicationContext()).load(jsonObject.getString("myUsrPic")).into(profileFAB);

                Glide.with(getApplicationContext()).load(jsonObject.getString("guestPic")).into(userPicProfile);

                //calling populateGallery() method using data from user
                populateGallery(jsonObject.getString("guestUsrId"), 1, jsonObject.getString("token"));

                FollowerService.LoadFollowers(getApplicationContext(), target, mUser.getToken(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        try {
                            System.out.println(response);
                            JSONArray followerList = null;
                            JSONArray followingList = null;
                            try {
                                JSONObject resultPayload = response.getJSONObject("resultPayload");
                                followerList = resultPayload.getJSONArray("followerList");
                                followingList = resultPayload.getJSONArray("followingList");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            numFollowersCountProfile.setText(followerList.length()+"");
                            // numFollowingCountProfile.setText(followingList.length()+"");

                            boolean isFollower = false;
                            boolean isFollowing = false;

                            for(int i = 0; i < followerList.length() - 1; i++){
                                if (followerList.getJSONObject(i).getString("id").equals(mUser.getUserId())){
                                    isFollower = true;
                                    break;
                                }


                            }

                            for(int i = 0; i < followingList.length() - 1; i++){
                                if (followingList.getJSONObject(i).getString("id").equals(mUser.getUserId())){
                                    isFollowing = true;
                                    break;
                                }

                            }
                            System.out.println(isFollowing + " for following and " + isFollower + " for follower");

                            if(mUser.getUserId().equals(guestUser.getUserId())){
                                follow.setVisibility(View.GONE);
                            } else if(isFollower && !isFollowing){
                                follow.setVisibility(View.VISIBLE);
                                follow.setBackgroundColor(Color.parseColor("#007FFF"));
                                follow.setText("Follower");
                            } else if(!isFollower && isFollowing){
                                follow.setVisibility(View.VISIBLE);
                                follow.setBackgroundColor(Color.parseColor("#E1C8FF"));
                                follow.setText("Following");
                            } else if(isFollower && isFollowing){
                                follow.setVisibility(View.VISIBLE);
                                follow.setBackgroundColor(Color.parseColor("#640064"));
                                follow.setText("Mutual");
                            } else {
                                follow.setVisibility(View.VISIBLE);
                                follow.setText("Follow");
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // Pull out the first event on the public timeline
                        //JSONObject firstEvent = timeline.get(0);
                        //String tweetText = firstEvent.getString("text");

                        // Do something with the response
                        System.out.println(response);
                        //System.out.println(tweetText);
                    }
                });






            } catch (Exception e) {
                e.printStackTrace();
            }
            */

        }
    }


    private void bindDatatoUI(JSONObject jsonObject) throws Exception {
        //this method treats the data brought by downloadData()
        //Log.i("bindDatatoUI", "data from async task is " + jsonObject);
        JSONObject data = jsonObject;

        int totalCompetitionPic = data.getInt("totalCompetitonPic");
        int totalmutualFollowerPics = data.getInt("totalmutualFollowerPics");
        int totalpublicPictures = data.getInt("totalpublicPictures");
        JSONObject model = data.getJSONObject("model");
        JSONArray competitionPictures = model.getJSONArray("competitionPictures");
        JSONArray mutualFollowerPictures = model.getJSONArray("mutualFollowerPictures");
        JSONArray publicPictures = model.getJSONArray("publicPictures");

        Integer totalPics =  data.getInt("allPictureCount");

        if(totalmutualFollowerPics == 0){
            mutualgrid.setVisibility(View.GONE);
            seeAllMutualProfile.setVisibility(View.GONE);
            mutualnopost.setVisibility(View.VISIBLE);
        } else if (totalmutualFollowerPics >0 && totalmutualFollowerPics <=3) {
            ViewGroup.LayoutParams mutual = mutualgrid.getLayoutParams();
            mutual.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mutualgrid.setLayoutParams(mutual);
            seeAllMutualProfile.setVisibility(View.GONE);
        }else if (totalmutualFollowerPics > 3 && totalmutualFollowerPics <=6) {
            ViewGroup.LayoutParams mutual1 = mutualgrid.getLayoutParams();
            mutual1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mutualgrid.setLayoutParams(mutual1);
            seeAllMutualProfile.setVisibility(View.GONE);
        }else if (totalmutualFollowerPics > 6 && totalmutualFollowerPics <=9) {
            seeAllMutualProfile.setVisibility(View.GONE);
        }else{
            mutualgrid.setVisibility(View.VISIBLE);
            mutualnopost.setVisibility(View.GONE);
        }

        if(totalCompetitionPic == 0){
            compnopost.setVisibility(View.VISIBLE);
            competitiongrid.setVisibility(View.GONE);
            seeAllCompetitionProfile.setVisibility(View.GONE);
        }else if (totalCompetitionPic >0 && totalCompetitionPic <=3) {
            ViewGroup.LayoutParams comp = competitiongrid.getLayoutParams();
            comp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            competitiongrid.setLayoutParams(comp);
            seeAllCompetitionProfile.setVisibility(View.GONE);
        }else if (totalCompetitionPic > 3 && totalCompetitionPic <=6) {
            ViewGroup.LayoutParams comp1 = mutualgrid.getLayoutParams();
            comp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            competitiongrid.setLayoutParams(comp1);
            seeAllCompetitionProfile.setVisibility(View.GONE);
        }else if (totalCompetitionPic > 6 && totalCompetitionPic <=9) {
            seeAllCompetitionProfile.setVisibility(View.GONE);
        } else{
            compBanner.setVisibility(View.VISIBLE);
            seeAllCompetitionProfile.setVisibility(View.VISIBLE);
            compnopost.setVisibility(View.GONE);
        }

        if(totalpublicPictures == 0){
            publicnopost.setVisibility(View.VISIBLE);
            publicgrid.setVisibility(View.GONE);
            seeAllPublicProfile.setVisibility(View.GONE);
        }else if (totalpublicPictures >0 && totalpublicPictures <=3) {
            ViewGroup.LayoutParams pub = publicgrid.getLayoutParams();
            pub.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            publicgrid.setLayoutParams(pub);
            seeAllPublicProfile.setVisibility(View.GONE);

        }else if (totalCompetitionPic > 3 && totalCompetitionPic <=6) {
            ViewGroup.LayoutParams pub1 = publicgrid.getLayoutParams();
            pub1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            publicgrid.setLayoutParams(pub1);
            seeAllPublicProfile.setVisibility(View.GONE);
            seeAllCompetitionProfile.setVisibility(View.GONE);
        }else if (totalCompetitionPic > 6 && totalCompetitionPic <=9) {
            seeAllCompetitionProfile.setVisibility(View.GONE);

        } else{
            publicBanner.setVisibility(View.VISIBLE);
            seeAllPublicProfile.setVisibility(View.VISIBLE);
        }

        //numPhotosCount.setText(totalPics.toString());




        Gson gson=new Gson();
        ArrayList<Hashtag> getAllCom=gson.fromJson(jsonObject.getJSONObject("model").getJSONArray("competitionPictures").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
        compImgsUrls.addAll(getAllCom);
        comAdapter.notifyDataSetChanged();

        ArrayList<Hashtag> getAllMutual=gson.fromJson(jsonObject.getJSONObject("model").getJSONArray("mutualFollowerPictures").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
        mutualImgsUrls.addAll(getAllMutual);
        mutualAdapter.notifyDataSetChanged();

        ArrayList<Hashtag> getAllPub=gson.fromJson(jsonObject.getJSONObject("model").getJSONArray("publicPictures").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
        publicImgsUrls.addAll(getAllPub);
        publicAdapter.notifyDataSetChanged();

        /*if(competitionPictures != null){
            for(int i = 0; i < competitionPictures.length(); i++){
                JSONObject pic = competitionPictures.getJSONObject(i);
                setCompAdapter(pic.getString("picUrl"));
            }
        }

        if(publicPictures != null){
            for(int i = 0; i < publicPictures.length(); i++){
                JSONObject pic = publicPictures.getJSONObject(i);
                setPublicAdapter(pic.getString("picUrl"));
            }
        }

        if(mutualFollowerPictures != null){
            for(int i = 0; i < mutualFollowerPictures.length(); i++){
                JSONObject pic = mutualFollowerPictures.getJSONObject(i);


                setMutualAdapter(pic.getString("picUrl"));
            }
        }*/

    }




    /*private void setMutualAdapter(String picUrl) {
        mutualImgsUrls.add(picUrl);
        mutualAdapter.notifyDataSetChanged();
    }

    private void setPublicAdapter(String profilePicURL) {
        publicImgsUrls.add(profilePicURL);
        publicAdapter.notifyDataSetChanged();
    }

    private void setCompAdapter(String profilePicURL) {
        compImgsUrls.add(profilePicURL);
        comAdapter.notifyDataSetChanged();
        //
    }*/



    public class CustomGridLayout extends GridLayoutManager{
        private boolean isScrolEnabled = true;

        public CustomGridLayout(Context context, Integer colummns){
            super(context, colummns);
        }

        public void setScrollEnabled(boolean flag){
            this.isScrolEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrolEnabled && super.canScrollVertically();
        }
    }

}
