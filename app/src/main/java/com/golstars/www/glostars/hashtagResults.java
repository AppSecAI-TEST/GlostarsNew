package com.golstars.www.glostars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class hashtagResults extends AppCompatActivity {

    RecyclerView hashtags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hashtags"); //Changing the activity label here.
        setContentView(R.layout.activity_hashtag_results);

        hashtags = (RecyclerView)findViewById(R.id.hashtagrecycler);


    }
}
