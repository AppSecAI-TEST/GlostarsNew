package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class noInternet extends AppCompatActivity {

    Button retrybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        retrybutton = (Button)findViewById(R.id.retrybutton);


        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        retrybutton.setTypeface(type);
        retrybutton.setTransformationMethod(null);


    }

}
