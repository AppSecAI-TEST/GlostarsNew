package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Hashtag;
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

import cz.msebera.android.httpclient.Header;

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
    ImageView gl;
    boolean showingFirst = true;


    private ArrayList<Hashtag> compPicsUrls=new ArrayList<Hashtag>();


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        competitionusergrid = (RecyclerView) findViewById(R.id.competitionusergrid);

        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        gl = (ImageView)findViewById(R.id.glostarslogo);
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


        //==========================================================================================

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, notification.class));
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

                startActivity(new Intent(competitionUser.this, competitionAll.class));
            }
        });


        //======================== DATA HANDLING =====================================
        //mUser = MyUser.getmUser(getApplicationContext());
        //new getUserDataComp().execute("");

//        Picasso.with(this).load(mUser.getProfilePicURL()).into(profileFAB);

        targetList = new ArrayList<>();

        //targetAdapter = new GridAdapter(getApplicationContext(), targetList);
        targetAdapter = new RecyclerGridAdapter(this, compPicsUrls, getSupportFragmentManager());
        int numOfColumns = 3;

        layoutManager=new GridLayoutManager(this, numOfColumns);

        competitionusergrid.setLayoutManager(layoutManager);
        competitionusergrid.setAdapter(targetAdapter);


        target = this.getIntent().getStringExtra("LOAD_TARGET");
        guestUserId = this.getIntent().getStringExtra("user_id");
        if(mUser == null){
            new getUserDataComp().execute("");
        } else{
            load(target);
        }

        System.out.println(target);




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
                                if(mUser.getUserId().equals(null)){
                                    new getUserDataComp().execute("");
                                } else{
                                    load(target);
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

            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
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


