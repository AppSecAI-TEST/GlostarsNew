package com.golstars.www.glostars;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Notification;
import com.golstars.www.glostars.adapters.NotificationAdapter;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.network.NotificationService;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class notificationNew extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    com.github.clans.fab.FloatingActionButton homeFAB;
    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;

    FloatingActionMenu menuDown;

    MyUser mUser;
    Intent homeIntent;
    ImageView slogo;
    Integer unseenNotifs = 0;
    Handler handler = new Handler();


    notificationFollowersNew nfN = new notificationFollowersNew();
    notificationNotificationNew nnN = new notificationNotificationNew();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_new);



        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);

        menuDown = (com.github.clans.fab.FloatingActionMenu)findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        slogo = (ImageView)findViewById(R.id.searchlogo);



        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notificationNew.this,MainFeed.class));
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notificationNew.this,competition_page.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeIntent != null){
                    startActivity(homeIntent);
                }
            }
        });

        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notificationNew.this,upload.class));
            }
        });


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notificationNew.this,searchResults.class));
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(mUser == null){
            new getUserData().execute("");
        }
        LoadServer();

    }
    public void LoadServer(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        HubConnection connection = new HubConnection(ServerInfo.BASE_URL);
        HubProxy hub = connection.createHubProxy("GlostarsHub");

        final MyUser me=MyUser.getmUser();
        System.out.println("server Token "+me.getToken());

        Credentials credentials=new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("Authorization", "Bearer " + me.getToken());
                // request.addHeader("authorization","bearer rLQd1-q-5CdheRQ5l5envaEZfdTWpPX4RIzvTDelURIw6ITegpEbD1U6XZrVrYcODUGYA8pH16vc4MXA_XHONtvJDkCEQahDDksw-oxBENuZH4k0F7vm0rtgdoRm89xwqSlu119JA2BBuHTg1apVo9vv8YSN3ke7SAR8Hzz_QFJ3m4tu-PFlatsrANLdRQAWxxuMYlPUSMG_Crfd46JJVa-h9Yvgz0pPs2oYDFsOJqp54wUsFLPOhnSGD-kp2rOvm16kOx9Uz3qxBai_pYYPmbzvr_e5d-pvRxGqQFMVtXr2wl8Ar-2_eUjqwCMDNmh3AMEF5s7lUOxSn9q3c59Qaf7cSd6KWfop9pclbMqJFQITwK9bXe_5V676_r1cHwEdY-nf97gM8t0TuGCxmJlV2RvgRx1oYMHpeS1NNZcHVITu2bpyP1eoE-9lrx80-Sd8gRGYeAC0QwpNHG8BQRbSmv3D0B683f1Z_r1EkgTjwGs");
            }
        };
        connection.setCredentials(credentials);
        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
        } catch (InterruptedException e) {
            // Handle ...
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       /* try {
            hub.invoke("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*hub.on("updatePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println(o);



                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());
                        for (int i = 0; i < postList.size(); i++) {
                            System.out.println(postList.get(i).getId()+"--"+hashtag.getId());
                            if(postList.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");
                                postList.set(i,hashtag);
                                mAdapter.notifyDataSetChanged();



                                break;
                            }
                        }

                    }
                });

            }
        },String.class);*/


        hub.on("picNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("picNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            System.out.println("Enter to thread...");
                            Gson gson=new Gson();
                            Notification notification=gson.fromJson(o,Notification.class);

                            NotificationObj notif = new NotificationObj(notification.originatedById+"", notification.pictureId+"", notification.description,
                                    notification.name, notification.profilePicURL, notification.picUrl, notification.seen, notification.checked);
                            notif.setDate( Timestamp.getInterval(Timestamp.getOwnZoneDateTime(notification.date)));
                            nnN.addNotification(notif);
                            System.out.println("After Added notification "+notif.toString());

                        } catch (Exception e) {
                            //System.out.println("Exception in pic notification");
                            e.printStackTrace();
                        }
                    }
                });

            }
        },String.class);

        hub.on("followerNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("followerNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            Gson gson=new Gson();
                            Notification notification=gson.fromJson(o,Notification.class);

                            NotificationObj notif = new NotificationObj(notification.originatedById+"", notification.pictureId+"", "started following you",
                                    notification.name, notification.profilePicURL, notification.picUrl, notification.seen, notification.checked);
                            notif.setDate( Timestamp.getInterval(Timestamp.getOwnZoneDateTime(notification.date)));
                            System.out.println("After Added notification "+notif.toString());
                            nfN.addNotification(notif);
                        } catch (Exception e) {
                            System.out.println("Exception in follower notification");
                        }


                    }
                });

            }
        },String.class);

        hub.on("SeenPictureNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("SeenPictureNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notificationNew.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notificationNew.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);

        hub.on("SeenFollowerNotification",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("SeenFollowerNotification "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notificationNew.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notificationNew.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    }
                });

            }
        },String.class);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_competition_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_competition_page, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return nnN;
                case 1:

                    return nfN;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Notifications";
                case 1:
                    return "Followers";

            }
            return null;
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


            //setting user default pic on FAB MENU
            if(mUser.getSex().equals("Male")){
                System.out.println("MY USER'S GENDER IS : " + mUser.getSex());
                profileFAB.setImageResource(R.drawable.nopicmale);
            } else if(mUser.getSex().equals("Female")){
                System.out.println("MY USER'S GENDER IS : " + mUser.getSex());
                profileFAB.setImageResource(R.drawable.nopicfemale);
            }

            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getApplicationContext(),user_profile.class);
            getUnseen();
            //load(false);


        }
    }

    public void getUnseen() {


        NotificationService.getNotifications(getApplicationContext(), mUser.getUserId(), mUser.getToken(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("resultPayload");
                    System.out.println(response);

                    JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                    JSONArray followerNotifications = data.getJSONArray("followerNotifications");

                    System.out.println(activityNotifications);
                    System.out.println(followerNotifications);


                    for (int i = 0; i < activityNotifications.length(); ++i) {
                        if (activityNotifications.getJSONObject(i).getString("seen").equals("false")) {
                            unseenNotifs++;
                        }
                    }

                    for (int i = 0; i < followerNotifications.length(); ++i) {
                        if (followerNotifications.getJSONObject(i).getString("seen").equals("false")) {
                            unseenNotifs++;
                        }

                    }

                    if (unseenNotifs > 0) {
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


}
