package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

/**
 * Created by Shanto on 3/23/2017.
 */

public class LoadingSpinner extends AppCompatActivity{

    ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingspinner);

        loadingSpinner = (ProgressBar)findViewById(R.id.loadingSpinner);

    }
}
