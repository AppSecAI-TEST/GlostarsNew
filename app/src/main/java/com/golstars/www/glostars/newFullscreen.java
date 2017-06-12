package com.golstars.www.glostars;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Poster;
import com.golstars.www.glostars.models.Comment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class newFullscreen extends AppCompatActivity {

    ImageView newFullscreenPost;
    RecyclerView picturepreview;
    TextView fullscreenUsername;
    TextView fullscreenUploadTime;
    ImageView fullscreenPrivacy;
    ImageView fullscreenProflePic;
    Button newFullscreenFollow;
    TextView newFullscreenCaption;
    RatingBar newRating;
    TextView newRatingCount;
    ImageView newCommentIcon;
    TextView newCommentCount;
    ListView newFullscreenComments;
    EmojiconEditText newCommentArea;
    TextView newSendComment;
    ImageView emojiButton;
    EmojIconActions emojIcon;
    View rootView;

    Integer position;
    String token;
    String usrID;
    //private ArrayList<Hashtag> postData;
    private Hashtag postData;
    private Poster poster;
    RecyclerView.Adapter postDataAdapter;
    MyUser mUser;
    Typeface type;

    List<com.golstars.www.glostars.ModelData.Comment> commentsList;
    ListAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fullscreen);

        newFullscreenPost = (ImageView)findViewById(R.id.newfullscreenimage);             // image url
        picturepreview = (RecyclerView)findViewById(R.id.previewPictures);               // miniature pics
        fullscreenUsername = (TextView)findViewById(R.id.newFullscreenUsername);        // user name
        fullscreenUploadTime = (TextView)findViewById(R.id.newFullscreenUploadTime);   // timestamp
        fullscreenPrivacy = (ImageView)findViewById(R.id.newFullscreenPrivacy);       // privacy img
        fullscreenProflePic = (ImageView)findViewById(R.id.newFullscreenProfilePic); // profile pic
        newFullscreenFollow = (Button)findViewById(R.id.newFullscreenFollowButton); // follow button
        newFullscreenCaption = (TextView)findViewById(R.id.newFullscreenCaption);  // caption
        newRating = (RatingBar)findViewById(R.id.newFullscreenRating);                          // rating bar
        newRatingCount = (TextView)findViewById(R.id.newFullscreenRatingCount);                // rating count
        newCommentIcon = (ImageView)findViewById(R.id.newFullscreenCommentIcon);              // comment icon
        newCommentCount = (TextView)findViewById(R.id.newFullscreenCommentCount);            // comments count
        newFullscreenComments = (ListView)findViewById(R.id.newFullscreenComments);         // comments
        newCommentArea = (EmojiconEditText) findViewById(R.id.newFullscreenCommentArea);   //
        newSendComment = (TextView)findViewById(R.id.newFullscreenSendComment);
        emojiButton = (ImageView) findViewById(R.id.emojiButton);
        rootView = (View) findViewById(R.id.rootViewFullscreen);



        type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        fullscreenUsername.setTypeface(type);
        fullscreenUploadTime.setTypeface(type);
        newFullscreenCaption.setTypeface(type);
        newRatingCount.setTypeface(type);
        newCommentCount.setTypeface(type);
        newSendComment.setTypeface(type);
        newCommentArea.setTypeface(type);

        newFullscreenFollow.setTransformationMethod(null);

        token = getIntent().getExtras().getString("token");
        usrID = getIntent().getExtras().getString("usrID");
        //position = getIntent().getExtras().getInt("position");



        setData();







        emojIcon = new EmojIconActions(newFullscreen.this,rootView,newCommentArea,emojiButton);
        emojIcon.ShowEmojIcon();

        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });




    }

    private void setData() {
        //Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");


        //postData=((AdapterInfomation)getApplicationContext()).getAllData(;
        //postDataAdapter=((AdapterInfomation)getApplicationContext()).getAdapter();
        postData =  getIntent().getExtras().getParcelable("post");
        poster = getIntent().getExtras().getParcelable("poster");

        picturepreview.setVisibility(View.GONE);

        commentsList = postData.getComments();
        commentAdapter = new ListAdapter(this, R.layout.content_comment_model, commentsList, new BtnClickListener(){
            @Override
            public void onItemClick(com.golstars.www.glostars.ModelData.Comment com) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", com.getCommenterId());
                intent.setClass(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }

        });
        newFullscreenComments.setAdapter(commentAdapter);



        fullscreenUsername.setText(poster.getName());
        fullscreenUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), user_profile.class);
                intent.putExtra("USER_ID",poster.getUserId());

                getApplicationContext().startActivity(intent);
            }
        });

        fullscreenUploadTime.setText(postData.getUploaded());   // timestamp

        Picasso.with(getApplicationContext()).load(poster.getProfilePicURL()).into(fullscreenProflePic);  // profile pic
        fullscreenProflePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), user_profile.class);
                intent.putExtra("USER_ID",poster.getUserId());

                getApplicationContext().startActivity(intent);
            }
        });

        Picasso.with(getApplicationContext()).load(postData.getPicUrl()).into(newFullscreenPost); // image url
        newFullscreenCaption.setText(postData.getDescription());  // caption

        /**************** privacy icon settings ***********************************/
        if(postData.isIsCompeting()){
            fullscreenPrivacy.setImageResource(R.drawable.privacy_competition_photo);
        } else {
            if(postData.getPrivacy().equals("public")) {
                fullscreenPrivacy.setImageResource(R.drawable.privacy_public_photo);
            } else if (postData.getPrivacy().equals("followers") | postData.getPrivacy().equals("friends")) {
                fullscreenPrivacy.setImageResource(R.drawable.privacy_mutual_follower_photo);
            }
        }

        /**************** follow button settings ***********************************/
        if(postData.is_mutual()){
            newFullscreenFollow.setText("Mutual");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }else if(postData.isMe_follow()){
            newFullscreenFollow.setText("Following");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }else if(postData.isHe_follow()){
            newFullscreenFollow.setText("Follower");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }else{
            newFullscreenFollow.setText("Follow");
            newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
            newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            newFullscreenFollow.setTransformationMethod(null);
            newFullscreenFollow.setTypeface(type);
        }

        newFullscreenFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Integer selected=new Integer(position);


                if(newFullscreenFollow.getText().toString().equalsIgnoreCase("follower") || newFullscreenFollow.getText().toString().equalsIgnoreCase("follow")){
                    String url = ServerInfo.BASE_URL_FOLLOWER_API+"Following/"+poster.getUserId();
                    AsyncHttpClient client=new AsyncHttpClient();
                    try {
                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);
                        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                        client.setSSLSocketFactory(sf);
                    }
                    catch (Exception e) {}
                    MyUser myUser=MyUser.getmUser();
                    client.addHeader("Authorization", "Bearer " + myUser.getToken());
                    RequestParams requestParams=new RequestParams();

                    client.post(getApplicationContext(), url,requestParams,new JsonHttpResponseHandler(){


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                System.out.println("1. "+response.toString());
                                if(response.getJSONObject("resultPayload").getBoolean("result")){
                                    if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                        newFullscreenFollow.setText("Mutual");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(true);
                                        postData.setHe_follow(true);
                                        postData.setMe_follow(true);

                                    }
                                    else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                        newFullscreenFollow.setText("Following");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(false);
                                        postData.setHe_follow(false);
                                        postData.setMe_follow(true);

                                    }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                        newFullscreenFollow.setText("Follower");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(false);
                                        postData.setHe_follow(true);
                                        postData.setMe_follow(false);
                                    }else{
                                        newFullscreenFollow.setText("Follow");
                                        newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.roundedbuttongrey));
                                        newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                        newFullscreenFollow.setTransformationMethod(null);
                                        newFullscreenFollow.setTypeface(type);

                                        postData.setIs_mutual(false);
                                        postData.setHe_follow(false);
                                        postData.setMe_follow(false);
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(DialogInterface.BUTTON_POSITIVE==which){
                                String url = ServerInfo.BASE_URL_FOLLOWER_API+"Unfollowing/"+poster.getUserId();
                                AsyncHttpClient client=new AsyncHttpClient();
                                try {
                                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                    trustStore.load(null, null);
                                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                    client.setSSLSocketFactory(sf);
                                }
                                catch (Exception e) {}
                                MyUser myUser=MyUser.getmUser();
                                client.addHeader("Authorization", "Bearer " + myUser.getToken());
                                RequestParams requestParams=new RequestParams();

                                client.post(getApplicationContext(), url,requestParams,new JsonHttpResponseHandler(){


                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            System.out.println("1. "+response.toString());
                                            if(response.getJSONObject("resultPayload").getBoolean("result")) {
                                                if (response.getJSONObject("resultPayload").getBoolean("is_mutual")) {
                                                    newFullscreenFollow.setText("Mutual");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(true);
                                                    postData.setHe_follow(true);
                                                    postData.setMe_follow(true);

                                                } else if (response.getJSONObject("resultPayload").getBoolean("me_follow")) {
                                                    newFullscreenFollow.setText("Following");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(false);
                                                    postData.setHe_follow(false);
                                                    postData.setMe_follow(true);

                                                } else if (response.getJSONObject("resultPayload").getBoolean("he_follow")) {
                                                    newFullscreenFollow.setText("Follower");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(false);
                                                    postData.setHe_follow(true);
                                                    postData.setMe_follow(false);
                                                } else {
                                                    newFullscreenFollow.setText("Follow");
                                                    newFullscreenFollow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedbuttongrey));
                                                    newFullscreenFollow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                    newFullscreenFollow.setTransformationMethod(null);
                                                    newFullscreenFollow.setTypeface(type);

                                                    postData.setIs_mutual(false);
                                                    postData.setHe_follow(false);
                                                    postData.setMe_follow(false);
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                dialog.dismiss();
                            }

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Unfollow", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();

                }
            }

        });





        //picturepreview = (RecyclerView)findViewById(R.id.previewPictures);               // miniature pics
//
//        newRating = (RatingBar)findViewById(R.id.newFullscreenRating);                          // rating bar
//        newRatingCount = (TextView)findViewById(R.id.newFullscreenRatingCount);                // rating count
//        newCommentIcon = (ImageView)findViewById(R.id.newFullscreenCommentIcon);              // comment icon
//        newCommentCount = (TextView)findViewById(R.id.newFullscreenCommentCount);            // comments count
//        newFullscreenComments = (ListView)findViewById(R.id.newFullscreenComments);         // comments




    }


    public class ListAdapter extends ArrayAdapter<com.golstars.www.glostars.ModelData.Comment> {

        //private List<Comment>;
        //public Context context;

        private BtnClickListener mClickListener = null;

        public ListAdapter(Context context, int resource, List<com.golstars.www.glostars.ModelData.Comment> items, BtnClickListener btnClickListener) {
            super(context, resource, items);
            mClickListener = btnClickListener;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.comment_model, null);
            }

            final com.golstars.www.glostars.ModelData.Comment comm = getItem(position);

            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");


            if (comm != null) {
                TextView namecomment = (TextView) v.findViewById(R.id.namecomment);
                ImageView commentPic = (ImageView) v.findViewById(R.id.commentpic);
                EmojiconTextView comment = (EmojiconTextView) v.findViewById(R.id.comment);
                TextView hours = (TextView) v.findViewById(R.id.hourcomment);

                namecomment.setTypeface(type);
                comment.setTypeface(type);
                hours.setTypeface(type);

                if(comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/male.jpg") || comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/Male.jpg")){
                    commentPic.setImageResource(R.drawable.nopicmalegrey);

                } else if(comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/female.jpg") || comm.getProfilePicUrl().equals("/Content/Profile/Thumbs/Female.jpg")){
                    commentPic.setImageResource(R.drawable.nopicfemalegrey);
                }else{

                    Picasso.with(getApplicationContext()).load(comm.getProfilePicUrl()).into(commentPic);

                }


                //Picasso.with(getApplicationContext()).load(comm.getProfilePicUrl()).into(commentPic);//
                comment.setText(comm.getCommentMessage());
                //hours.setText("2");
                namecomment.setText(comm.getFirstName() + " " + comm.getLastName());

                commentPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("pic clicked");
                        if (mClickListener != null){
                            mClickListener.onItemClick(comm);
                        }

                        //

                    }
                });

                namecomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("pic clicked");
                        if (mClickListener != null){
                            mClickListener.onItemClick(comm);
                        }
                    }
                });


            }

            return v;
        }

    }

    public interface BtnClickListener{
        void onItemClick(com.golstars.www.glostars.ModelData.Comment com);
    }

    /*
    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {

            setData();


        }
    } */
}
