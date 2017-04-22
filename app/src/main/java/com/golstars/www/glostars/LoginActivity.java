package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Looper;
import android.support.v4.app.Fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 1/31/2017.
 * this class contains the login methods and uses Auth class
 */

public class LoginActivity extends Fragment {



    private EditText email;
    private EditText password;
    private Button login;
    private TextView signup;
    private TextView forgotpass;
    private TextView upwrong;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private android.os.Handler mHander;

    private static final MediaType txtType = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();


    private static final String MyPREFERENCES = "glostarsPrefs";
    Auth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_activity, container, false);

        Context context = getActivity();
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        auth = new Auth(context);



        mHander = new android.os.Handler(Looper.getMainLooper());




        email = (EditText) rootView.findViewById(R.id.emailEditText);
        password = (EditText) rootView.findViewById(R.id.passwordEditText);
        login = (Button) rootView.findViewById(R.id.logInButton);
        signup = (TextView) rootView.findViewById(R.id.signUp);
        forgotpass = (TextView) rootView.findViewById(R.id.forgotPass);
        //upwrong = (TextView) rootView.findViewById(R.id.upwrong);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Light.ttf");
        email.setTypeface(type);
        password.setTypeface(type);
        email.setTypeface(type);
        signup.setTypeface(type);
        login.setTypeface(type);
        forgotpass.setTypeface(type);


        login.setTransformationMethod(null);


        tryFastLogin();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String pwd = password.getText().toString();
                String usrname = email.getText().toString();
                try {
                    login("password", pwd, usrname);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    public void tryFastLogin(){
        if(auth.isTokenValid()){
            startActivity(new Intent(getActivity(), MainFeed.class));
            getActivity().finish();
        }
    }

    public void login(String grantType, String password, String username) throws Exception{

        URL url = new URL("https://www.glostars.com/Token");
        /*
        String postMessage = "{'grant_type':" + "password," +
                             "'password':" + "91113603," +
                             "'username':" + "netosilvan@hotmail.com" + "}";
        */
        String postMessage = "username=" + username +
                "&password="+ password +
                "&grant_type=" + grantType;

        RequestBody body =  RequestBody.create(txtType, postMessage);

        System.out.println(body);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //TODO: CREATE A DIALOG FOR FAILED LOGIN


                /*Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/
                String authData = response.body().string();
                System.out.println(authData);


                try {
                    JSONObject authObject = new JSONObject(authData);
                    auth.setUsername(authObject.getString("userName"));
                    auth.setAcessToken(authObject.getString("access_token"));
                    auth.setExpires(authObject.getString(".expires"));
                    auth.setIssued(authObject.getString(".issued"));
                    //auth.isTokenValid();

//                    login.setBackgroundResource(R.drawable.roundedbutton1);
//                    login.setTextColor(getResources().getColor(R.color.colorPrimary));

                    startActivity(new Intent(getActivity(), MainFeed.class));
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }



    //You can work with them now using the objects like :
    // email.getText().... blah blah blah



}
