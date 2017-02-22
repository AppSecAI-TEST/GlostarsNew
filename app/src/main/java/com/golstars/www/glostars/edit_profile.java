package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class edit_profile extends AppCompatActivity {

    EditText firstname;
    EditText lastname;
    EditText aboutme;
    EditText interests;
    EditText currentcity;
    EditText homecity;
    EditText oldpass;
    EditText newpass;
    EditText confirmpass;

    Button save;
    Button cancel;

    TextView changepass;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;


    Spinner currentcountry;
    Spinner homecountry;
    Spinner occupation;
    ArrayAdapter<CharSequence> occupationadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstname = (EditText)findViewById(R.id.firstnameedit);
        lastname = (EditText)findViewById(R.id.lastnameedit);
        aboutme = (EditText)findViewById(R.id.aboutmeedit);
        interests = (EditText)findViewById(R.id.interestedit);
        oldpass = (EditText)findViewById(R.id.oldpass);
        newpass = (EditText)findViewById(R.id.newpass);
        confirmpass = (EditText)findViewById(R.id.confirmnewpass);
        currentcity = (EditText)findViewById(R.id.currentcity);
        homecity = (EditText)findViewById(R.id.homecity);

        save = (Button)findViewById(R.id.savebutton);
        cancel = (Button)findViewById(R.id.cancelbutton);
        changepass = (TextView)findViewById(R.id.changepassban);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);


        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        firstname.setTypeface(type);
        lastname.setTypeface(type);
        aboutme.setTypeface(type);
        interests.setTypeface(type);
        oldpass.setTypeface(type);
        newpass.setTypeface(type);
        confirmpass.setTypeface(type);
        currentcity.setTypeface(type);
        homecity.setTypeface(type);

        currentcountry = (Spinner) findViewById(R.id.currentcountryspinner);
        homecountry = (Spinner) findViewById(R.id.homecountryspinner);
        occupation = (Spinner)findViewById(R.id.occupationspinner);

        occupationadapter = ArrayAdapter.createFromResource(this,R.array.occupation,android.R.layout.simple_spinner_item);
        occupationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        occupation.setAdapter(occupationadapter);


        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(showingFirst == true){
                    slogo.setBackground(getResources().getDrawable(R.drawable.search_active));
                    gl.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                }else{
                    slogo.setBackground(getResources().getDrawable(R.drawable.search));
                    gl.setVisibility(View.VISIBLE);
                    search.setVisibility(View.INVISIBLE);
                }

            }
        });


        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, countries);
        homecountry.setAdapter(adapter);
        currentcountry.setAdapter(adapter);

    }
}
