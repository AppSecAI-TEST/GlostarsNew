package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class user_profile extends AppCompatActivity {


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

    TextView numPhotosCount;

    ImageView userPicProfile;
    ImageView weeklyPrizeProfile;
    ImageView monthlyPrizeProfile;
    ImageView exhibitionPrizeProfile;



    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;


    Button follow;

    GridView competitiongrid;
    GridView publicgrid;
    GridView mutualgrid;

    private GridView compGridView;
    private ArrayList<String> compImgsUrls;
    private GridAdapter compAdapter;

    private GridView publicGridView;
    private ArrayList<String> publicImgsUrls;
    private GridAdapter publicAdapter;

    private ArrayList<String> mutualImgsUrls;
    private GridAdapter mutualAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        competitiongrid = (GridView)findViewById(R.id.competitionPosts);
        publicgrid = (GridView)findViewById(R.id.publicPosts);
        mutualgrid = (GridView)findViewById(R.id.mutualPosts);


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


        usernameProfile = (TextView)findViewById(R.id.profileuserNAME);
        userLocationProfile = (TextView)findViewById(R.id.profileuserLOCATION);
        aboutMeBannerProfile = (TextView)findViewById(R.id.useraboutBANNER);
        aboutMeTextProfile = (TextView)findViewById(R.id.useraboutTEXT);
        interestBannerProfile = (TextView)findViewById(R.id.userinterestBANNER);
        interestTextProfile = (TextView)findViewById(R.id.userinterestTEXT);
        recognitionBannerProfile =(TextView)findViewById(R.id.recognitionBanner);
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





        userPicProfile = (ImageView) findViewById(R.id.userPIC);
        weeklyPrizeProfile = (ImageView)findViewById(R.id.weeklyPrize);
        monthlyPrizeProfile = (ImageView)findViewById(R.id.monthlyPrize);
        exhibitionPrizeProfile = (ImageView)findViewById(R.id.exhibitionPrize);

        follow = (Button)findViewById(R.id.profileuserFOLLOW);


        //-------------------------- ADAPTER AND NETWORK SETTINGS ---------------------------------//

        compImgsUrls = new ArrayList<>();
        publicImgsUrls = new ArrayList<>();
        mutualImgsUrls = new ArrayList<>();

        compGridView = (GridView) findViewById(R.id.competitionPosts);
        compAdapter = new GridAdapter(this, compImgsUrls);
        competitiongrid.setAdapter(compAdapter); //adapter for competition pictures

        //publicgrid = (GridView) findViewById(R.id.publicPosts);
        publicAdapter = new GridAdapter(this, publicImgsUrls);
        publicgrid.setAdapter(publicAdapter);

        mutualAdapter = new GridAdapter(this, mutualImgsUrls);
        mutualgrid.setAdapter(mutualAdapter);




        Context context = user_profile.this;
        MyUser mUser = MyUser.getmUser();
        mUser.setContext(context);

        usernameProfile.setText(mUser.getName());
        try {
            populateGallery(mUser.getUserId(), 1, mUser.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        seeAllCompetitionProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("COMPETITION_PICS", compImgsUrls);
                intent.putExtra("LOAD_TARGET", "COMPETITION");
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), competitionUser.class);
                startActivity(intent);

                //startActivity(new Intent(user_profile.this, competitionUser.class));

            }
        });

        seeAllPublicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("PUBLIC_PICS", publicImgsUrls);
                intent.putExtra("LOAD_TARGET", "PUBLIC");
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), competitionUser.class);
                startActivity(intent);
            }
        });



        seeAllMutualProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("MUTUAL_PICS", mutualImgsUrls);
                intent.putExtra("LOAD_TARGET", "MUTUAL");
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), competitionUser.class);
                startActivity(intent);

                startActivity(new Intent(user_profile.this, competitionUser.class));

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
                startActivity(new Intent(user_profile.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, user_profile.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, competitionAll.class));
            }
        });

        Picasso.with(this).load(mUser.getProfilePicURL()).into(profileFAB);







    }

    private void populateGallery(String userId, int pg, String token) throws JSONException {
        JSONObject data = null;
        PictureService pictureService = new PictureService();

        try {
            pictureService.getUserPictures(userId, 1, token);
            while(data == null){
                data = pictureService.getDataObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        compAdapter.notifyDataSetChanged();
        //
    }


}
