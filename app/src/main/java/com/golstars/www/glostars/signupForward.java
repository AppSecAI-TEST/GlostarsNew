package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class signupForward extends AppCompatActivity {

    TextView signupforwardtext;
    Button signupforwardbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_forward);

        signupforwardtext = (TextView)findViewById(R.id.emailforwardtext);
        signupforwardbutton = (Button)findViewById(R.id.emailforwardbutton);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");

        signupforwardbutton.setTypeface(type);
        signupforwardtext.setTypeface(type);

        signupforwardbutton.setTransformationMethod(null);
    }
}
