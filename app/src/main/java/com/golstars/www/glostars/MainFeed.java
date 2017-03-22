package com.golstars.www.glostars;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;


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
    TextView shareText;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    ImageView propic;
    ImageView post;
    ImageView shareFB;
    ImageView shareTWITTER;
    ImageView shareVK;
    ImageView privacyIcon;

    FloatingActionButton mainFAB ;
    FloatingActionButton cameraFAB;
    FloatingActionButton competitionFAB;
    ImageView profileFAB;
    FloatingActionButton notificationFAB;

    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    TextView comptext;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;

    boolean isOpen = false;

    // --------------- recycler view settings ---------
    private List<Post> postList = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //---------------NETOWORK AND RECYCLER VIEW --------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.mainfeedrecycler);

        Context context = MainFeed.this;

        mUser = MyUser.getmUser();
        mUser.setContext(this);

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
        });
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
                    System.out.println("CHECKING SCROLL DOWN");

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






        //--------------------------------------------------------------------------


        mainFAB = (FloatingActionButton)findViewById(R.id.mainFAB);
        cameraFAB =(FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (ImageView) findViewById(R.id.profileFAB);
        notificationFAB = (FloatingActionButton)findViewById(R.id.notificationFAB);

        homebadge = (TextView)findViewById(R.id.homebadge);
        notificationbadge = (TextView)findViewById(R.id.notificationbadge);
        profilebadge = (TextView)findViewById(R.id.profilebadge);
        camerabadge = (TextView)findViewById(R.id.uploadbadge);
        mainbadge = (TextView)findViewById(R.id.mainbadge);
        competitionbadge = (TextView)findViewById(R.id.competitionbadge);

        username=(TextView)findViewById(R.id.userNAME);
        caption=(TextView)findViewById(R.id.userCAPTION);
        postTime=(TextView)findViewById(R.id.uploadTIME);
        //totalStars=(TextView)findViewById(R.id.numStars);
        //totalComments=(TextView)findViewById(R.id.numComments);
        shareText=(TextView)findViewById(R.id.share);
        comptext = (TextView)findViewById(R.id.comptext);

        post = (ImageView)findViewById(R.id.userPOST);
        propic = (ImageView)findViewById(R.id.userPIC);
        shareFB = (ImageView)findViewById(R.id.shareFB);
        shareTWITTER = (ImageView)findViewById(R.id.shareTWITTER);
        shareVK = (ImageView)findViewById(R.id.shareVK);
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

        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen) {
                    cameraFAB.startAnimation(fab_hide);
                    competitionFAB.startAnimation(fab_hide);
                    profileFAB.startAnimation(fab_hide);
                    notificationFAB.startAnimation(fab_hide);
                    mainFAB.startAnimation(rotate_anticlockwise);

                    homebadge.setVisibility(View.GONE);
                    notificationbadge.setVisibility(View.GONE);
                    mainbadge.setVisibility(View.GONE);

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

        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(userProfileIntent);
            }
        });

        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(new Intent(MainFeed.this, notification.class));
            }
        });

        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(new Intent(MainFeed.this, competitionAll.class));

            }
        });


        //profileFAB
        if(unseenNotifs > 0){
            mainbadge.setVisibility(View.VISIBLE);
            notificationbadge.setVisibility(View.VISIBLE);
            mainbadge.setText(unseenNotifs.toString());
            notificationbadge.setText(unseenNotifs.toString());

        }
        getUnseen();




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
            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
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
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", bm);
        intent.putExtras(bundle);
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




    //-------------/CAMERA AND GALLERY CALLERS<end>------------------------------------------

}