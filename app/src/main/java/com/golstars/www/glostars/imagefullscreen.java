package com.golstars.www.glostars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class imagefullscreen extends AppCompatActivity {

    ImageView image;
    TextView author;
    TextView caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefullscreen);

        image = (ImageView)findViewById(R.id.fullscreenimage);
        author = (TextView)findViewById(R.id.author);
        caption = (TextView)findViewById(R.id.caption);

    }
}
