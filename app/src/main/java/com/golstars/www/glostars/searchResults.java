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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.DatabaseModel.RecentPicRealm;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.RecyclerGridAdapter;
import com.golstars.www.glostars.interfaces.OnSinglePicClick;
import com.golstars.www.glostars.interfaces.PopulatePage;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.NotificationService;
import com.golstars.www.glostars.network.PictureService;
import com.golstars.www.glostars.network.SearchUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;


public class searchResults extends AppCompatActivity implements PopulatePage, OnSinglePicClick,AdapterInfomation {

    RecyclerView searchgrid;
    ListView searchlist;

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

    ImageView slogo;

    CoordinatorLayout parentLayout;

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


    ArrayList<SearchItemUsr> usrs;
    SearchAdapter searchAdapter;

    RecyclerGridAdapter recentsAdapter;
    //recents is a string array that'll receive the pic urls from the recents
    ArrayList<Hashtag> recentsPics;
    //ArrayList<JSONObject> recentPostObjs;


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    GridLayoutManager layoutManager;
    MyUser mUser;
    int pg = 1;
    private Intent homeIntent;
    //gridImages is a Post array provides pic urls for recentsPics
    private ArrayList<Post> gridImages;
    Integer unseenNotifs = 0;
    private Handler handler=new Handler();
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realm=Realm.getDefaultInstance();
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Search");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.lightViolate));
//        getSupportActionBar().setDisplayUseLogoEnabled(false);

        parentLayout = (CoordinatorLayout) findViewById(R.id.search_result);

        searchgrid = (RecyclerView) findViewById(R.id.searchGrid);
        searchlist = (ListView)findViewById(R.id.searchlist);

        recentlyposted = (TextView)findViewById(R.id.recentlyposted);

        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);


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
        slogo = (ImageView)findViewById(R.id.searchlogo);




        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
//        mainbadge.setTypeface(type);
//        notificationbadge.setTypeface(type);
//        homebadge.setTypeface(type);
//        profilebadge.setTypeface(type);
//        competitionbadge.setTypeface(type);
//        camerabadge.setTypeface(type);
        recentlyposted.setTypeface(type);



        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(searchResults.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(searchResults.this, notificationNew.class));
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


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(searchResults.this, upload.class));
                menuDown.close(true);
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(searchResults.this, competition_page.class));
                menuDown.close(true);
            }
        });

        mUser = MyUser.getmUser(getApplicationContext());
        searchUser = new SearchUser();
        gridImages = new ArrayList<>();


        ///recentPostObjs = new ArrayList<>();
        recentsPics = new ArrayList<>();
        recentsAdapter = new RecyclerGridAdapter(this, recentsPics, getSupportFragmentManager()); //********************

        int numOfColumns = 3;
        layoutManager = new GridLayoutManager(this, numOfColumns);
        searchgrid.setLayoutManager(layoutManager);
        searchgrid.setAdapter(recentsAdapter);


        /*
        usrsNames = new ArrayList<>();
        usrsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usrsNames);
        searchlist.setAdapter(usrsAdapter);
        */

        /***************** user-search specific adapters ****************/
        usrs = new ArrayList<>();
        searchAdapter = new SearchAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, usrs, new BtnClick() {
            @Override
            public void onItemClick(SearchItemUsr com) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", com.UsrId);
                intent.setClass(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }
        });
        searchlist.setAdapter(searchAdapter);
        /***************************************************************/


        new getUserData().execute("");

        searchgrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(dy > 0){ //check for scroll down
                    //System.out.println("SCROLL DOWN");
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if(loading){
                        if((visibleItemCount + pastVisiblesItems) >= totalItemCount){
                            loading = false;
                            pg++;
                            try {
                                if(isConnected(getApplicationContext())){
                                    callAsyncPopulate(pg, mUser.getToken());
                                }else{
                                    noConnetionMsg();
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

        if(isConnected(getApplicationContext())){
            //callAsyncPopulate(pg, mUser.getToken());
            loading = false;
            pg++;
            try {
                callAsyncPopulate(pg, mUser.getToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getUnseen();
            LoadServer();
        }else{
            noConnetionMsg();
        }


//        if(!isConnected()){
//            startActivity(new Intent(this, noInternet.class));
//        }


        LoadOffline();

    }

    private void LoadOffline() {
        Gson gson=new Gson();
        RealmResults<RecentPicRealm> all=realm.where(RecentPicRealm.class).findAllSorted("id", Sort.DESCENDING);
        for (RecentPicRealm recentPicRealm:all
             ) {
            recentsPics.add(gson.fromJson(recentPicRealm.data,Hashtag.class));
        }
        recentsAdapter.notifyDataSetChanged();
    }

    //<editor-fold desc="Load server">
    public void LoadServer(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        HubConnection connection = new HubConnection(ServerInfo.BASE_URL);
        HubProxy hub = connection.createHubProxy("GlostarsHub");

        final MyUser me=MyUser.getmUser(getApplicationContext());
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

                            for (Hashtag h:recentsPics
                                    ) {

                                System.out.println("Check with user id "+h.getPoster().getUserId());
                                if(h.getPoster().getUserId().equalsIgnoreCase(me.getUserId())){
                                    continue;
                                }else if(followInfo.isMutual){
                                    h.setIs_mutual(true);
                                    h.setHe_follow(true);
                                    h.setMe_follow(true);
                                }else if(h.getPoster().getUserId().equalsIgnoreCase(followInfo.originatedById)){
                                    if(followInfo.originateFollowDestination){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(true);
                                    }else if(followInfo.destinationFollowOriginate){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(true);
                                        h.setMe_follow(false);
                                    }else{
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(false);
                                    }
                                }else if(h.getPoster().getUserId().equalsIgnoreCase(followInfo.destinationById)){

                                    if(followInfo.originateFollowDestination){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(true);
                                        h.setMe_follow(false);
                                    }else if(followInfo.destinationFollowOriginate){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(true);
                                    }else{
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(false);
                                    }
                                }
                            }
                            recentsAdapter.notifyDataSetChanged();
                        }

                    }
                });

            }
        },String.class);
        //</editor-fold>
        hub.on("updatePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Update Picture "+o);



                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());

                        for (int i = 0; i < recentsPics.size(); i++) {
                            System.out.println(recentsPics.get(i).getId()+"--"+hashtag.getId());
                            if(recentsPics.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");
                                for (Rating rating:hashtag.getRatings()
                                        ) {
                                    if(rating.getRaterId().equalsIgnoreCase(recentsPics.get(i).getPoster().getUserId())){
                                        hashtag.setMyStarCount(rating.getStarsCount());
                                        break;
                                    }
                                }

                                List<Comment> comments=recentsPics.get(i).getComments();
                                comments.clear();
                                for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                        ) {
                                    comments.add(comment);
                                }
                                hashtag.setComments(comments);

                                recentsPics.set(i,hashtag);
                                recentsAdapter.notifyDataSetChanged();
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

                        if(hashtag.getPrivacy().equalsIgnoreCase("public")){
                            recentsPics.add(0,hashtag);
                            recentsAdapter.notifyDataSetChanged();
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

                        for (Hashtag hashtag:recentsPics){
                            if(hashtag.getId()==postId){
                                recentsPics.remove(hashtag);
                                recentsAdapter.notifyDataSetChanged();
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

                        for (Hashtag hashtag:recentsPics){
                            if(hashtag.getPoster().getUserId().equalsIgnoreCase(userDetails.id)){
                                hashtag.getPoster().setName(userDetails.name+" "+userDetails.lastName);
                                hashtag.getPoster().setProfilePicURL(userDetails.profilePicURL);
                                Picasso.with(searchResults.this).invalidate(userDetails.profilePicURL);
                            }
                        }
                        recentsAdapter.notifyDataSetChanged();
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorAccent));
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/

                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorAccent));
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
                        /*menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorAccent));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorAccent));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);*/


                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);


    }
    //</editor-fold>
    public static boolean isConnected(Context context){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }

    public void noConnetionMsg(){

        Snackbar noInternetSnackBar = Snackbar.make(parentLayout,"No Internet Connection",Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.lightViolate))
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridImages.clear();

                        pg=1;
                        try {
                            //callAsyncPopulate(pg);
                            pg = 1;
                            loading=false;
                            if(mUser == null){
                                new getUserData().execute("");
                            }else{
                                if(isConnected(getApplicationContext())){
                                    callAsyncPopulate(pg, mUser.getToken());
                                }else{
                                    noConnetionMsg();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
        noInternetSnackBar.show();
        loading = !false;
    }

    /*
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }*/
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
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(searchResults.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });




    }

    @Override
    public ArrayList<Hashtag> getAllData() {
        return recentsPics;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return recentsAdapter;
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
//            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
            try {
                callAsyncPopulate(pg, mUser.getToken());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_feed,menu);
        MenuItem search = menu.findItem(R.id.searchmenu);

        final SearchView searchView = (SearchView) search.getActionView();



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
                    //usrsNames.clear();
                    usrs.clear();
                    for(int i = 0; i < data.length(); i++){
                        //usrsNames.add(data.getJSONObject(i).getString("name") + " " + data.getJSONObject(i).getString("lastName"));
                        String name = data.getJSONObject(i).getString("name") + " " + data.getJSONObject(i).getString("lastName");
                        String usrPicUrl = data.getJSONObject(i).getString("profilemediumPath");
                        String usrId = data.getJSONObject(i).getString("id");

                        SearchItemUsr item = new SearchItemUsr(name, usrPicUrl,usrId);
                        usrs.add(item);


                        searchAdapter.notifyDataSetChanged();
                        //usrsAdapter.notifyDataSetChanged();
                        //System.out.println(usrsNames);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(newText.isEmpty()){
                    searchlist.setVisibility(View.GONE);
                    searchgrid.setVisibility(View.VISIBLE);
                    recentlyposted.setVisibility(View.VISIBLE);
                }
                // usrsAdapter.getFilter().filter(newText);



                return false;
            }
        });



        return true;
    }



    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }



    @Override
    public void callAsyncPopulate(Integer pg, String token) throws Exception{
        JSONObject data = new JSONObject();
        data.put("pg", pg);
        data.put("token", token);
        new populatePageAsync().execute(data);

    }

    @Override
    public void onItemClick(String url, Integer pos) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("images", gridImages);
        bundle.putInt("position", pos);
        bundle.putString("token", mUser.getToken());
        bundle.putString("usrID", mUser.getUserId());

/*
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);
        bundle.putString("token", mUser.getToken());
        bundle.putString("usrID", mUser.getUserId());
        bundle.putParcelable("poster", post.getPoster());



        Intent intent = new Intent(context, newFullscreen.class);
        intent.putExtras(bundle);
        //intent.putExtra("post", (Serializable) post);
        context.startActivity(intent);*/

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");


        /*
        Intent intent = new Intent();
        intent.putExtra("GOTOPIC", url);
        intent.setClass(searchResults.this, recentsFeed.class);
        startActivity(intent);
        */
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

            if(!loading){
                loading = true;
            }

            if(pg == 1){
                recentsPics.clear();
                recentsAdapter.notifyDataSetChanged();
                pg++;
                try {
                    callAsyncPopulate(pg, mUser.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }


    @Override
    public void bindDatatoUI(JSONObject object) throws Exception{
        System.out.println("BindTODataUi "+object.toString());
        JSONArray data = object.getJSONArray("picsToReturn");
        Gson gson=new Gson();
        ArrayList<Hashtag> getAllPost=gson.fromJson(data.toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
        recentsPics.addAll(getAllPost);
        Realm r= Realm.getDefaultInstance();
        for (Hashtag hashtag:getAllPost
             ) {
            RecentPicRealm recentPicRealm=new RecentPicRealm(hashtag.getId(),gson.toJson(hashtag).toString());
            r.beginTransaction();
            r.insertOrUpdate(recentPicRealm);
            r.commitTransaction();

        }
        System.out.println("Total Post "+getAllPost.size());
        recentsAdapter.notifyDataSetChanged();
    }

    /*private void setmAdapter(String author, String usr, String photoID, String description, String picURL, String profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount,
                             Integer commentCount, JSONArray ratings, JSONArray comments, String uploaded){
        if(description == "null"){
            description = "";
        }
        Post post = new Post(author, usr, photoID, description, picURL, profilePicURL, isFeatured, isCompeting, starsCount, commentCount);
        post.setComments(comments);
        post.setRatings(ratings);
        post.setUploaded(uploaded);
        gridImages.add(post);
        recentsPics.add(post.getPicURL());
        recentsAdapter.notifyDataSetChanged();
    }*/


    public class SearchItemUsr{
        String Username;
        String UserPicUrl;
        String UsrId;

        public SearchItemUsr(String usrname, String userPicUrl, String usrId){
            this.Username = usrname;
            this.UserPicUrl = userPicUrl;
            this.UsrId = usrId;
        }

    }

    public class SearchAdapter extends ArrayAdapter<SearchItemUsr>{

        private  BtnClick mClickListener = null;

        public SearchAdapter(Context context, int resource, List<SearchItemUsr> items, BtnClick listener){
            super(context, resource, items);
            mClickListener = listener;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.searchmodel, null);
            }

            final SearchItemUsr item = getItem(position);
            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");

            if(item != null){
                TextView usrName = (TextView) v.findViewById(R.id.usernameSearchModel);
                ImageView profilePic = (ImageView) v.findViewById(R.id.propicSearchModel);
                usrName.setTypeface(type);

                if(item.UserPicUrl.equals(null)){
                    profilePic.setVisibility(View.GONE);
                } else{
                    if(item.UserPicUrl.equals("/Content/Profile/Thumbs/male.jpg") || item.UserPicUrl.equals("/Content/Profile/Thumbs/Male.jpg")){
                        profilePic.setImageResource(R.drawable.nopicmalegrey);
                    } else if(item.UserPicUrl.equals("/Content/Profile/Thumbs/female.jpg") || item.UserPicUrl.equals("/Content/Profile/Thumbs/Female.jpg")){
                        profilePic.setImageResource(R.drawable.nopicfemalegrey);
                    }else{
                        Picasso.with(getApplicationContext()).load(item.UserPicUrl).into(profilePic);
                    }

                }

                usrName.setText(item.Username);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onItemClick(item);
                    }
                });


            }

            return v;

        }
    }


    public interface BtnClick{
        void onItemClick(SearchItemUsr com);
    }



}


