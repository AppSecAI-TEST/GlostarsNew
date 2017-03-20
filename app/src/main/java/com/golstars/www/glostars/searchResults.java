package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class searchResults extends AppCompatActivity implements  PopulatePage, OnSinglePicClick{

    RecyclerView searchgrid;
    ListView searchlist;

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;


    FloatingActionButton mainFAB ;
    FloatingActionButton cameraFAB;
    FloatingActionButton competitionFAB;
    ImageView profileFAB;
    FloatingActionButton notificationFAB;
    FloatingActionButton homeFAB;

    ImageView slogo;

    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    TextView recentlyposted;

    SearchUser searchUser;
    ArrayAdapter<String> usrsAdapter;
    ArrayList<String> usrsNames;

    RecyclerGridAdapter recentsAdapter;
    ArrayList<String> recentsPics;
    ArrayList<JSONObject> recentPostObjs;
    int pg = 1;

    MyUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logoandtext);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        searchgrid = (RecyclerView) findViewById(R.id.searchGrid);
        searchlist = (ListView)findViewById(R.id.searchlist);


        recentlyposted = (TextView)findViewById(R.id.recentlyposted);


        mainFAB = (FloatingActionButton)findViewById(R.id.mainFAB);
        cameraFAB =(FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (ImageView) findViewById(R.id.profileFAB);
        notificationFAB = (FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (FloatingActionButton)findViewById(R.id.homeFAB);

        //=============Notification Badges===============================================
        homebadge = (TextView)findViewById(R.id.homebadge);
        notificationbadge = (TextView)findViewById(R.id.notificationbadge);
        profilebadge = (TextView)findViewById(R.id.profilebadge);
        camerabadge = (TextView)findViewById(R.id.uploadbadge);
        mainbadge = (TextView)findViewById(R.id.mainbadge);
        competitionbadge = (TextView)findViewById(R.id.competitionbadge);
        slogo = (ImageView)findViewById(R.id.searchlogo);




        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        mainbadge.setTypeface(type);
        notificationbadge.setTypeface(type);
        homebadge.setTypeface(type);
        profilebadge.setTypeface(type);
        competitionbadge.setTypeface(type);
        camerabadge.setTypeface(type);
       recentlyposted.setTypeface(type);

        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen) {

                    cameraFAB.startAnimation(fab_hide);
                    competitionFAB.startAnimation(fab_hide);
                    profileFAB.startAnimation(fab_hide);
                    notificationFAB.startAnimation(fab_hide);
                    homeFAB.startAnimation(fab_hide);
                    mainFAB.startAnimation(rotate_anticlockwise);

                    cameraFAB.setClickable(false);
                    competitionFAB.setClickable(false);
                    profileFAB.setClickable(false);
                    notificationFAB.setClickable(false);
                    homeFAB.setClickable(false);
                    isOpen=false;
                }

                else{
                    cameraFAB.startAnimation(fab_show);
                    competitionFAB.startAnimation(fab_show);
                    profileFAB.startAnimation(fab_show);
                    notificationFAB.startAnimation(fab_show);
                    mainFAB.startAnimation(rotate_clockwise);
                    homeFAB.startAnimation(fab_show);

                    cameraFAB.setVisibility(View.VISIBLE);
                    competitionFAB.setVisibility(View.VISIBLE);
                    profileFAB.setVisibility(View.VISIBLE);
                    notificationFAB.setVisibility(View.VISIBLE);
                    homeFAB.setVisibility(View.VISIBLE);

                    cameraFAB.setClickable(true);
                    competitionFAB.setClickable(true);
                    profileFAB.setClickable(true);
                    notificationFAB.setClickable(true);
                    homeFAB.setClickable(true);
                    isOpen=true;

                }
            }
        });


        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                homeFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                homeFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(searchResults.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                homeFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                homeFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(searchResults.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                homeFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                homeFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(searchResults.this, user_profile.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                homeFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                homeFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(searchResults.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                homeFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                homeFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(searchResults.this, competitionAll.class));
            }
        });

        mUser = MyUser.getmUser();
        mUser.setContext(this);
        searchUser = new SearchUser();

        recentPostObjs = new ArrayList<>();
        recentsPics = new ArrayList<>();
        recentsAdapter = new RecyclerGridAdapter(this, recentsPics, this);
        int numOfColumns = 3;
        searchgrid.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        //=================>>>>><<<<<==========================
        searchgrid.setAdapter(recentsAdapter);
        //=================>>>>><<<<<==========================


        usrsNames = new ArrayList<>();
        usrsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usrsNames);
        searchlist.setAdapter(usrsAdapter);



        try {
            callAsyncPopulate(pg, mUser.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_feed,menu);
        MenuItem search = menu.findItem(R.id.searchmenu);
        final SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("search string is : " + newText);
                searchlist.setVisibility(View.VISIBLE);
                searchgrid.setVisibility(View.GONE);
                recentlyposted.setVisibility(View.GONE);
                JSONArray data = null;
                try {
                    searchUser.findUserByName(newText, mUser.getToken());
                    while(data == null){
                        data = searchUser.getDataArray();
                    }
                    usrsNames.clear();
                    for(int i = 0; i < data.length(); i++){
                        usrsNames.add(data.getJSONObject(i).getString("name") + " " + data.getJSONObject(i).getString("lastName"));
                        usrsAdapter.notifyDataSetChanged();
                        System.out.println(usrsNames);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
               // usrsAdapter.getFilter().filter(newText);



                return false;
            }
        });


        return true;
    }
    /*
    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("search string is : " + newText);
        try {
            searchUser.findUserByName(newText, mUser.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }*/

    @Override
    public void callAsyncPopulate(Integer pg, String token) throws Exception{
        JSONObject data = new JSONObject();
        data.put("pg", pg);
        data.put("token", token);
        new populatePageAsync().execute(data);

    }

    @Override
    public void onItemClick(String url, Integer pos) {
        Intent intent = new Intent();
        intent.putExtra("GOTOPIC", url);
        intent.setClass(searchResults.this, recentsFeed.class);
        startActivity(intent);

    }


    private class populatePageAsync extends AsyncTask<JSONObject, Integer, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            PictureService pictureService = new PictureService();
            JSONObject data = null;
            try {
                pictureService.getPublicPictures(jsonObjects[0].getInt("pg"), jsonObjects[0].getString("token"));
                while (data == null){
                    data = pictureService.getDataObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                bindDatatoUI(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void bindDatatoUI(JSONObject object) throws Exception{
        JSONArray data = object.getJSONArray("picsToReturn");
        for(int i = 0; i < data.length(); i++){
            recentPostObjs.add(data.getJSONObject(i));
            System.out.println(data.getJSONObject(i).getString("picUrl"));
            recentsPics.add(data.getJSONObject(i).getString("picUrl"));
            recentsAdapter.notifyDataSetChanged();
        }

    }



}
