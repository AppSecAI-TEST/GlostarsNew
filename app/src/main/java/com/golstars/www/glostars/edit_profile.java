package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class edit_profile extends AppCompatActivity {

    //===========================FABS=========================================

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;


    FloatingActionButton mainFAB ;
    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;
    com.github.clans.fab.FloatingActionButton homeFAB;

    FloatingActionMenu menuDown;



    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    //===================================================================

    EditText firstname;
    EditText lastname;
    EditText aboutme;
    EditText interests;
    EditText currentcity;
    EditText homecity;
    EditText oldpass;
    EditText newpass;
    EditText confirmpass;

    Button save;
    Button cancel;

    TextView changepass;

    ImageView slogo;
    EditText search;
    ImageView gl;
    ImageView editPic;
    boolean showingFirst = true;

    MyUser myUser;


    Spinner currentcountry;
    Spinner homecountry;
    Spinner occupation;
    ArrayAdapter<CharSequence> occupationadapter;

    Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        firstname = (EditText)findViewById(R.id.firstnameedit);
        lastname = (EditText)findViewById(R.id.lastnameedit);
        aboutme = (EditText)findViewById(R.id.aboutmeedit);
        interests = (EditText)findViewById(R.id.interestedit);

        currentcity = (EditText)findViewById(R.id.currentcity);
        homecity = (EditText)findViewById(R.id.homecity);

        save = (Button)findViewById(R.id.savebutton);
        cancel = (Button)findViewById(R.id.cancelbutton);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        editPic = (ImageView)findViewById(R.id.editpic);
        search = (EditText)findViewById(R.id.searchedit);


        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        firstname.setTypeface(type);
        lastname.setTypeface(type);
        aboutme.setTypeface(type);
        interests.setTypeface(type);
        currentcity.setTypeface(type);
        homecity.setTypeface(type);

        currentcountry = (Spinner) findViewById(R.id.currentcountryspinner);
        homecountry = (Spinner) findViewById(R.id.homecountryspinner);
        occupation = (Spinner)findViewById(R.id.occupationspinner);

        occupationadapter = ArrayAdapter.createFromResource(this,R.array.occupation,android.R.layout.simple_spinner_item);
        occupationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        occupation.setAdapter(occupationadapter);

//        mainFAB = (FloatingActionButton)findViewById(R.id.mainFAB);
        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.homeFAB);

        //=============Notification Badges===============================================
//        homebadge = (TextView)findViewById(R.id.homebadge);
//        notificationbadge = (TextView)findViewById(R.id.notificationbadge);
//        profilebadge = (TextView)findViewById(R.id.profilebadge);
//        camerabadge = (TextView)findViewById(R.id.uploadbadge);
//        mainbadge = (TextView)findViewById(R.id.mainbadge);
//        competitionbadge = (TextView)findViewById(R.id.competitionbadge);




        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(edit_profile.this,searchResults.class));

            }
        });




        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, countries);
        homecountry.setAdapter(adapter);
        currentcountry.setAdapter(adapter);



        //==========================================================================================

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(edit_profile.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(edit_profile.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(homeIntent);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(edit_profile.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(edit_profile.this, competitionAll.class));
            }
        });

        new setupUser().execute("");

    }
    /*

    private void bindToUI(){

        SearchUser.searchUsrInfo(getApplicationContext(), myUser.getUserId(), myUser.getToken(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println(response);
                    JSONObject data = response.getJSONObject("resultPayload");
                    firstname.setText(myUser.getName());
                    //lastname.setText();
                    aboutme.setText(data.getString("aboutMe"));
                    interests.setText(data.getString("interests"));
                    currentcity.setText(data.getString("location"));
                    homecity.setText(data.getString("original_Location"));
                    Picasso.with(getApplicationContext()).load(myUser.getProfilePicURL()).into(editPic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }
    */

    private class setupUser extends AsyncTask<String, Integer, JSONObject>{


        @Override
        protected JSONObject doInBackground(String... strings) {
            myUser = MyUser.getmUser();
            myUser.setContext(getApplicationContext());
            JSONObject data = null;
            SearchUser searchUser = new SearchUser();
            try {
                searchUser.findUserInfo(myUser.getUserId(), myUser.getToken());
                while(data == null){
                    data = searchUser.getDataObj();
                }

                System.out.println(data);



            } catch (Exception e) {
                e.printStackTrace();
            }



            return data;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            firstname.setText(myUser.getName());
            Picasso.with(getApplicationContext()).load(myUser.getProfilePicURL()).into(editPic);
            Picasso.with(getApplicationContext()).load(myUser.getProfilePicURL()).into(profileFAB);

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID", myUser.getUserId());
            homeIntent.setClass(getApplicationContext(), user_profile.class);


            try {
                firstname.setText(data.getString("name"));
                if(!data.getString("lastName").equals("null")){
                    lastname.setText(data.getString("lastName"));
                }
                if(!data.getString("aboutMe").equals("null")){
                    aboutme.setText(data.getString("aboutMe"));
                }
                if(!data.getString("interests").equals("null")){
                    interests.setText(data.getString("interests"));
                }
                if(!data.getString("location").equals("null")){
                    currentcity.setText(data.getString("location"));
                }
                if(!data.getString("original_Location").equals("null")) {
                    homecity.setText(data.getString("original_Location"));
                }





            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
