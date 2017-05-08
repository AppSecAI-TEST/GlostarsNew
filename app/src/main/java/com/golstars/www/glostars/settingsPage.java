package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class settingsPage extends AppCompatActivity {

    TextView logout;
    TextView aboutus;
    TextView faq;
    TextView dataprivacy;
    TextView contactus;
    TextView Terms;

    com.github.clans.fab.FloatingActionButton homeFAB;
    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;

    FloatingActionMenu menuDown;

    MyUser mUser;
    Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new getUserData().execute("");

        aboutus = (TextView)findViewById(R.id.aboutus);
        faq = (TextView)findViewById(R.id.FAQ);
        dataprivacy = (TextView)findViewById(R.id.dataprivacy);
        contactus = (TextView)findViewById(R.id.contactus);
        Terms = (TextView)findViewById(R.id.termsset);
        logout = (TextView)findViewById(R.id.logoutset);




        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);




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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getApplicationContext().getSharedPreferences("glostarsPrefs", Context.MODE_PRIVATE).edit().clear().commit(); // delete all saved data
                Auth auth = new Auth(getApplicationContext());
                System.out.println("ACCESS TOKEN AFTER LOGOUT " + auth.getAcessToken());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
//

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this,MainFeed.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this, upload.class));
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

        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this, notification.class));
                menuDown.close(true);
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settingsPage.this, competitionAll.class));
                menuDown.close(true);

            }
        });

        if(!isConnected()){
            startActivity(new Intent(this, noInternet.class));
        }


    }

    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
//            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("male")){
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("female")){
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);

        }
    }

    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }

}
