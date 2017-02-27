package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;

public class searchResults extends AppCompatActivity implements SearchView.OnQueryTextListener{

    GridView searchgrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.lotext);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        searchgrid = (GridView)findViewById(R.id.searchGrid);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main_feed,menu);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText){
        return false;

    }

}
