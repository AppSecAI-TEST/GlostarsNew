package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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


        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    //startActivity(new Intent(getApplicationContext(), MainFeed.class));
                    finish();
                } else{
                    Intent intent = new Intent();
                    Toast.makeText(getApplicationContext(), "You are disconnected", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean isConnected() {
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;
    }

}
