package com.golstars.www.glostars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.adapters.RecyclerGridAdapter;
import com.golstars.www.glostars.interfaces.OnSinglePicClick;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class competitionUser extends AppCompatActivity implements OnSinglePicClick {

    //===========================FABS=========================================

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;


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

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;



    RecyclerView competitionusergrid;

    private ArrayList<String> targetList;
    private RecyclerGridAdapter targetAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        competitionusergrid = (RecyclerView) findViewById(R.id.competitionusergrid);

        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

         gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
         search = (EditText)findViewById(R.id.searchedit);

        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);

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

                startActivity(new Intent(competitionUser.this,searchResults.class));

            }
        });


        //==========================================================================================

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, user_profile.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionUser.this, competitionAll.class));
            }
        });


        //======================== DATA HANDLING =====================================
        MyUser mUser = MyUser.getmUser();
        mUser.setContext(this);
//        Picasso.with(this).load(mUser.getProfilePicURL()).into(profileFAB);

        targetList = new ArrayList<>();

        //targetAdapter = new GridAdapter(getApplicationContext(), targetList);
        targetAdapter = new RecyclerGridAdapter(this, targetList, this);
        int numOfColumns = 3;
        competitionusergrid.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        competitionusergrid.setAdapter(targetAdapter);

        String target = "";
        target = this.getIntent().getStringExtra("LOAD_TARGET");
        System.out.println(target);

        if(target == null) {
            System.out.println("something else happened here");
        }

        else if(target.equals("COMPETITION")){
            //if we have competition pics
            if(targetList.isEmpty()){
                ArrayList<String> aux = this.getIntent().getStringArrayListExtra("COMPETITION_PICS");
                System.out.println("target list - " + aux);
                for(int i = 0; i < aux.size(); i++){
                    targetList.add(aux.get(i));
                    targetAdapter.notifyDataSetChanged();
                }

            }

        }else if(target.equals("PUBLIC")){
            //if we have public
            if(targetList.isEmpty()){
                //targetList.clear();
                ArrayList<String> aux = this.getIntent().getStringArrayListExtra("PUBLIC_PICS");
                System.out.println("target list - " + aux);
                for(int i = 0; i < aux.size(); i++){
                    targetList.add(aux.get(i));
                    targetAdapter.notifyDataSetChanged();
                }
            }

        }else if(target.equals("MUTUAL")){
            //if we have mutual
            if(targetList.isEmpty()){
                //targetList.clear();
                ArrayList<String> aux = this.getIntent().getStringArrayListExtra("MUTUAL_PICS");
                System.out.println("target list - " + aux);
                for(int i = 0; i < aux.size(); i++){
                    targetList.add(aux.get(i));
                    targetAdapter.notifyDataSetChanged();
                }
            }

        }else if(target == null) {
            System.out.println("something else happened here");
        }


    }


    @Override
    public void onItemClick(String url, Integer pos) {

    }
}


