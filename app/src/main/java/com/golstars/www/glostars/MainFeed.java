package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String MyPREFERENCES = "glostarsPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Context context = MainFeed.this;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        MyUser mUser = MyUser.getmUser();
        mUser.setContext(context);

        PictureService pictureService = new PictureService();

        try {
            pictureService.getMutualPictures(mUser.getUserId(), 1, mUser.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }


        username=(TextView)findViewById(R.id.userNAME);
        caption=(TextView)findViewById(R.id.userCAPTION);
        postTime=(TextView)findViewById(R.id.uploadTIME);
        totalStars=(TextView)findViewById(R.id.numStars);
        totalComments=(TextView)findViewById(R.id.numComments);
        shareText=(TextView)findViewById(R.id.share);
        post = (ImageView)findViewById(R.id.userPOST);

        post = (ImageView)findViewById(R.id.userPOST);
        propic = (ImageButton)findViewById(R.id.userPIC);
        shareFB = (ImageView)findViewById(R.id.shareFB);
        shareTWITTER = (ImageView)findViewById(R.id.shareTWITTER);
        shareVK = (ImageView)findViewById(R.id.shareVK);
        privacyIcon = (ImageView)findViewById(R.id.privacy);


    }


}
