package com.golstars.www.glostars;

import android.content.Intent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.baoyz.widget.PullRefreshLayout;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Poster;
import com.golstars.www.glostars.adapters.PostData;
import com.golstars.www.glostars.network.NotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class hashtagResults extends AppCompatActivity implements AdapterInfomation {

    RecyclerView hashtags;


    com.github.clans.fab.FloatingActionButton homeFAB;
    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;

    FloatingActionMenu menuDown;

    ArrayList<Hashtag> posts=new ArrayList<Hashtag>();
    PostData postDataAdapter;

    private Intent homeIntent;
    int pg=1;
    MyUser mUser;
    private boolean loading=false;
    PullRefreshLayout layout;
    String searchTag="";
    LinearLayoutManager horizontalLayoutManagaer;
    Integer unseenNotifs = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hashtags"); //Changing the activity label here.
        setContentView(R.layout.activity_hashtag_results);


        layout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                pg=1;
                try {
                    //callAsyncPopulate(pg);
                    pg = 1;
                    loading=false;
                    getPostData(searchTag);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int Height = displayMetrics.heightPixels;
        int Width = displayMetrics.widthPixels;


        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);
        menuDown = (com.github.clans.fab.FloatingActionMenu)findViewById(R.id.menu_down);






        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hashtagResults.this,MainFeed.class));
                menuDown.close(true);
            }
        });

        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hashtagResults.this, upload.class));
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

        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hashtagResults.this, notification.class));
                menuDown.close(true);
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hashtagResults.this, competitionAll.class));
                menuDown.close(true);

            }
        });





        hashtags = (RecyclerView)findViewById(R.id.hashtagrecycler);
        postDataAdapter=new PostData(posts,hashtagResults.this,Width,getSupportFragmentManager());


        searchTag=getIntent().getStringExtra("data");
        if(mUser == null){
            new getUserData().execute("");
        } else {
            getPostData(searchTag);
            getUnseen();
        }

        horizontalLayoutManagaer= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        hashtags.setLayoutManager(horizontalLayoutManagaer);
        hashtags.setAdapter(postDataAdapter);





        hashtags.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                System.out.println("Scrolling "+dx+" "+dy);
                if(dy > 0){ //check for scroll down
                    int visibleItemCount = horizontalLayoutManagaer.getChildCount();
                    int totalItemCount = horizontalLayoutManagaer.getItemCount();
                    int pastVisiblesItems = horizontalLayoutManagaer.findFirstVisibleItemPosition();
                    System.out.println("Total Item "+totalItemCount+" Loading "+loading);
                    if(!loading){
                        if((visibleItemCount + pastVisiblesItems) >= totalItemCount-2){
                            loading = true;
                            //pg++;
                            try {
                                //callAsyncPopulate(pg);
                                if(mUser == null){
                                    new getUserData().execute("");
                                } else {
                                    getPostData(searchTag);
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

        setTitle("#"+searchTag);



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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
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
            //userProfileIntent = new Intent();
            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);
            //setting user default pic on FAB MENU

            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }
            getUnseen();
            getPostData(searchTag);



        }
    }







    private void getPostData(String data) {
        //posts.clear();
        loading=true;
        String url = ServerInfo.BASE_URL_API+"images/HashTagPhoto?searchTag="+data+"&count="+pg;

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
                    posts.addAll(getAllPost);
                    System.out.println("Total Post "+posts.size());
                    postDataAdapter.notifyDataSetChanged();
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

    @Override
    public ArrayList<Hashtag> getAllData() {
        return this.posts;
    }

    @Override
    public PostData getAdapter() {
        return postDataAdapter;
    }
}
