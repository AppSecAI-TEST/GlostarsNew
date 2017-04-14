package com.golstars.www.glostars;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.adapters.PostAdapter;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.interfaces.OnRatingEventListener;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.PictureService;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2/21/2017.
 */

public class competitionFeed extends AppCompatActivity implements OnRatingEventListener, OnItemClickListener {

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



    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    // --------------- recycler view settings ---------
    private ArrayList<Post> postList = new ArrayList<>();
    private PostAdapter mAdapter;
    //-------------------------------------------------
    LinearLayoutManager layoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private MyUser mUser;
    int pg = 1;
    boolean goPic = false;
    //-------------------------------------------------
    Intent userProfileIntent;


    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.competitionfeedrecycler);



        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);

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

                startActivity(new Intent(competitionFeed.this,searchResults.class));

            }
        });



        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionFeed.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(userProfileIntent);
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionFeed.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(competitionFeed.this, competitionAll.class));
            }
        });


        mUser = MyUser.getmUser();
        userProfileIntent = new Intent();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int Height = displayMetrics.heightPixels;
        int Width = displayMetrics.widthPixels;

        layoutManager = new LinearLayoutManager(this);

        mAdapter = new PostAdapter(postList, Width, mUser.getUserId(), this, this, this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {
                Intent intent = new Intent();
                intent.putExtra("IMAGE_SAUCE", item.getPicURL());
                intent.putExtra("IMAGE_AUTHOR", item.getAuthor());
                intent.putExtra("IMAGE_CAPTION", item.getDescription());
                intent.setClass(getApplicationContext(), imagefullscreen.class);
                startActivity(intent);
            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {

            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

                Intent intent = new Intent();

                intent.putExtra("COMMENTS", item.getComments().toString());
                intent.putExtra("PICID", item.getPhotoId());
                intent.setClass(getApplicationContext(), commentModel.class);
                startActivity(intent);
            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {

            }
        }, R.layout.content_main_feed);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        new getUserData().execute("");
        //populateFeed(mUser.getUserId(), pg, mUser.getToken());

        /* checks wether the user has reached the end of the view
           and calls another page
        * */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){ //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if(loading){
                        if((visibleItemCount + pastVisiblesItems) >= totalItemCount){
                            loading = false;
                            pg++;
                            try {
                                callAsyncPopulate(pg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //populateFeed(mUser.getUserId(), pg, mUser.getToken());
                        }
                    }
                }
            }
        });













    }

    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
            //userProfileIntent = new Intent();
            userProfileIntent.putExtra("USER_ID",mUser.getUserId());
            userProfileIntent.setClass(getApplicationContext(),user_profile.class);
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
                //pictureService.getMutualPictures(mUser.getUserId(), integers[0], mUser.getToken());
                pictureService.getCompetitionPictures(integers[0], mUser.getToken());
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
        if(!loading){
            loading = true;
        }

    }

    private void setmAdapter(String author, String usr, String photoID, String description, String picURL,
                             String profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount,
                             Integer commentCount, JSONArray ratings, JSONArray comments, String interval){
        if(description == "null"){
            description = "";
        }
        Post post = new Post(author, usr, photoID, description, picURL, profilePicURL, isFeatured, isCompeting, starsCount, commentCount);
        post.setComments(comments);
        post.setRatings(ratings);
        post.setUploaded(interval);
        postList.add(post);

        mAdapter.notifyDataSetChanged();


        if(mAdapter.getItemCount() > 9 && !goPic){
            String picUrl = this.getIntent().getStringExtra("GOTOPIC");
            Integer pos = mAdapter.findPosByPhotoURL(picUrl);
            if(pos > 0){
                recyclerView.scrollToPosition(pos);
                goPic = true;
            }

        } else if((mAdapter.getItemCount() % 10) == 0 && !goPic){
            String picUrl = this.getIntent().getStringExtra("GOTOPIC");
            Integer pos = mAdapter.findPosByPhotoURL(picUrl);
            if(pos > 0){
                recyclerView.scrollToPosition(pos);
                goPic = true;
            } else {
                pg++;
                try {
                    callAsyncPopulate(pg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRatingBarChange(Post item, float value, int postPosition){
        System.out.println("RATING BAR");
        JSONArray ratings = item.getRatings();
        if(mUser.getUserId() != null){
            JSONObject rating = new JSONObject();
            try {
                rating.put("starsCount", (int)value);
                rating.put("raterId", mUser.getUserId());
                rating.put("ratingTime", (new Date()).toString());
                ratings.put(rating);
                item.setRatings(ratings);
                System.out.println("my id is " + mUser.getUserId());
                System.out.println(item.getRatings());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            postList.set(postPosition,item);
            mAdapter.notifyDataSetChanged();

        }


        //postList.set(postPosition,)


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



}