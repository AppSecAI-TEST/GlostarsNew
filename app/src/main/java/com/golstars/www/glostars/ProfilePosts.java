package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by admin on 2/13/2017.
 */

public class ProfilePosts extends AppCompatActivity{

    GridView postGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileposts);

        postGrid = (GridView)findViewById(R.id.postgrid);
        postGrid.setAdapter(new ImageAdapter(this));
        postGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getBaseContext(),+ i+" th image clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
}
