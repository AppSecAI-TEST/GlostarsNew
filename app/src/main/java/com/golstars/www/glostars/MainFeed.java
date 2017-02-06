package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainFeed extends AppCompatActivity {


    TextView username;
    TextView caption;
    TextView postTime;
    TextView totalStars;
    TextView totalComments;
    TextView shareText;
    ImageButton propic;
    ImageView post;
    ImageView shareFB;
    ImageView shareTWITTER;
    ImageView shareVK;
    ImageView privacyIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        username=(TextView)findViewById(R.id.userNAME);
        caption=(TextView)findViewById(R.id.userCAPTION);
        postTime=(TextView)findViewById(R.id.uploadTIME);
        totalStars=(TextView)findViewById(R.id.numStars);
        totalComments=(TextView)findViewById(R.id.numComments);
        shareText=(TextView)findViewById(R.id.share);

        propic = (ImageButton)findViewById(R.id.userPIC);
        shareFB = (ImageView)findViewById(R.id.shareFB);
        shareTWITTER = (ImageView)findViewById(R.id.shareTWITTER);
        shareVK = (ImageView)findViewById(R.id.shareVK);
        privacyIcon = (ImageView)findViewById(R.id.privacy);


    }
}
