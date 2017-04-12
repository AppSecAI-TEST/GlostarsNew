package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.adapters.NotificationAdapter;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.NotificationService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class notification extends AppCompatActivity implements OnItemClickListener {


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

    FloatingActionMenu menuDown;


    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    //===================================================================

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

    List<NotificationObj> notifs;
    List<NotificationObj> follNotifs;
    NotificationAdapter mAdapter;
    NotificationAdapter follAdapter;
    Integer unseenNotifs = 0;

    MyUser mUser;
    Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

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

          Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");

          notification.setTypeface(type);
          followers.setTypeface(type);


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notification.this,searchResults.class));

            }
        });

        notification.setTransformationMethod(null);
        followers.setTransformationMethod(null);




        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(homeIntent);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notification.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notification.this, competitionAll.class));
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

                setUserNotifSeen();

            }
        });

        //============================== NETWORK SERVICE HANDLING ========================================
        notifs = new ArrayList<>();
        follNotifs = new ArrayList<>();

        mUser = MyUser.getmUser(this);

        mAdapter = new NotificationAdapter(notifs, this, this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", notif.getOriginID());
                intent.setClass(getApplicationContext(),user_profile.class);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        noti.setLayoutManager(layoutManager);
        noti.setItemAnimator(new DefaultItemAnimator());
        noti.setAdapter(mAdapter);

        follAdapter = new NotificationAdapter(follNotifs, this, this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", notif.getOriginID());
                intent.setClass(getApplicationContext(),user_profile.class);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutM = new LinearLayoutManager(getApplicationContext());
        foll.setLayoutManager(layoutM);
        foll.setItemAnimator(new DefaultItemAnimator());
        foll.setAdapter(follAdapter);



//        RecyclerView noti;
//        RecyclerView foll;
        new getUserData().execute("");

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
            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
            try {
                populateNotificationsList(mUser.getUserId(), mUser.getToken());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);

        }
    }

    private void setActivityNotifSeen(){

        StringEntity entity = null;
        try {
            entity = new StringEntity("{}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationService.activityNotifSeen(getApplicationContext(), mUser.getToken(),entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
        } );
    }

    private void setUserNotifSeen(){
        StringEntity entity = null;
        try {
            entity = new StringEntity("{}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationService.userNotifsSeen(getApplicationContext(), mUser.getToken(), entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
        });

    }


    private void populateNotificationsList(String userId, String token) throws JSONException {

            NotificationService.getNotifications(this,userId, token, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    try {
                        JSONObject data = response.getJSONObject("resultPayload");
                        System.out.println(response);
                        JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                        JSONArray followerNotifications = data.getJSONArray("followerNotifications");
                        System.out.println(activityNotifications);
                        System.out.println(followerNotifications);

                        for(int i = 0; i < activityNotifications.length(); ++i){
                            JSONObject singleNotif = activityNotifications.getJSONObject(i);
                            String description = singleNotif.getString("description");
                            String profilePicURL = singleNotif.getString("profilePicURL");
                            String name = singleNotif.getString("name");
                            String id = singleNotif.getString("id");
                            String usrId = singleNotif.getString("userId");
                            String originatedById = singleNotif.getString("originatedById");
                            String pictureId = singleNotif.getString("pictureId");
                            Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                            Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                            String Seen = singleNotif.getString("seen");
                            String picURL = singleNotif.getString("picUrl");

                            String date = singleNotif.getString("date");
                            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);

                            if (Seen.equals("false")){
                               unseenNotifs ++;
                            }

                            setActivityNotifsAdapter(description, profilePicURL, name, id, usrId, originatedById, pictureId, seen, interval, picURL, checked);
                        }

                        for(int i = 0; i < followerNotifications.length(); ++i){
                            JSONObject singleNotif = followerNotifications.getJSONObject(i);
                            String description = "started following you";
                            String profilePicURL = singleNotif.getString("profilePicURL");
                            String name = singleNotif.getString("name");
                            String usrId = singleNotif.getString("userId");
                            String originatedById = singleNotif.getString("originatedById");
                            Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                            Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                            String Seen = singleNotif.getString("seen");

                            String date = singleNotif.getString("date");
                            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);

                            if (Seen.equals("false")){
                                unseenNotifs ++;
                            }

                            setActivityNotifSeen();

                            setFollowerNotifsAdapter(description, profilePicURL, name, "", usrId, originatedById, null, seen, interval, checked);

                        }
                        if(unseenNotifs > 0){
                            mainbadge.setVisibility(View.VISIBLE);
                            notificationbadge.setVisibility(View.VISIBLE);
                            mainbadge.setText(unseenNotifs.toString());
                            notificationbadge.setText(unseenNotifs.toString());

                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }




        });

        /*NotificationService notif = new NotificationService();
        JSONObject data = null;
        try {
            notif.getNotifications(userId, token);
            while (data == null){
                data = notif.getDataObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/









    }

    private void setFollowerNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, Boolean checked) {
        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, null, seen, checked);
        notif.setDate(date);
        follNotifs.add(notif);
        follAdapter.notifyDataSetChanged();
    }

    private void setActivityNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, String picURL, Boolean checked) {
        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, picURL, seen, checked);
        notif.setDate(date);
        notifs.add(notif);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickPost(Post item) {

    }

    @Override
    public void onItemClickNotif(NotificationObj notif) {
        if(!(notif.getDescription() == "started following you")){
            Intent intent = new Intent();
            intent.putExtra("IMAGE_SAUCE",notif.getPicURL());
            intent.putExtra("IMAGE_AUTHOR", mUser.getName());
            intent.putExtra("IMAGE_CAPTION","");
            intent.setClass(getApplicationContext(), imagefullscreen.class);
            startActivity(intent);
        }
    }
}
