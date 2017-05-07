package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import org.w3c.dom.Text;

public class AboutUs extends AppCompatActivity {


    TextView aboutus, aboutustext,offer , offertext, differ , differtext, ach, achtext;

    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;
    com.github.clans.fab.FloatingActionButton homeFAB;

    FloatingActionMenu menuDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        aboutus = (TextView)findViewById(R.id.aboutusBanner);
        aboutustext = (TextView)findViewById(R.id.aboutustext);
        offer = (TextView)findViewById(R.id.offerBanner);
        offertext = (TextView)findViewById(R.id.offertext);
        differ = (TextView)findViewById(R.id.differenceBanner);
        differtext = (TextView)findViewById(R.id.differenceText);
        ach = (TextView)findViewById(R.id.achBanner);
        achtext = (TextView)findViewById(R.id.achText);


        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);



        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutUs.this, notification.class));
                menuDown.close(true);
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuDown.close(true);
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutUs.this,user_profile.class));
                menuDown.close(true);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutUs.this, upload.class));
                menuDown.close(true);
            }
        });

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUs.this,MainFeed.class));
            }
        });


        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        aboutustext.setTypeface(type);
        aboutus.setTypeface(type);
        offer.setTypeface(type);
        offertext.setTypeface(type);
        differ.setTypeface(type);
        differtext.setTypeface(type);
        ach.setTypeface(type);
        achtext.setTypeface(type);




    }
}
