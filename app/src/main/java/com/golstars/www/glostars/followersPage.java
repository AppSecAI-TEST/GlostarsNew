package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.golstars.www.glostars.R.id.p;
import static java.security.AccessController.getContext;

public class followersPage extends AppCompatActivity {

    ListView followersList;
    ListView followingList;


    ImageView followpropic;
    TextView surname;
    TextView lastname;

    Button followbutton;
    Button followerbut;
    Button followingbut;

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
    ImageView slogo;


    ArrayList<Follower> followers;
    ArrayList<Follower> following;

    FollowersAdapter followingAdaper;
    FollowersAdapter followersAdapter;

    JSONArray myFollowers;
    JSONArray myFollowing;

    String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final String TAG = followersPage.class.getName();

        followersList = (ListView)findViewById(R.id.followerList);
        followingList = (ListView)findViewById(R.id.followingList);


        followpropic = (ImageView)findViewById(R.id.propicfollow);
        surname = (TextView) findViewById(R.id.surnamefollow);
        lastname = (TextView)findViewById(R.id.namefollow);

        followbutton = (Button)findViewById(R.id.followBUT);
        followerbut = (Button)findViewById(R.id.followersbutuser);
        followingbut = (Button)findViewById(R.id.followingbutuser);
        //followbutton.setTransformationMethod(null);


        slogo = (ImageView)findViewById(R.id.searchlogo);



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


        followingbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    followingbut.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                    followerbut.setTextColor(getResources().getColor(R.color.darkGrey));

                    followingList.setVisibility(View.VISIBLE);
                    followersList.setVisibility(View.GONE);


            }
        });

        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(followersPage.this,searchResults.class));

            }
        });


        followerbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followingbut.setTextColor(getResources().getColor(R.color.darkGrey));

                followerbut.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                followingList.setVisibility(View.GONE);
                followersList.setVisibility(View.VISIBLE);

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


                    homebadge.setVisibility(View.GONE);
                    notificationbadge.setVisibility(View.GONE);
                    mainbadge.setVisibility(View.GONE);

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
                homeFAB.startAnimation(fab_hide);
                mainFAB.startAnimation(rotate_anticlockwise);

                cameraFAB.setClickable(false);
                competitionFAB.setClickable(false);
                profileFAB.setClickable(false);
                notificationFAB.setClickable(false);
                homeFAB.setClickable(false);
                isOpen=false;
                startActivity(new Intent(followersPage.this, MainFeed.class));
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
                startActivity(new Intent(followersPage.this, user_profile.class));
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
                startActivity(new Intent(followersPage.this, upload.class));
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
                startActivity(new Intent(followersPage.this, competitionAll.class));
            }
        });

        String mUserProfPic = this.getIntent().getStringExtra("myUserIdPic");
        String guestUserID  = this.getIntent().getStringExtra("guestUserId");
        mUserID = this.getIntent().getStringExtra("myUserId");
        String token =  this.getIntent().getStringExtra("token");

        followers = new ArrayList<>();
        following = new ArrayList<>();

        followersAdapter = new FollowersAdapter(this, R.layout.activity_followers_model, followers);
        followingAdaper = new FollowersAdapter(this, R.layout.activity_followers_model, following);

        followersList.setAdapter(followersAdapter);
        followingList.setAdapter(followingAdaper);

        LoadFollowers(guestUserID, token, mUserID);



    }

    public void LoadFollowers(String usr, String token, final String myID){
        FollowerService.LoadFollowers(this, myID, token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject resultPayload = response.getJSONObject("resultPayload");
                    myFollowers = resultPayload.getJSONArray("followerList");
                    myFollowing = resultPayload.getJSONArray("followingList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        FollowerService.LoadFollowers(this, usr, token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println(response);
                    JSONArray followerList = null;
                    JSONArray followingList = null;

                    JSONObject resultPayload = response.getJSONObject("resultPayload");
                    followerList = resultPayload.getJSONArray("followerList");
                    followingList = resultPayload.getJSONArray("followingList");

                   if((myFollowers != null)  && (myFollowing != null)){
                        for(int i = 0; i < followerList.length(); i++){
                            Follower follower = new Follower();
                            follower.setUserId(followerList.getJSONObject(i).getString("id"));

                            String name = followerList.getJSONObject(i).getString("name");
                            String surname = followerList.getJSONObject(i).getString("lastName");

                            follower.setUserName(name + " " + surname);
                            follower.setProfilePicture(followingList.getJSONObject(i).getString("profilemediumPath"));

                            if(follower.getUserId().equals(mUserID)){
                                follower.setRigStatus("");
                            } else{
                                follower.setStatus(myFollowers, myFollowing);
                            }



                            followers.add(follower);
                            followersAdapter.notifyDataSetChanged();



                        }

                       for(int i = 0; i < followingList.length(); i++){
                           Follower follower = new Follower();
                           follower.setUserId(followingList.getJSONObject(i).getString("id"));

                           String name = followingList.getJSONObject(i).getString("name");
                           String surname = followingList.getJSONObject(i).getString("lastName");

                           follower.setUserName(name + " " + surname);
                           follower.setProfilePicture(followingList.getJSONObject(i).getString("profilemediumPath"));

                           if(follower.getUserId().equals(mUserID)){
                               follower.setRigStatus("");
                           } else{
                               follower.setStatus(myFollowers, myFollowing);
                           }

                           following.add(follower);
                           followingAdaper.notifyDataSetChanged();


                       }
                   }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }




    private class FollowersAdapter extends ArrayAdapter<Follower> {

        ArrayList<Follower> followers;
        Context context;


        public FollowersAdapter(Context context, int resource, ArrayList<Follower> followers) {
            super(context, resource, followers);

            this.context = context;
            this.followers = followers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.activity_followers_model, null);
            }

            Follower p = getItem(position);

            if (p != null) {

                ImageView usrPic= (ImageView) v.findViewById(R.id.propicfollow);

                TextView name = (TextView) v.findViewById(R.id.namefollow);
                TextView surname = (TextView) v.findViewById(R.id.surnamefollow);
                Button follow = (Button) v.findViewById(R.id.followBUT);

                if (name != null) {
                    name.setText(p.getUserName());
                }

                if (surname != null) {
                    surname.setText("");
                }


                String fStatus = p.getStatus();
                follow.setText(fStatus);

                if(fStatus.equals("")){
                    follow.setVisibility(View.INVISIBLE);
                } else if(fStatus.equals("follower")){
                    follow.setBackgroundColor(Color.parseColor("#007FFF"));
                } else if(fStatus.equals("following")){
                    follow.setBackgroundColor(Color.parseColor("#E1C8FF"));
                } else if(fStatus.equals("mutual")){
                    follow.setBackgroundColor(Color.parseColor("#640064"));
                }

                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });




                Picasso.with(getApplicationContext()).load(p.getProfilePicture()).into(usrPic);
            }

            return v;
        }

    }

}
