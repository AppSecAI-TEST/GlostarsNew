package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 2/21/2017.
 */

public class competitionFeed extends AppCompatActivity {

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;


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

    FloatingActionButton mainFAB;
    FloatingActionButton cameraFAB;
    FloatingActionButton competitionFAB;
    FloatingActionButton profileFAB;
    FloatingActionButton notificationFAB;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    private RecyclerView recyclerView;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.competitionfeedrecycler);

        mainFAB = (FloatingActionButton)findViewById(R.id.mainFAB);
        cameraFAB =(FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (FloatingActionButton)findViewById(R.id.notificationFAB);

        username=(TextView)findViewById(R.id.userNAME);
        caption=(TextView)findViewById(R.id.userCAPTION);
        postTime=(TextView)findViewById(R.id.uploadTIME);
        //totalStars=(TextView)findViewById(R.id.numStars);
        //totalComments=(TextView)findViewById(R.id.numComments);
        shareText=(TextView)findViewById(R.id.share);

        post = (ImageView)findViewById(R.id.userPOST);
        propic = (ImageButton)findViewById(R.id.userPIC);
        shareFB = (ImageView)findViewById(R.id.shareFB);
        shareTWITTER = (ImageView)findViewById(R.id.shareTWITTER);
        shareVK = (ImageView)findViewById(R.id.shareVK);
        privacyIcon = (ImageView)findViewById(R.id.privacy);

         gl = (ImageView)findViewById(R.id.glostarslogo);
         slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);



//        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
//        username.setTypeface(type);
//        caption.setTypeface(type);
//        postTime.setTypeface(type);
//        shareText.setTypeface(type);


        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);



        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(showingFirst == true){
                    slogo.setBackground(getResources().getDrawable(R.drawable.search_active));
                    gl.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                }else{
                    slogo.setBackground(getResources().getDrawable(R.drawable.search));
                    gl.setVisibility(View.VISIBLE);
                    search.setVisibility(View.INVISIBLE);
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
                    mainFAB.startAnimation(rotate_clockwise);

                    cameraFAB.setVisibility(View.VISIBLE);
                    competitionFAB.setVisibility(View.VISIBLE);
                    profileFAB.setVisibility(View.VISIBLE);
                    notificationFAB.setVisibility(View.VISIBLE);

                    cameraFAB.setClickable(true);
                    competitionFAB.setClickable(true);
                    profileFAB.setClickable(true);
                    notificationFAB.setClickable(true);
                    isOpen=true;




                }
            }
        });


        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionFeed.this, user_profile.class));
            }
        });

        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionFeed.this, notification.class));
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionFeed.this, imagefullscreen.class));
            }
        });





    }

}