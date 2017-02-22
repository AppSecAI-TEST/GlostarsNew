package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class commentModel extends AppCompatActivity {

    ImageView commentpic;
    TextView namecomment;
    TextView comment;
    TextView hours;
    TextView mins;
    TextView time;

    EditText commentbox;
    Button sendcomment;

    ListView commentlist;

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

        commentlist = (ListView)findViewById(R.id.commentslist);

        commentbox = (EditText)findViewById(R.id.commentBox);
        sendcomment = (Button)findViewById(R.id.sendcomment);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        comment.setTypeface(type);
        hours.setTypeface(type);
        mins.setTypeface(type);
        time.setTypeface(type);
        sendcomment.setTypeface(type);
        namecomment.setTypeface(type);



    }

}
