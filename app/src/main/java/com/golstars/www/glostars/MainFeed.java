package com.golstars.www.glostars;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.adapters.CommentAdapter;
import com.golstars.www.glostars.adapters.PostAdapter;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.interfaces.OnRatingEventListener;
import com.golstars.www.glostars.models.Comment;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.NotificationService;
import com.golstars.www.glostars.network.PictureService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class MainFeed extends AppCompatActivity implements OnRatingEventListener, OnItemClickListener {


    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;


    TextView username;
    TextView caption;
    TextView postTime;
    TextView totalStars;
    TextView totalComments;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    ImageView propic;
    ImageView post;
    ImageView privacyIcon;

    com.github.clans.fab.FloatingActionButton homeFAB;
    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;

    FloatingActionMenu menuDown;


    View rootView;

    EmojIconActions emojIcon;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;

    boolean isOpen = false;

    // --------------- recycler view settings ---------
    private ArrayList<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    //-------------------------------------------------
    LinearLayoutManager layoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private MyUser mUser;
    int pg = 1;
    Integer unseenNotifs = 0;
    //-------------------------------------------------
    Intent userProfileIntent = new Intent();
    PullRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootView = findViewById(R.id.myRoot);
        layout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList.clear();
                pg=1;
                try {
                    //callAsyncPopulate(pg);
                    loadFeed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final String TAG = MainFeed.class.getSimpleName();



        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

        //---------------NETOWORK AND RECYCLER VIEW --------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.mainfeedrecycler);

        final Context context = MainFeed.this;

        mUser = MyUser.getmUser();
        mUser.setContext(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int Height = displayMetrics.heightPixels;
        int Width = displayMetrics.widthPixels;

        layoutManager = new LinearLayoutManager(this);

        mAdapter = new PostAdapter(postList, Width, mUser.getUserId(), getApplicationContext(), this, this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {
                /*
                Intent intent = new Intent();
                intent.putExtra("IMAGE_SAUCE", item.getPicURL());
                intent.putExtra("IMAGE_AUTHOR", item.getAuthor());
                intent.putExtra("IMAGE_CAPTION", item.getDescription());
                intent.setClass(getApplicationContext(), imagefullscreen.class);
                startActivity(intent);
                */

                ArrayList<Post> gridImages = new ArrayList<>();
                gridImages.add(0, item);

                Bundle bundle = new Bundle();
                bundle.putSerializable("images", gridImages);
                bundle.putInt("position", 0);
                bundle.putString("token", mUser.getToken());
                bundle.putString("usrID", mUser.getUserId());

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideShowDialogFragment newFragment = SlideShowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");

                // double click rates only one star to photo
                /*if(item != null){
                    JSONObject msg = new JSONObject();
                    try {
                        msg.put("NumOfStars", 1);
                        msg.put("PhotoId", item.getPhotoId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringEntity entity = null;
                    try {
                        entity = new StringEntity(msg.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    PictureService.ratePicture(getApplicationContext(), mUser.getToken(), entity, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            System.out.println(response);
                        }
                    });

                } */


            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {

            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClickPost(final Post item) {
                /* the following method opens a dialog box
                    containing the comments
                * */


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainFeed.this);
                View mView = getLayoutInflater().inflate(R.layout.commentdialog,null);

                //TextView commentsbanner = (TextView)mView.findViewById(R.id.commentbannerdialog);
                ListView commentrecycler  = (ListView)mView.findViewById(R.id.commentrecycler);
                ImageView emojibtn = (ImageView)mView.findViewById(R.id.emoji_btn);
                final EmojiconEditText commentbox = (EmojiconEditText)mView.findViewById(R.id.commentBox);
                final TextView sendcomment  = (TextView)mView.findViewById(R.id.sendcomment);

               // rootView = mView.findViewById(R.id.rootView);

                final ArrayList<Comment> commentsList = new ArrayList<>();
                final CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), R.layout.content_comment_model, commentsList,
                        new commentModel.BtnClickListener() {
                            @Override
                            public void onItemClick(Comment com) {
                                Intent intent = new Intent();
                                intent.putExtra("USER_ID", com.getCommenterId());
                                intent.setClass(getApplicationContext(), user_profile.class);
                                startActivity(intent);
                            }
                        });
                commentrecycler.setAdapter(commentAdapter);

                for(int i = 0; i < item.getComments().length(); i++){
                    try{
                        JSONObject com = item.getComments().getJSONObject(i);
                        Integer commentId = com.getInt("commentId");
                        String  commentMessage = com.getString("commentMessage");
                        String commenterUserName = com.getString("commenterUserName");
                        String commenterID = com.getString("commenterId");
                        String commentTime = com.getString("commentTime");
                        String profilePicUrl = com.getString("profilePicUrl");
                        String firstName = com.getString("firstName");
                        String lastName = com.getString("lastName");
                        Comment comment = new Comment(commentMessage, commenterUserName, commenterID, commentTime, profilePicUrl, firstName, lastName, commentId);
                        commentsList.add(comment);
                        commentAdapter.notifyDataSetChanged();

                    } catch (JSONException e){
                        e.printStackTrace();
                    }


                }


                //emojIcon = new EmojIconActions(MainFeed.this, rootView, commentbox, emojibtn);
                emojIcon = new EmojIconActions(mView.getContext(), mView, commentbox, emojibtn);

                //emojIcon.setUseSystemEmoji(true);
                //commentbox.setUsetUseSystemEmoji(true);
                //commentbox.setUseSystemDefault(true);

                emojIcon.ShowEmojIcon();


                emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
                    @Override
                    public void onKeyboardOpen() {
                        Log.e(TAG, "Keyboard opened!");
                    }

                    @Override
                    public void onKeyboardClose() {
                        Log.e(TAG, "Keyboard closed");
                    }
                });


                sendcomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = String.valueOf(commentbox.getText());
                        try {
                            postComment(item.getPhotoId(), comment, commentsList, commentAdapter, sendcomment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        commentbox.setText(""); ///
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();




            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {
                //deleteListener handler
                JSONObject msg = new JSONObject();
                try {
                    msg.put("", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                StringEntity entity = null;
                try {
                    entity = new StringEntity(msg.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                String picId = item.getPhotoId();
                PictureService.unratePicture(getApplicationContext(), mUser.getToken(), picId, entity, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println(errorResponse);
                    }
                });

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {

            }
        }, R.layout.content_main_feed);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        loadFeed();
        //new getUserData().execute("");
        //populateFeed(mUser.getUserId(), pg, mUser.getToken());

        /* checks whether the user has reached the end of the view
           and calls another page
        * */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                System.out.println("Scrolling "+dx+" "+dy);
                if(dy > 0){ //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    System.out.println("Total Item "+totalItemCount+" Loading "+loading);
                    if(!loading){
                        if((visibleItemCount + pastVisiblesItems) >= totalItemCount-2){
                            loading = true;
                            //pg++;
                            try {
                                //callAsyncPopulate(pg);
                                loadFeed();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //populateFeed(mUser.getUserId(), pg, mUser.getToken());
                        }
                    }
                }
            }
        });






        //--------------------------------------------------------------------------



        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.homeFAB);



        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDown.close(true);
            }
        });


        username=(TextView)findViewById(R.id.userNAME);
        caption=(TextView)findViewById(R.id.userCAPTION);
        postTime=(TextView)findViewById(R.id.uploadTIME);
        //totalStars=(TextView)findViewById(R.id.numStars);
        //totalComments=(TextView)findViewById(R.id.numComments);
        //comptext = (TextView)findViewById(R.id.comptext);

        post = (ImageView)findViewById(R.id.userPOST);
        propic = (ImageView)findViewById(R.id.userPIC);
        privacyIcon = (ImageView)findViewById(R.id.privacy);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);



        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this,searchResults.class));

            }
        });



        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this, upload.class));
                menuDown.close(true);
            }
        });


        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(userProfileIntent);
                menuDown.close(true);
            }
        });

        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this, notification.class));
                menuDown.close(true);
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFeed.this, competitionAll.class));
                menuDown.close(true);

            }
        });


        //profileFAB
        //getUnseen();






    }
    public void loadFeed(){

        String url = ServerInfo.BASE_URL_API+"images/mutualpic/" + mUser.getUserId() + "/" + pg;

        System.out.println(url);

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
        StringEntity stringEntity=null;
        try {
            stringEntity=new StringEntity("{ListPhoto:[]}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(getApplicationContext(), url,stringEntity,"application/json",new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    JSONObject resultPayload = response.getJSONObject("resultPayload");
                    JSONArray data = resultPayload.getJSONArray("data");
                    for(int i = 0; i < data.length(); ++i){
                        try {
                            JSONObject pic = data.getJSONObject(i);
                            JSONObject poster = pic.getJSONObject("poster");
                            String name = poster.getString("name");
                            String usrId = poster.getString("userId");
                            String profilePicUrl = poster.getString("profilePicURL");
                            String id = pic.getString("id");
                            String description = pic.getString("description");
                            String picURL = pic.getString("picUrl");

                            Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
                            Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
                            Integer starsCount = Integer.parseInt(pic.getString("starsCount"));
                            System.out.println("POSTER: " + name + " " + usrId + " " + id + " " + description + " " + picURL + " " + isFeatured + " " + isCompeting + " " + starsCount);

                            JSONArray ratings = pic.getJSONArray("ratings");
                            JSONArray comments = pic.getJSONArray("comments");

                            String uploaded = pic.getString("uploaded");
                            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);

                            setmAdapter(name, usrId, id, description, picURL, profilePicUrl , isFeatured, isCompeting, ratings.length(), comments.length(), ratings, comments, interval);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    layout.setRefreshing(false);
                    System.out.println("Loading complete");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public void getUnseen(){


        NotificationService.getNotifications(getApplicationContext(), mUser.getUserId(), mUser.getToken(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("resultPayload");
                    System.out.println(response);
                    JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                    JSONArray followerNotifications = data.getJSONArray("followerNotifications");
                    System.out.println(activityNotifications);
                    System.out.println(followerNotifications);


                    for(int i = 0; i < activityNotifications.length(); ++i){
                        if(activityNotifications.getJSONObject(i).getString("seen").equals("false")){
                            unseenNotifs++;
                        }
                    }

                    for(int i = 0; i < followerNotifications.length(); ++i){
                        if(followerNotifications.getJSONObject(i).getString("seen").equals("false")){
                            unseenNotifs++;
                        }

                    }

                    if(unseenNotifs > 0){
//                        mainbadge.setVisibility(View.VISIBLE);
//                        notificationbadge.setVisibility(View.VISIBLE);
//                        mainbadge.setText(unseenNotifs.toString());
//                        notificationbadge.setText(unseenNotifs.toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });




    }




    // Setup a recurring alarm every fifteen minutes
    private void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra("token", mUser.getToken());
        intent.putExtra("usrId", mUser.getUserId());

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    }


    private class getUserData extends AsyncTask<String, Integer, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            //userProfileIntent = new Intent();
            userProfileIntent.putExtra("USER_ID",mUser.getUserId());
            userProfileIntent.setClass(getApplicationContext(),user_profile.class);
            //setting up alarm to service
            scheduleAlarm();
            try {
                callAsyncPopulate(pg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    class populatePageAsync extends AsyncTask<Integer, Integer, JSONArray>{
        @Override
        protected JSONArray doInBackground(Integer... integers) {
            JSONArray data = null;
            PictureService pictureService = new PictureService();
            try {
                pictureService.getMutualPictures(mUser.getUserId(), integers[0], mUser.getToken());
                while(data == null){
                    data = pictureService.getData();
                }
                System.out.println("PICTURES: " + data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        protected void onPostExecute(JSONArray jsonArray) {
            populateFeed(jsonArray);
        }
    };

    void callAsyncPopulate(Integer pg) throws Exception {
        new populatePageAsync().execute(pg);

    }


    //private void populateFeed(String userId, int pg, String token) {
    private void populateFeed(JSONArray data){
        /*JSONArray data = null;
        PictureService pictureService = new PictureService();
        try {
            pictureService.getMutualPictures(userId, pg, token);
            while(data == null){
                data = pictureService.getData();
            }
            System.out.println("PICTURES: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        for(int i = 0; i < data.length(); ++i){
            try {
                JSONObject pic = data.getJSONObject(i);
                JSONObject poster = pic.getJSONObject("poster");
                String name = poster.getString("name");
                String usrId = poster.getString("userId");
                String profilePicUrl = poster.getString("profilePicURL");
                String id = pic.getString("id");
                String description = pic.getString("description");
                String picURL = pic.getString("picUrl");

                Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
                Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
                Integer starsCount = Integer.parseInt(pic.getString("starsCount"));
                String starsCnt = pic.getString("starsCount");
                System.out.println("POSTER: " + name + " " + usrId + " " + id + " " + description + " " + picURL + " " + isFeatured + " " + isCompeting + " " + starsCnt);

                JSONArray ratings = pic.getJSONArray("ratings");
                JSONArray comments = pic.getJSONArray("comments");

                String uploaded = pic.getString("uploaded");
                String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));
                String interval = Timestamp.getInterval(localDateTime);

                setmAdapter(name, usrId, id, description, picURL, profilePicUrl , isFeatured, isCompeting, starsCount, comments.length(), ratings, comments, interval);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(!loading){
            loading = true;
        }

    }

    private void setmAdapter(String author, String usr, String photoID, String description, String picURL, String profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount,
                             Integer commentCount, JSONArray ratings, JSONArray comments, String uploaded){
        if(description == "null"){
            description = "";
        }
        Post post = new Post(author, usr, photoID, description, picURL, profilePicURL, isFeatured, isCompeting, starsCount, commentCount);
        post.setComments(comments);
        post.setRatings(ratings);
        post.setUploaded(uploaded);
        postList.add(post);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRatingBarChange(Post item, float value, int postPosition){

        if(item != null){
            JSONObject msg = new JSONObject();
            try {
                msg.put("NumOfStars", (int)value);
                msg.put("PhotoId", item.getPhotoId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringEntity entity = null;
            try {
                entity = new StringEntity(msg.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            PictureService.ratePicture(getApplicationContext(), mUser.getToken(), entity, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println(response);
                }
            });

        }




    }

    @Override
    public void onItemClickPost(Post item) {
        //sends user id to the next activity before starting it
        Intent intent = new Intent();
        intent.putExtra("USER_ID", item.getUserId());
        intent.setClass(this, user_profile.class);
        startActivity(intent);
    }

    @Override
    public void onItemClickNotif(NotificationObj notif) {

    }


    void postComment(String picID, final String comment, final ArrayList commentsList, final CommentAdapter commentAdapter, final TextView commentbox) throws Exception{

        if(!comment.isEmpty()){
            //PictureService pictureService = new PictureService();
            //pictureService.commentPicture(picID, comment, mUser.getToken());
            final JSONObject res = null;
            /*while(res == null){
                res = pictureService.getDataObject();
            }*/

            String url = ServerInfo.BASE_URL + "api/images/comment";





            AsyncHttpClient client=new AsyncHttpClient();
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client.setSSLSocketFactory(sf);
            }
            catch (Exception e) {}
            RequestParams msg=new RequestParams();
            client.addHeader("Authorization", "Bearer " + mUser.getToken());
            msg.add("CommentText", comment);
            msg.add("PhotoId", picID);

            client.post(getApplicationContext(), url,msg,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Comment dummyComment = new Comment(
                            comment,
                            mUser.getName(),
                            mUser.getUserId(),
                            (new Date()).toString(),
                            mUser.getProfilePicURL(),
                            mUser.getName(),
                            "", 0);


                    try {
                        if(response.getInt("responseCode") == 1){
                            commentsList.add(dummyComment);
                            commentAdapter.notifyDataSetChanged();
                            commentbox.setText("");

                        } else Toast.makeText(MainFeed.this, "couldn't connect to the servers", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });



        } else {
            Toast.makeText(this, "write a message before sending", Toast.LENGTH_LONG).show();
        }


    }

    //-------------CAMERA AND GALLERY CALLERS------------------------------------------
    /**
     *  In this method we'll create a dialog box with three options
     *  for either camera, gallery or cancelling actions
     */

    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainFeed.this);
        builder.setTitle("Select Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                boolean result = Utility.checkPermission(MainFeed.this);

                if(items[item].equals("Take Photo")){
                    userChoosenTask = "Take Photo";
                    if(result)
                        cameraIntent();
                } else if(items[item].equals("Choose from Library")){
                    userChoosenTask = "Choose from Library";
                    if(result)
                        galleryIntent();
                } else if(items[item].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,  REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        File imageFile = null;
        if (data != null) {
            try {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if(selectedImagePath!=null){
                    System.out.println(selectedImagePath);
                    imageFile = new File(selectedImagePath);
                }
                else System.out.println("selectedImagePath is null");
                if(filemanagerstring!=null){
                    System.out.println(filemanagerstring);
                    imageFile = new File(filemanagerstring);
                    System.out.println(imageFile);
                }

                else System.out.println("filemanagerstring is null");





                //imageFile = new File(imagePath);
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Bundle filePath = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", bm);
        filePath.putSerializable("FILEPATH", imageFile);
        intent.putExtras(bundle);
        intent.putExtras(filePath);
        intent.setClass(this, upload.class);
        startActivity(intent);

        //ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Bundle filePath = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", thumbnail);
        filePath.putSerializable("FILEPATH", destination);
        intent.putExtras(bundle);
        intent.putExtras(filePath);
        intent.setClass(this, upload.class);
        startActivity(intent);

        //ivImage.setImageBitmap(thumbnail);
    }


    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }








    //-------------/CAMERA AND GALLERY CALLERS<end>------------------------------------------

}