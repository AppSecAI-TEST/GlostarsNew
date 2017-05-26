package com.golstars.www.glostars;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class inputCode extends AppCompatActivity {

    TextView banner, resend;
    Button submit;
    EditText codeinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);


        banner = (TextView)findViewById(R.id.banner);
        resend = (TextView)findViewById(R.id.resend);
        submit = (Button)findViewById(R.id.submit);
        codeinput =(EditText)findViewById(R.id.numberinput);



        submit.setTransformationMethod(null);
        resend.setPaintFlags(resend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
