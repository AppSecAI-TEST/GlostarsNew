package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.golstars.www.glostars.models.Comment;
import com.golstars.www.glostars.network.PictureService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class commentModel extends AppCompatActivity {

    ImageView commentpic;
    TextView namecomment;
    EmojiconTextView comment;
    ImageView emojiImageView;
    TextView hours;
    TextView mins;
    TextView time;

    EmojiconEditText commentbox;
    TextView sendcomment;
    EmojIconActions emojIcon;
    View rootView;

    ListView commentlistView;
    ArrayList<Comment> commentsList;
    ListAdapter commentAdapter;



    MyUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_model);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         final String TAG = commentModel.class.getSimpleName();

        commentpic = (ImageView)findViewById(R.id.commentpic);
        namecomment = (TextView)findViewById(R.id.namecomment);
        comment = (EmojiconTextView) findViewById(R.id.comment);
        hours = (TextView)findViewById(R.id.hourcomment);
        mins = (TextView)findViewById(R.id.commentmins);
        time = (TextView)findViewById(R.id.timecomment);
        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);

        rootView = findViewById(R.id.content_comment_model);

        commentlistView = (ListView)findViewById(R.id.commentslist);

        commentbox = (EmojiconEditText) findViewById(R.id.commentBox);
        sendcomment = (TextView) findViewById(R.id.sendcomment);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
//        comment.setTypeface(type);
//        hours.setTypeface(type);
//        mins.setTypeface(type);
//        time.setTypeface(type);
        sendcomment.setTypeface(type);
        commentbox.setTypeface(type);
//        namecomment.setTypeface(type);

        //====================== EMOJIS=============================================================
        emojIcon = new EmojIconActions(this, rootView, commentbox, emojiImageView);
        emojIcon.ShowEmojIcon();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
        //==========================================================================================

        commentsList = new ArrayList<>();
        commentAdapter = new ListAdapter(this, R.layout.content_comment_model, commentsList, new BtnClickListener(){
            @Override
            public void onItemClick(Comment com) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", com.getCommenterId());
                intent.setClass(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }
        });
        commentlistView.setAdapter(commentAdapter);

        Intent intent = getIntent();
        String comments = intent.getStringExtra("COMMENTS");

        try {
            JSONArray commentList = new JSONArray(comments);
            populateList(commentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(comments);

        mUser = MyUser.getmUser();
        new getUser().execute("");
        //mUser.setContext(this);

        sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                String picID = i.getStringExtra("PICID");

                try {
                    postComment(picID, commentbox.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });

    }

    void postComment(String picID,  String comment) throws Exception{

        if(!comment.isEmpty()){
            PictureService pictureService = new PictureService();
            pictureService.commentPicture(picID, comment, mUser.getToken());
            JSONObject res = null;
            while(res == null){
                res = pictureService.getDataObject();
            }

            Comment dummyComment = new Comment(
                    comment,
                    mUser.getName(),
                    mUser.getUserId(),
                    (new Date()).toString(),
                    mUser.getProfilePicURL(),
                    mUser.getName(),
                    "", 0);



        if(res.getInt("responseCode") == 1){
            commentsList.add(dummyComment);
            commentAdapter.notifyDataSetChanged();
            commentbox.setText("");

        } else Toast.makeText(this, "couldn't connect to the servers", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "write a message before sending", Toast.LENGTH_LONG).show();
        }


    }



    void populateList(JSONArray data) throws Exception {
        for(int i = 0; i < data.length(); i++){
            JSONObject com = data.getJSONObject(i);
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

        }

    }

    private class getUser extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            mUser.setContext(getApplicationContext());
            return null;
        }
    }

    public class ListAdapter extends ArrayAdapter<Comment> {

        //private List<Comment>;
        //public Context context;

        private BtnClickListener mClickListener = null;

        public ListAdapter(Context context, int resource, List<Comment> items, BtnClickListener btnClickListener) {
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

            final Comment comm = getItem(position);

            Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");


            if (comm != null) {
                TextView namecomment = (TextView) v.findViewById(R.id.namecomment);
                ImageView commentPic = (ImageView) v.findViewById(R.id.commentpic);
                EmojiconTextView  comment = (EmojiconTextView) v.findViewById(R.id.comment);
                TextView hours = (TextView) v.findViewById(R.id.hourcomment);

                namecomment.setTypeface(type);
                comment.setTypeface(type);
                hours.setTypeface(type);

                Picasso.with(getApplicationContext()).load(comm.getProfilePicUrl()).into(commentPic);
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
        void onItemClick(Comment com);
    }


}
