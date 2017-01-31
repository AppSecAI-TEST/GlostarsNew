package com.golstars.www.glostars;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by edson on 30/01/17.
 *
 * authentication class
 */

public class Auth  {

    private static final String TAG = "Auth";


    private String grantType = "password";
    private String password = "";
    private String username = "";
    private String acessToken = "";
    private String tokenType = "";
    private String issued = "";
    private String expires = "";
    private String expires_in = "";

    private Context context;
    private SharedPreferences sharedPrefs;

    private static final String MyPREFERENCES = "glostarsPrefs";



    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();



    public Auth(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPrefs = sharedPreferences;

        sharedPrefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        this.username = sharedPrefs.getString("username", "");
        this.acessToken = sharedPrefs.getString("token", "");
        this.issued = sharedPrefs.getString("issued", "");
        this.expires = sharedPrefs.getString("expires", "");

    }


    public void login(String grantType, String password, String username) throws IOException{

        URL url = new URL("http://www.glostars.com/Token");
        String postMessage = "{'grant_type':" + grantType +
                             ",'password':" + password +
                             ",'username':" + username + "}";

        RequestBody body = RequestBody.create(JSON, postMessage);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            Log.v(TAG, response.body().string());
        }
    }



    public String getUsername() { return username; }

    public String getAcessToken() {
        return acessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpires() {
        return expires;
    }




}
