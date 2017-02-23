package com.golstars.www.glostars;

import android.app.Notification;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class notification extends AppCompatActivity {

    RelativeLayout notirelative;

    ImageView propic;
    ImageView postpreview;

    TextView name;
    TextView surname;
    TextView notibanner;
    TextView timebanner;
    TextView minsbanner;
    TextView hoursbanner;

    Button notification;
    Button followers;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    RecyclerView noti;
    RecyclerView foll;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notirelative = (RelativeLayout)findViewById(R.id.notimodelRL);

        propic = (ImageView) findViewById(R.id.propicNOTI);
        postpreview = (ImageView)findViewById(R.id.postpreviewNOTI);

        name = (TextView)findViewById(R.id.nameNOTI);
        surname = (TextView)findViewById(R.id.surnameNOTI);
        notibanner = (TextView)findViewById(R.id.notiBanner);
        timebanner = (TextView)findViewById(R.id.timeNOTI);
        minsbanner = (TextView)findViewById(R.id.minbannerNOTI);
        hoursbanner = (TextView)findViewById(R.id.hourbannerNOTI);

         gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
         search = (EditText)findViewById(R.id.searchedit);

        notification = (Button)findViewById(R.id.notificationbut);
        followers = (Button)findViewById(R.id.followersbut);

        noti = (RecyclerView)findViewById(R.id.notificationRecycler);
        foll = (RecyclerView)findViewById(R.id.followersRecycler);

          Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");

          notification.setTypeface(type);
          followers.setTypeface(type);
//        name.setTypeface(type);
//        surname.setTypeface(type);
//        notibanner.setTypeface(type);
//        timebanner.setTypeface(type);
//        minsbanner.setTypeface(type);
//        hoursbanner.setTypeface(type);

        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(showingFirst == true){
                    slogo.setBackground(getResources().getDrawable(R.drawable.search_active));
                    gl.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                }else{
                    slogo.setBackground(getResources().getDrawable(R.drawable.search));
                    gl.setVisibility(View.VISIBLE);
                    search.setVisibility(View.INVISIBLE);
                }

            }
        });






        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                followers.setTextColor(getResources().getColor(R.color.darkGrey));

                noti.setVisibility(View.VISIBLE);
                foll.setVisibility(View.GONE);

            }
        });


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.setTextColor(getResources().getColor(R.color.darkGrey));

                followers.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                noti.setVisibility(View.GONE);
                foll.setVisibility(View.VISIBLE);

            }
        });

        MyUser myUser = MyUser.getmUser();
        populateNotificationsList(myUser.getUserId(), myUser.getToken());




    }

    private void populateNotificationsList(String userId, String token) {
        NotificationService notif = new NotificationService();
        try {
            notif.getNotifications(userId, token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
