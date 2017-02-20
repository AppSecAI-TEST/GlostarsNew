package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class edit_profile extends AppCompatActivity {

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

        currentcountry = (Spinner) findViewById(R.id.currentcountryspinner);
        homecountry = (Spinner) findViewById(R.id.homecountryspinner);
        occupation = (Spinner)findViewById(R.id.occupationspinner);

        occupationadapter = ArrayAdapter.createFromResource(this,R.array.occupation,android.R.layout.simple_spinner_item);
        occupationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        occupation.setAdapter(occupationadapter);


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
