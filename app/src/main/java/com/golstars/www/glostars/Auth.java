package com.golstars.www.glostars;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by edson on 30/01/17.
 *
 * authentication class
 */

class Auth  {

    private static final String TAG = "Auth";



    private String password = "";
    private String username = "";
    private String acessToken = "";
    private String tokenType = "";
    private String issued = "";
    private String expires = "";
    private String expires_in = "";

    private Context context;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    private static final String MyPREFERENCES = "glostarsPrefs";



    private static final MediaType txtType = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();







    Auth(Context context, SharedPreferences sharedPreferences, SharedPreferences.Editor ed){
        this.context = context;
        this.sharedPrefs = sharedPreferences;
        this.editor = ed;

        sharedPrefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        this.username = sharedPrefs.getString("username", null);
        this.acessToken = sharedPrefs.getString("token", null);
        this.issued = sharedPrefs.getString("issued", null);
        this.expires = sharedPrefs.getString("expires", null);

    }



    public void login(String grantType, String password, String username) throws Exception{

        URL url = new URL("http://www.glostars.com/Token");
        /*
        String postMessage = "{'grant_type':" + "password," +
                             "'password':" + "91113603," +
                             "'username':" + "netosilvan@hotmail.com" + "}";
        */
        String postMessage = "username=" + username +
                             "&password="+ password +
                             "&grant_type=password";

        RequestBody body =  RequestBody.create(txtType, postMessage);

        System.out.println(body);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                //.addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                /*Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/
                String authData = response.body().string();
                System.out.println(authData);


                try {
                    JSONObject authObject = new JSONObject(authData);
                    setUsername(authObject.getString("userName"));
                    setAcessToken(authObject.getString("access_token"));
                    setExpires(authObject.getString(".expires"));
                    setIssued(authObject.getString(".issued"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    void setUsername(String username) {
        this.username = username;
        System.out.println("usr name "+this.username);
        this.editor.putString(username, "username");
        this.editor.commit();

    }

    void setAcessToken(String acessToken) {
        this.acessToken = acessToken;
        System.out.println("authentication "+this.acessToken);
        this.editor.putString(acessToken, "token");
        this.editor.commit();

    }

    void setIssued(String issued) {
        this.issued = issued;
        System.out.println("issued "+this.issued);
        this.editor.putString(issued, "issued");
        this.editor.commit();
    }

    void setExpires(String expires) {
        this.expires = expires;
        System.out.println("expires "+this.expires);
        this.editor.putString(expires, "expires");
        this.editor.commit();

    }

    public boolean isAuthenticated(){
        boolean auth = false;
        if(this.acessToken != null){
            System.out.println("authentication "+this.acessToken);
            auth = true;
        }

        return  auth;
    }





}
