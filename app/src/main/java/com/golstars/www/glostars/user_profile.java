package com.golstars.www.glostars;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class user_profile extends AppCompatActivity {


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
    TextView weeklyPrizeCountProfile;
    TextView monthlyPrizeCountProfile;
    TextView exhibitionPrizeCountProfile;

    ImageButton userPicProfile;
    ImageView weeklyPrizeProfile;
    ImageView monthlyPrizeProfile;
    ImageView exhibitionPrizeProfile;


    Button follow;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        usernameProfile = (TextView)findViewById(R.id.userNAME);
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


        userPicProfile = (ImageButton)findViewById(R.id.userPIC);
        weeklyPrizeProfile = (ImageView)findViewById(R.id.weeklyPrize);
        monthlyPrizeProfile = (ImageView)findViewById(R.id.monthlyPrize);
        exhibitionPrizeProfile = (ImageView)findViewById(R.id.exhibitionPrize);

        follow = (Button)findViewById(R.id.profileuserFOLLOW);

        usernameProfile.setText("something");

        Context context = user_profile.this;
        MyUser mUser = MyUser.getmUser();
        mUser.setContext(context);

        System.out.println(mUser.getName());
        System.out.println(mUser.getProfilePicURL());
        DownloadImageTask downloadImageTask = new DownloadImageTask();

        String url = mUser.getProfilePicURL();
        String name = "";
        name = mUser.getName();

        try {
            downloadImageTask.getImage(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bm = null;
        while(bm == null){
            bm = downloadImageTask.getData();

        }

        System.out.println(bm);











    }

}
