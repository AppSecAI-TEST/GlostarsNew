package com.golstars.www.glostars;

import android.content.Context;
import android.content.SharedPreferences;

import com.golstars.www.glostars.network.SearchUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;

/**
 * Created by edson on 08/02/17.
 * singleton class representing current user
 */

public class MyUser {

    private static MyUser mUser = new MyUser();

    private String name;
    private String email;
    private String profilePicURL;
    private String userId;
    private String sex;
    private String token;

    private static final String MyPREFERENCES = "com.golstars.www.glostars.PREFERENCE_FILE_KEY";


    private MyUser(){}

    public static MyUser getmUser(){
        return mUser;
    }

    public static MyUser getmUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        mUser.setName(sharedPreferences.getString("NAME", null));
        mUser.setEmail(sharedPreferences.getString("EMAIL", null));
        mUser.setProfilePicURL(sharedPreferences.getString("PROFILEPIC", null));
        mUser.setUserId(sharedPreferences.getString("USER_ID", null));
        mUser.setToken(sharedPreferences.getString("TOKEN", null));
        return mUser;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setContext(Context context) {
        final Auth auth = new Auth(context);
        String data = null;
        SearchUser searchUser = new SearchUser();

        mUser.setToken(auth.getAcessToken());
        try { //this method searches data for the current user using the app
            searchUser.search(auth.getUsername(), auth.getAcessToken(), null);
            while (data == null){
                data = searchUser.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject dat = new JSONObject(data);
            JSONObject usrData = dat.getJSONObject("resultPayload");
            mUser.setEmail(usrData.getString("email"));
            mUser.setName(usrData.getString("name"));
            mUser.setUserId(usrData.getString("userId"));
            mUser.setProfilePicURL(usrData.getString("profilePicURL"));
            mUser.setSex(usrData.getString("gender"));
            System.out.println("user data is - name: " + mUser.getName() +
                                            " email: " + mUser.getEmail()+
                                            " profilePic: " + mUser.getProfilePicURL() +
                                            " userId: " + mUser.getUserId() +
                                            " sex: " + mUser.getSex());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", mUser.getEmail());
        editor.putString("NAME", mUser.getName());
        editor.putString("USER_ID", mUser.getUserId());
        editor.putString("PROFILE_PIC", mUser.getProfilePicURL());
        editor.putString("TOKEN", mUser.getToken());
        editor.putString("SEX", mUser.getSex());




    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
