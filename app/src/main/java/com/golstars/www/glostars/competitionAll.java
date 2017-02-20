package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class competitionAll extends AppCompatActivity {


    Button gallerybut;
    Button tipsbut;
    Button termsbut;
    Button recogbut;



    GridView gallery;
    LinearLayout tips;
    ScrollView terms;
    ScrollView recog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gallery = (GridView)findViewById(R.id.gallerygrid);
        tips = (LinearLayout)findViewById(R.id.tipslin);
        terms = (ScrollView)findViewById(R.id.termslin);
        recog = (ScrollView)findViewById(R.id.recoglin);

        gallerybut = (Button)findViewById(R.id.gallerybut);
        tipsbut = (Button)findViewById(R.id.tipsbut);
        termsbut = (Button)findViewById(R.id.termsbut);
        recogbut = (Button)findViewById(R.id.recogbut);


        //==========================================================================

        gallerybut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallerybut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                gallery.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);

            }
        });


        termsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termsbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                terms.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);
            }
        });



        tipsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipsbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                tips.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);
            }
        });


        recogbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                recogbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));


                recog.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
            }
        });

    }





}
