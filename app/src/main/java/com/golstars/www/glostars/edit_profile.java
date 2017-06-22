package com.golstars.www.glostars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.network.SearchUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

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

    private MyUser mUser;

    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView textChange;
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
    Button uploadbutton;



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

    FrameLayout editpicframe;

    Intent homeIntent;
    File selectedImage;
    private ImageView final_view;



    //<editor-fold desc="User Information">
    String Id;
    String AboutMe;
    String Interests;
    String  Name;
    String LastName;
    String Location;
    String Country;
    String Original_Location;
    String Original_Country;
    String  Ocupation;
    String OcupationOther;
    //</editor-fold>




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //<editor-fold desc="Easy Image Default Config">
        EasyImage.configuration(this)
                .setImagesFolderName("AOS") //images folder name, default is "EasyImage"
                //.saveInAppExternalFilesDir() //if you want to use root internal memory for storying images
                .saveInRootPicturesDirectory(); //if you want to use internal memory for storying images - default
        //</editor-fold>

        mUser = MyUser.getmUser();
        editpicframe = (FrameLayout) findViewById(R.id.editpicframe);


        editpicframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(edit_profile.this, 0);
            }
        });

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        textChange = (TextView) findViewById(R.id.textChange);


        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        editPic = (ImageView)findViewById(R.id.editpic);
        final_view = (ImageView)findViewById(R.id.final_view);
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


        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(edit_profile.this,MainFeed.class));
            }
        });


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

        //new setupUser().execute("");
        Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(editPic);
        loadUserInformation();
    }

    public void loadUserInformation(){

        //Picasso.with(getApplicationContext()).load(myUser.getProfilePicURL()).into(editPic);

        String url = ServerInfo.BASE_URL_API+"account/GetUserDetails?userId="+mUser.getUserId();

        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + mUser.getToken());
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {}
        client.get(this, url,new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. " + response.toString());

                    JSONObject jsonObject = response.getJSONObject("resultPayload");
                    Id=jsonObject.getString("id");
                    AboutMe=jsonObject.getString("aboutMe");
                    Interests=jsonObject.getString("interests");
                    Name=jsonObject.getString("name");
                    LastName=jsonObject.getString("lastName");
                    Location=jsonObject.getString("location");
                    Country=jsonObject.getString("country");
                    Original_Location=jsonObject.getString("original_Location");
                    Original_Country=jsonObject.getString("original_Country");
                    Ocupation=jsonObject.getString("ocupation");
                    OcupationOther=jsonObject.getString("ocupationOther");

                    firstname.setText(Name);
                    lastname.setText(LastName);
                    aboutme.setText(AboutMe);
                    interests.setText(Interests);
                    /*try {

                        *//*currentcity.setText(data.getString("location"));
                        if(!data.getString("original_Location").equals("null")) {
                            homecity.setText(data.getString("original_Location"));
                        }*//*





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                System.out.println("Selected File Is : " + imageFile.getAbsolutePath());
                selectedImage = imageFile;
                /*image.setImageURI(Uri.fromFile(imageFile));*/
                editPic.setVisibility(View.GONE);
                textChange.setVisibility(View.GONE);
                Picasso.with(edit_profile.this).load(selectedImage).fit().centerCrop().into(final_view);
                //editPic.setVisibility(View.VISIBLE);
                //Bitmap bitmap = BitmapFactory.decodeFile(imageFile);
            }
        });
    }
    public void updateProfile() {

        final ProgressDialog dialog = ProgressDialog.show(edit_profile.this, "","Profile updating. Please wait...", true);
        dialog.show();

        String url = ServerInfo.BASE_URL+"Home/Edit";
        AsyncHttpClient client = new AsyncHttpClient();


        client.addHeader("Authorization", "Bearer " + mUser.getToken());

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }catch (Exception e){

        }


        //Log.d("UPLOAD", "token: " + mUser.getToken());
        final RequestParams requestParams = new RequestParams();

        requestParams.put("Id", Id);
        requestParams.put("AboutMe", aboutme.getText().toString());
        requestParams.put("Interests", interests.getText().toString());
        requestParams.put("Name", firstname.getText().toString());
        requestParams.put("LastName", lastname.getText().toString());
        requestParams.put("Location", Location);
        requestParams.put("Country", Country);
        requestParams.put("Original_Location", Original_Location);
        requestParams.put("Original_Country", Original_Country);
        requestParams.put("Ocupation",Ocupation );
        requestParams.put("OcupationOther", OcupationOther);
        if(selectedImage!=null){
            try {
                requestParams.put("file", selectedImage);
                System.out.println("Successfully selected");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        /*try {
            System.out.println("file to load is: " + file);
            requestParams.put("file", file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        client.post(getApplicationContext(), url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                try {
                    if(response.getInt("responseCode")==1){

                       // JSONObject data=response.getJSONObject("resultPayload").getJSONObject("edited");


                        Toast.makeText(getApplicationContext(),  "Changes have been saved successfully", Toast.LENGTH_LONG).show();
                        Intent userProfileIntent=new Intent();
                        userProfileIntent.putExtra("USER_ID",mUser.getUserId());
                        userProfileIntent.setClass(getApplicationContext(),user_profile.class);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("something wrong 2. "+responseString);
                Toast.makeText(getApplicationContext(),  "Couldn't reach servers", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("something wrong 3. "+errorResponse);
                Toast.makeText(getApplicationContext(),  "Couldn't reach servers", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }




        });



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
//            Picasso.with(getApplicationContext()).load(myUser.getProfilePicURL()).into(profileFAB);

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
