package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class commentModel extends AppCompatActivity {

    ImageView commentpic;
    TextView namecomment;
    TextView comment;
    TextView hours;
    TextView mins;
    TextView time;

    EditText commentbox;
    Button sendcomment;

    ListView commentlistView;
    ArrayList<Comment> commentsList;
    ListAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_model);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        commentpic = (ImageView)findViewById(R.id.commentpic);
        namecomment = (TextView)findViewById(R.id.namecomment);
        comment = (TextView)findViewById(R.id.comment);
        hours = (TextView)findViewById(R.id.hourcomment);
        mins = (TextView)findViewById(R.id.commentmins);
        time = (TextView)findViewById(R.id.timecomment);

        commentlistView = (ListView)findViewById(R.id.commentslist);

        commentbox = (EditText)findViewById(R.id.commentBox);
        sendcomment = (Button)findViewById(R.id.sendcomment);

        /*Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        comment.setTypeface(type);
        hours.setTypeface(type);
        mins.setTypeface(type);
        time.setTypeface(type);
        sendcomment.setTypeface(type);
        namecomment.setTypeface(type); */


        commentsList = new ArrayList<>();
        commentAdapter = new ListAdapter(this, R.layout.content_comment_model, commentsList);
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

    public class ListAdapter extends ArrayAdapter<Comment> {

        //private List<Comment>;
        //public Context context;

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);

        }

        public ListAdapter(Context context, int resource, List<Comment> items) {
            super(context, resource, items);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.comment_model, null);
            }

            Comment comm = getItem(position);

            if (comm != null) {
                TextView namecomment = (TextView) v.findViewById(R.id.namecomment);
                ImageView commentPic = (ImageView) v.findViewById(R.id.commentpic);
                TextView  comment = (TextView) v.findViewById(R.id.comment);
                TextView hours = (TextView) v.findViewById(R.id.hourcomment);

                Picasso.with(getApplicationContext()).load(comm.getProfilePicUrl()).into(commentPic);
                comment.setText(comm.getCommentMessage());
                //hours.setText("2");
                namecomment.setText(comm.getFirstName() + " " + comm.getLastName());
            }

            return v;
        }

    }


}
