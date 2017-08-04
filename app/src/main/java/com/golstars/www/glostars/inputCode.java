package com.golstars.www.glostars;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;

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


        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        banner.setTypeface(type);
        resend.setTypeface(type);
        codeinput.setTypeface(type);
        submit.setTypeface(type);



    }

    private void send(String userid, String data){
        String url = ServerInfo.BASE_URL_API+"Account/Confirmcode?userid=" + userid + "&confirmCode=" + data;

        AsyncHttpClient client=new AsyncHttpClient();

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {}

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }
        });


    }
}
