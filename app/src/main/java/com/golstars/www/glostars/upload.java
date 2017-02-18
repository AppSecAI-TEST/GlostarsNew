package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class upload extends AppCompatActivity {


    ImageView image;
    RadioButton publicpost;
    RadioButton competition;
    RadioButton followerspost;
    EditText description;
    ImageView fbshare;
    ImageView vkshare;
    ImageView twshare;
    Button cancel;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        image = (ImageView)findViewById(R.id.imageUpload);
        publicpost = (RadioButton)findViewById(R.id.radiopublic);
        competition = (RadioButton)findViewById(R.id.radiocompetition);
        followerspost = (RadioButton)findViewById(R.id.radiofollowers);
        description = (EditText)findViewById(R.id.description);
        fbshare = (ImageView)findViewById(R.id.fbshare);
        vkshare = (ImageView)findViewById(R.id.vkshare);
        twshare = (ImageView)findViewById(R.id.twshare);
        cancel = (Button)findViewById(R.id.cancelbutton);
        submit = (Button)findViewById(R.id.submitbutton);



    }

}
