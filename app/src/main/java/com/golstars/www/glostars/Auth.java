package com.golstars.www.glostars;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
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








    Auth(Context context){
        this.context = context;
        //this.sharedPrefs = sharedPreferences;
        //this.editor = ed;

        sharedPrefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        this.username = sharedPrefs.getString("username", null);
        this.acessToken = sharedPrefs.getString("token", null);
        this.issued = sharedPrefs.getString("issued", null);
        this.expires = sharedPrefs.getString("expires", null);

    }


    void setUsername(String username) {
        this.username = username;
        System.out.println("usr name "+ this.getUsername());
        this.editor.putString("username", this.username);
        this.editor.commit();

    }

    void setAcessToken(String acessToken) {
        this.acessToken = acessToken;
        System.out.println("authentication "+ this.getAcessToken());
        this.editor.putString("token", this.acessToken);
        this.editor.commit();

    }

    void setIssued(String issued) {
        this.issued = issued;
        System.out.println("issued "+this.issued);
        editor.putString("issued", this.issued);
        editor.commit();
    }

    void setExpires(String expires) {
        this.expires = expires;
        System.out.println("expires "+this.expires);
        this.editor.putString("expires", this.expires);
        this.editor.commit();

    }

    public boolean isTokenValid(){

        if(this.expires != null){
            String expiresDate = this.expires;
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            boolean isValid = false;

            try {
                if(expiresDate != ""){
                    Timestamp datetime = new Timestamp(format.parse(expiresDate).getTime());
                    isValid = datetime.after(new java.util.Date());
                    System.out.println("Token Valid: " + isValid);

                }



            } catch (ParseException e) {
                e.printStackTrace();
            }

            return isValid;
        }
        return false;

        //boolean auth = false;
        //if(this.getAcessToken() != null){
         //   System.out.println("authentication "+ this.getAcessToken());
        //    auth = true;
        //}

        //return  auth;
    }


    public String getUsername() {
        return username;
    }

    public String getAcessToken() {
        return acessToken;
    }

    public void clearData(){
            editor.clear();
            editor.commit();
    }
}
