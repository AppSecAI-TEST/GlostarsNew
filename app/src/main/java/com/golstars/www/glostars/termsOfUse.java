package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class termsOfUse extends AppCompatActivity {

    TextView termsban,termsall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        termsall = (TextView)findViewById(R.id.termsall);
        termsban = (TextView)findViewById(R.id.termsofUsebanner);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        termsban.setTypeface(type);
        termsall.setTypeface(type);

    }
}

