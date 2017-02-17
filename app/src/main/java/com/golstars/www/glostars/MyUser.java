package com.golstars.www.glostars;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

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

    private String token;

    //private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private static final String MyPREFERENCES = "glostarsPrefs";


    private MyUser(){}

    public static MyUser getmUser(){
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
            System.out.println("user data is - name: " + mUser.getName() +
                                            " email: " + mUser.getEmail()+
                                            " profilePic: " + mUser.getProfilePicURL() +
                                            " userId: " + mUser.getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public  void getUserData(){
        SearchUser searchUser = new SearchUser();

        try {
            searchUser.userEditData(getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
