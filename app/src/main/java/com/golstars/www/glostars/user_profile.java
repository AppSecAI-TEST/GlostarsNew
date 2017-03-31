package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

public class user_profile extends AppCompatActivity implements OnSinglePicClick{


    //===========================FABS=========================================

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

    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
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
    ImageView userPicProfile;
    ImageView weeklyPrizeProfile;
    ImageView monthlyPrizeProfile;
    ImageView exhibitionPrizeProfile;

    ImageView editprofile;



    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;


    Button follow;


    RecyclerView competitiongrid;
    RecyclerView publicgrid;
    RecyclerView mutualgrid;

    private ArrayList<String> compImgsUrls;
    //private GridAdapter compAdapter;
    private RecyclerGridAdapter comAdapter;

    private ArrayList<String> publicImgsUrls;
    //private GridAdapter publicAdapter;
    private RecyclerGridAdapter publicAdapter;

    private ArrayList<String> mutualImgsUrls;
    //private GridAdapter mutualAdapter;
    private RecyclerGridAdapter mutualAdapter;

    private GuestUser guestUser;

    private MyUser mUser;
    private FollowerService fService;
    private Intent homeIntent;
    Integer unseenNotifs = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String TAG = user_profile.class.getName();


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


        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


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

        editprofile = (ImageView)findViewById(R.id.editprofile);


        //===============================SEARCH BAR OPTIONS=============================================
        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);



        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(user_profile.this,searchResults.class));

            }
        });
        //===================================================================================================
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
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
      //  follow.setTypeface(type);





        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this,edit_profile.class));
            }
        });




        //userPicProfile = (ImageView) findViewById(R.id.userPIC);
        userPicProfile = (ImageView) findViewById(R.id.profileuserPIC);

        weeklyPrizeProfile = (ImageView)findViewById(R.id.weeklyPrize);
        monthlyPrizeProfile = (ImageView)findViewById(R.id.monthlyPrize);
        exhibitionPrizeProfile = (ImageView)findViewById(R.id.exhibitionPrize);

        follow = (Button)findViewById(R.id.profileuserFOLLOW);
        follow.setTransformationMethod(null);


        //-------------------------- ADAPTER AND NETWORK SETTINGS ---------------------------------//
        competitiongrid = (RecyclerView) findViewById(R.id.competitionPosts);
        publicgrid = (RecyclerView) findViewById(R.id.publicPosts);
        mutualgrid = (RecyclerView) findViewById(R.id.mutualPosts);


        compImgsUrls = new ArrayList<>();
        publicImgsUrls = new ArrayList<>();
        mutualImgsUrls = new ArrayList<>();

        //compGridView = (GridView) findViewById(R.id.competitionPosts);
        //compAdapter = new GridAdapter(this, compImgsUrls);

        comAdapter = new RecyclerGridAdapter(this, compImgsUrls, this); // the last "this" means the onItemClick method is
                                                                        // implemented somewhere in this class
        publicAdapter = new RecyclerGridAdapter(this, publicImgsUrls, this);
        mutualAdapter = new RecyclerGridAdapter(this, mutualImgsUrls, this);

        int numOfColumns = 3;
        //setting the recyclerviews to exhibit grid views
        competitiongrid.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        publicgrid.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        mutualgrid.setLayoutManager(new GridLayoutManager(this, numOfColumns));

        //assigning the adapter to the recycler views
        competitiongrid.setAdapter(comAdapter); //adapter for competition pictures
        publicgrid.setAdapter(publicAdapter);
        mutualgrid.setAdapter(mutualAdapter);


        //publicgrid = (GridView) findViewById(R.id.publicPosts);
        //publicAdapter = new GridAdapter(this, publicImgsUrls);
        //mutualAdapter = new GridAdapter(this, mutualImgsUrls);





        mUser = MyUser.getmUser();
       // mUser.setContext(context);



        String target = null;
        target = this.getIntent().getStringExtra("USER_ID");

        fService = new FollowerService();

        if(target != null){
            try {

                new getUserAndSetData().execute(target);

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
            populateGallery(guestUser.getUserId(), 1, mUser.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        numFollowersProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guestUser != null){
                    Intent intent = new Intent();
                    intent.putExtra("guestUserId", guestUser.getUserId());
                    intent.putExtra("myUserId", mUser.getUserId());
                    intent.putExtra("myUserPic", mUser.getProfilePicURL());
                    intent.putExtra("token", mUser.getToken());
                    intent.setClass(getApplicationContext(),followersPage.class);
                    startActivity(intent);
                }
                /*
                try{
                    startActivity(new Intent(user_profile.this,followersPage.class));
                }
                catch (Exception e){
                    Log.e(TAG,"ERROR HERE",e);
                }*/

            }
        });

        numFollowingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guestUser != null){
                    Intent intent = new Intent();
                    intent.putExtra("guestUserId", guestUser.getUserId());
                    intent.putExtra("myUserId", mUser.getUserId());
                    intent.putExtra("myUserPic", mUser.getProfilePicURL());
                    intent.putExtra("token", mUser.getToken());
                    intent.setClass(getApplicationContext(),followersPage.class);
                    startActivity(intent);
                }
            }
        });

        seeAllCompetitionProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!compImgsUrls.isEmpty()){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("COMPETITION_PICS", compImgsUrls);
                    intent.putExtra("LOAD_TARGET", "COMPETITION");
                    intent.putExtras(bundle);
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
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("PUBLIC_PICS", publicImgsUrls);
                    intent.putExtra("LOAD_TARGET", "PUBLIC");
                    intent.putExtras(bundle);
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
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("MUTUAL_PICS", mutualImgsUrls);
                    intent.putExtra("LOAD_TARGET", "MUTUAL");
                    intent.putExtras(bundle);
                    intent.setClass(getApplicationContext(), competitionUser.class);
                    startActivity(intent);

                    startActivity(new Intent(user_profile.this, competitionUser.class));
                } else{
                    Toast.makeText(getApplicationContext(), "this user does not have mutual pictures", Toast.LENGTH_LONG).show();
                }


            }
        });

        //settingsuser = (Button)findViewById(R.id.settingsbutton);

        settingsuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUser.getUserId() != null){
                    if(guestUser.getUserId().equals(mUser.getUserId())){
                        Intent intent = new Intent();
                        intent.putExtra("usrId", mUser.getUserId());
                        intent.putExtra("token", mUser.getToken());
                        intent.setClass(getApplicationContext(), settingsPage.class);
                        startActivity(intent);
                    }
                }




            }
        });




        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen) {

                    cameraFAB.startAnimation(fab_hide);
                    competitionFAB.startAnimation(fab_hide);
                    profileFAB.startAnimation(fab_hide);
                    notificationFAB.startAnimation(fab_hide);
                    mainFAB.startAnimation(rotate_anticlockwise);
                    homeFAB.startAnimation(fab_hide);


//                    homebadge.setVisibility(View.GONE);
//                    notificationbadge.setVisibility(View.GONE);

                    cameraFAB.setClickable(false);
                    competitionFAB.setClickable(false);
                    profileFAB.setClickable(false);
                    notificationFAB.setClickable(false);
                    isOpen=false;

                }

                else{
                    cameraFAB.startAnimation(fab_show);
                    competitionFAB.startAnimation(fab_show);
                    profileFAB.startAnimation(fab_show);
                    notificationFAB.startAnimation(fab_show);
                    homeFAB.startAnimation(fab_show);
                    mainFAB.startAnimation(rotate_clockwise);

//                    homebadge.setVisibility(View.VISIBLE);
//                    notificationbadge.setVisibility(View.VISIBLE);

                    cameraFAB.setVisibility(View.VISIBLE);
                    competitionFAB.setVisibility(View.VISIBLE);
                    profileFAB.setVisibility(View.VISIBLE);
                    notificationFAB.setVisibility(View.VISIBLE);
                    homeFAB.setVisibility(View.VISIBLE);

                    cameraFAB.setClickable(true);
                    competitionFAB.setClickable(true);
                    profileFAB.setClickable(true);
                    notificationFAB.setClickable(true);
                    isOpen=true;


                }
            }
        });


        //==========================================================================================

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(user_profile.this, MainFeed.class));
                finish();
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(user_profile.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                isOpen=false;

                startActivity(homeIntent);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(user_profile.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFAB.startAnimation(fab_hide);
                competitionFAB.startAnimation(fab_hide);
                profileFAB.startAnimation(fab_hide);
                notificationFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(user_profile.this, competitionAll.class));
            }
        });





    //getUnseen();



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
                        mainbadge.setVisibility(View.VISIBLE);
                        notificationbadge.setVisibility(View.VISIBLE);
                        mainbadge.setText(unseenNotifs.toString());
                        notificationbadge.setText(unseenNotifs.toString());

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
            SearchUser searchUser = new SearchUser();

            JSONObject data = new JSONObject();
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


            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
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


                Picasso.with(getApplicationContext()).load(jsonObject.getString("myUsrPic")).into(profileFAB);

                Picasso.with(getApplicationContext()).load(jsonObject.getString("guestPic")).into(userPicProfile);

                //calling populateGallery() method using data from user
                populateGallery(jsonObject.getString("guestUsrId"), 1, jsonObject.getString("token"));

                FollowerService.LoadFollowers(getApplicationContext(), guestUser.getUserId(), mUser.getToken(), new JsonHttpResponseHandler(){
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
                            numFollowersCountProfile.setText(followerList.length());
                            numFollowingCountProfile.setText(followingList.length());

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
                                follow.setVisibility(View.INVISIBLE);
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

        Integer totalPics = totalmutualFollowerPics + totalCompetitionPic + totalpublicPictures;
        numPhotosCount.setText(totalPics.toString());


        if(competitionPictures != null){
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
        }

    }




    private void setMutualAdapter(String picUrl) {
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
    }


}
