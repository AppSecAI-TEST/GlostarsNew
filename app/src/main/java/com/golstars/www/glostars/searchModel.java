package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shanto on 4/4/2017.
 */

public class searchModel extends AppCompatActivity {

    ImageView propicsearchmodel;
    TextView usernamesearchmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmodel);

        propicsearchmodel= (ImageView)findViewById(R.id.propicSearchModel);
        usernamesearchmodel = (TextView)findViewById(R.id.usernameSearchModel);
    }
}