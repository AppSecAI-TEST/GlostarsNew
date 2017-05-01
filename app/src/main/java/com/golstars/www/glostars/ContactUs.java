package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


        submitcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usertype = sender.getSelectedItem().toString();
                String email = emailcon.getText().toString();
                String phone = phonecon.getText().toString();
                String query = querycon.getText().toString();
                String extra = null;

                if(email.isEmpty() && phone.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please add your email or phone number", Toast.LENGTH_LONG).show();
                }
                else if(query.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please add the content of your message", Toast.LENGTH_LONG).show();

                } else{

                    if(!email.isEmpty()){
                        extra = email;
                    } else if(!phone.isEmpty()){
                        extra = phone;
                    }

                    String message = usertype + " message: " + query + '\n' + '\n' + "Contact info: " + extra ;
                    String emailtosend = "care.glostars@gmail.com";
                    submit(emailtosend, message);
                }


            }
        });

        cancelcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailcon.setText("");
                phonecon.setText("");
                querycon.setText("");
                finish();

            }
        });


    }


    private void submit(String email, String msg){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Message to Glostars");
        i.putExtra(Intent.EXTRA_TEXT, msg);
        try{
            startActivity(Intent.createChooser(i, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


    }
}
