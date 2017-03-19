package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class dataPrivacy extends AppCompatActivity {

    TextView dataprivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_privacy);

        dataprivacy = (TextView)findViewById(R.id.datapri);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        dataprivacy.setTypeface(type);



    }
}
