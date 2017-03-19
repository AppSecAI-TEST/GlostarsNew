package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FAQs extends AppCompatActivity {

    TextView what , whattext,who,whotext,winner,winnertext,copy,copytext,prize,prizetext,
            see,seetext , collect,collecttext,security,securitytext,camera,cameratext,theme,themetext,cost,costtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        what= (TextView)findViewById(R.id.what);
        whattext= (TextView)findViewById(R.id.whattext);

        who= (TextView)findViewById(R.id.who);
        whotext= (TextView)findViewById(R.id.whotext);


        see= (TextView)findViewById(R.id.see);
        seetext= (TextView)findViewById(R.id.seeText);


        camera= (TextView)findViewById(R.id.cameracomp);
        cameratext= (TextView)findViewById(R.id.cameracomptext);


        security= (TextView)findViewById(R.id.security);
        securitytext= (TextView)findViewById(R.id.securitytext);


        winner= (TextView)findViewById(R.id.winners);
        winnertext= (TextView)findViewById(R.id.winnersText);


        copy= (TextView)findViewById(R.id.copyrite);
        copytext= (TextView)findViewById(R.id.copyriteText);

        prize= (TextView)findViewById(R.id.prizes);
        prizetext= (TextView)findViewById(R.id.prizesText);

        theme= (TextView)findViewById(R.id.themepic);
        themetext= (TextView)findViewById(R.id.themepictext);

        collect= (TextView)findViewById(R.id.collect);
        collecttext= (TextView)findViewById(R.id.collecttext);

        cost = (TextView)findViewById(R.id.cost);
        costtext = (TextView)findViewById(R.id.costtext);




        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        prize.setTypeface(type);
        prizetext.setTypeface(type);
        what.setTypeface(type);
        whattext.setTypeface(type);
        collecttext.setTypeface(type);
        collect.setTypeface(type);
        copy.setTypeface(type);
        copytext.setTypeface(type);
        winner.setTypeface(type);
        winnertext.setTypeface(type);
        security.setTypeface(type);
        securitytext.setTypeface(type);
        cameratext.setTypeface(type);
        camera.setTypeface(type);
        who.setTypeface(type);
        whotext.setTypeface(type);
        see.setTypeface(type);
        seetext.setTypeface(type);
        theme.setTypeface(type);
        themetext.setTypeface(type);
        cost.setTypeface(type);
        costtext.setTypeface(type);
    }
}
