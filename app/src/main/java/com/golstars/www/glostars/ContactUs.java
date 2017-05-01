package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity {

    TextView happy , fillform ,iam,coninfo,message;
    EditText emailcon,phonecon,querycon;
    Button cancelcon,submitcon;

    Spinner sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        happy  = (TextView)findViewById(R.id.happy);
        fillform  = (TextView)findViewById(R.id.fillform);
        iam  = (TextView)findViewById(R.id.iam);
        coninfo  = (TextView)findViewById(R.id.coninfo);
        message  = (TextView)findViewById(R.id.message);


        emailcon = (EditText)findViewById(R.id.emailcon);
        phonecon = (EditText)findViewById(R.id.phonecon);
        querycon = (EditText)findViewById(R.id.querycon);


        cancelcon = (Button)findViewById(R.id.cancelcon);
        submitcon = (Button)findViewById(R.id.submitcon);


        sender = (Spinner)findViewById(R.id.sender);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        cancelcon.setTypeface(type);
        submitcon.setTypeface(type);
        emailcon.setTypeface(type);
        phonecon.setTypeface(type);
        querycon.setTypeface(type);
        message.setTypeface(type);
        coninfo.setTypeface(type);
        iam.setTypeface(type);
        fillform.setTypeface(type);
        happy.setTypeface(type);


        cancelcon.setTransformationMethod(null);
        submitcon.setTransformationMethod(null);
    }
}
