package com.golstars.www.glostars;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by admin on 2/13/2017.
 */

public class ProfilePosts extends AppCompatActivity{

    GridView postGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileposts);

        MyUser mUser = MyUser.getmUser();
        ArrayList<Bitmap> imgs = null;
        mUser.setContext(this);

        PictureService pictureService = new PictureService();
        DownloadImageTask downloadImageTask = new DownloadImageTask();

        try {
            pictureService.getUserPictures(mUser.getUserId(), 1, mUser.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        postGrid = (GridView)findViewById(R.id.postgrid);

        postGrid.setAdapter(new ImageAdapter(this, imgs));
        postGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getBaseContext(),+ i+" th image clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
}
