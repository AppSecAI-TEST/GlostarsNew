package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class upload extends AppCompatActivity {


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

    ImageView image;
    RadioButton publicpost;
    RadioButton competition;
    RadioButton followerspost;
    EditText description;
    ImageView fbshare;
    ImageView vkshare;
    ImageView twshare;
    Button cancel;
    Button submit;

    private Bitmap bm;
    private MyUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        image = (ImageView)findViewById(R.id.imageUpload);
        publicpost = (RadioButton)findViewById(R.id.radiopublic);
        competition = (RadioButton)findViewById(R.id.radiocompetition);
        followerspost = (RadioButton)findViewById(R.id.radiofollowers);
        description = (EditText)findViewById(R.id.description);
        fbshare = (ImageView)findViewById(R.id.fbshare);
        vkshare = (ImageView)findViewById(R.id.vkshare);
        twshare = (ImageView)findViewById(R.id.twshare);
        cancel = (Button)findViewById(R.id.cancelbutton);
        submit = (Button)findViewById(R.id.submitbutton);


        submit.setTransformationMethod(null);
        cancel.setTransformationMethod(null);


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

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        submit.setTypeface(type);
        cancel.setTypeface(type);
        description.setTypeface(type);
        competition.setTypeface(type);
        publicpost.setTypeface(type);
        followerspost.setTypeface(type);


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
                startActivity(new Intent(upload.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, user_profile.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, competitionAll.class));
            }
        });

        //========================= HANDLING PICTURE ===============================================
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            bm = bundle.getParcelable("PREVIEW_PICTURE");
            image.setImageBitmap(bm);
        }

        mUser = MyUser.getmUser();
        mUser.setContext(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String privacy = "";
                if(publicpost.isChecked()){
                    privacy = "public";
                } else if(competition.isChecked()){
                    privacy = "competition";
                } else if(followerspost.isChecked()){
                    privacy = "followers";
                }

                if((bm != null) && (privacy != "")){
                    prepareUpload(description.getText().toString(), privacy, competition.isChecked(), mUser.getToken(), bm);
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void prepareUpload(String description, String privacy, Boolean isCompeting, String token, Bitmap bm) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        String picUri = Base64.encodeToString(byteArray, Base64.DEFAULT);
        picUri = "data:image/png;base64," + picUri;

        PictureService pictureService = new PictureService();
        try {
            //System.out.println("description: " + description);
            //System.out.println("privacy: " + privacy);
            //System.out.println("iscompeting: " + isCompeting);
            //System.out.println("uri: " + picUri);
            pictureService.uploadPicture(description, isCompeting, privacy, picUri, token);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
