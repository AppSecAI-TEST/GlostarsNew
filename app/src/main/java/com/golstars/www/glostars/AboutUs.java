package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutUs extends AppCompatActivity {


    TextView aboutus, aboutustext,offer , offertext, differ , differtext, ach, achtext;

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
