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

import org.w3c.dom.Text;

public class settingsPage extends AppCompatActivity {

    TextView logout;
    TextView aboutus;
    TextView faq;
    TextView dataprivacy;
    TextView contactus;
    TextView Terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aboutus = (TextView)findViewById(R.id.aboutus);
        faq = (TextView)findViewById(R.id.FAQ);
        dataprivacy = (TextView)findViewById(R.id.dataprivacy);
        contactus = (TextView)findViewById(R.id.contactus);
        Terms = (TextView)findViewById(R.id.termsset);
        logout = (TextView)findViewById(R.id.logoutset);




        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        aboutus.setTypeface(type);
        faq.setTypeface(type);
        dataprivacy.setTypeface(type);
        contactus.setTypeface(type);
        Terms.setTypeface(type);
        logout.setTypeface(type);



//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Do your thing .
//            }
//        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this,AboutUs.class));
            }
        });

        Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this,termsOfUse.class));
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this , FAQs.class));
            }
        });

        dataprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this , dataPrivacy.class));
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this, ContactUs.class));
            }
        });



    }

}
