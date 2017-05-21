package com.golstars.www.glostars;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
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


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private android.os.Handler mHander;
    private android.os.Handler handler;

    private static final MediaType txtType = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();


    private static final String MyPREFERENCES = "glostarsPrefs";
    Auth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.login_activity, container, false);

        final Context context = getActivity();
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        auth = new Auth(context);



        mHander = new android.os.Handler(Looper.getMainLooper());

        handler = new android.os.Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };


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

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.glostars.com/Account/ForgotPassword"));
                startActivity(browserIntent);
            }
        });

        login.setTransformationMethod(null);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentItem (1, true);
            }
        });


        tryFastLogin();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String pwd = password.getText().toString();
                String usrname = email.getText().toString();

                if(pwd.isEmpty() || usrname.isEmpty()){
                    Toast.makeText(getContext(), "Login or password missing", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        newlogin(pwd,usrname);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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



    public void newlogin(String password, String username){
        final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Logging In. Please wait...", true);
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }catch (Exception e){}
        try {
            StringEntity entity = new StringEntity("username=" + username + "&password=" + password + "&grant_type=password");
            client.post(getContext(), "https://www.glostars.com/Token", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject authObject) {
                    try {
                        System.out.println("1 result "+authObject);
                        auth.setUsername(authObject.getString("userName"));
                        auth.setAcessToken(authObject.getString("access_token"));
                        auth.setExpires(authObject.getString(".expires"));
                        auth.setIssued(authObject.getString(".issued"));

                        startActivity(new Intent(getActivity(), MainFeed.class));
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    try {
                        System.out.println(errorResponse.toString());
                        if (errorResponse.get("error").toString().equals("invalid_grant")) {
                            Toast.makeText(getActivity(), "Login failed.Wrong user mail or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }


            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    //You can work with them now using the objects like :
    // email.getText().... blah blah blah



}
