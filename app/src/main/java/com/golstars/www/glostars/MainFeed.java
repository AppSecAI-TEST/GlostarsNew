package com.golstars.www.glostars;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainFeed extends AppCompatActivity {

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

    FloatingActionButton mainFAB ;
    FloatingActionButton cameraFAB;
    FloatingActionButton competitionFAB;
    FloatingActionButton profileFAB;
    FloatingActionButton notificationFAB;

    boolean isOpen = false;

    // --------------- recycler view settings ---------
    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    //-------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---------------NETOWORK AND RECYCLER VIEW --------------------------------
        //recyclerView = (RecyclerView) findViewById(R.id.)

        Context context = MainFeed.this;

        MyUser mUser = MyUser.getmUser();
        mUser.setContext(context);

        PictureService pictureService = new PictureService();
        try {
            pictureService.getMutualPictures(mUser.getUserId(), 1, mUser.getToken());
            JSONArray data = null;
            while(data == null){
                data = pictureService.getData();
            }
            System.out.println("PICTURES: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter = new PostAdapter(postList);
        //--------------------------------------------------------------------------

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

        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


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


    }
}
