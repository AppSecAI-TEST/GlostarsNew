package com.golstars.www.glostars;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.golstars.www.glostars.network.SearchUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;

/**
 * Created by edson on 08/02/17.
 * singleton class representing current user
 */

public class MyUser implements Parcelable {

    private static MyUser mUser = new MyUser();

    private String name;
    private String email;
    private String profilePicURL;
    private String userId;
    private String sex;
    private String token;

    private static final String MyPREFERENCES = "glostarspreferences";


    private MyUser(){}

    protected MyUser(Parcel in) {
        name = in.readString();
        email = in.readString();
        profilePicURL = in.readString();
        userId = in.readString();
        sex = in.readString();
        token = in.readString();
    }

    public static final Creator<MyUser> CREATOR = new Creator<MyUser>() {
        @Override
        public MyUser createFromParcel(Parcel in) {
            return new MyUser(in);
        }

        @Override
        public MyUser[] newArray(int size) {
            return new MyUser[size];
        }
    };

    public static MyUser getmUser(){
        return mUser;
    }

    public static MyUser getmUser(Context context){


        return getFromCache(context);



        /*
        mUser.setName(sharedPreferences.getString("EMAIL", null));
        mUser.setEmail(sharedPreferences.getString("NAME", null));
        mUser.setProfilePicURL(sharedPreferences.getString("USER_ID", null));
        mUser.setUserId(sharedPreferences.getString("PROFILE_PIC", null));
        mUser.setToken(sharedPreferences.getString("TOKEN", null));
        mUser.setSex(sharedPreferences.getString("SEX", null));
        */

        /*
        String data;
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        data = sharedPreferences.getString("user", null);


        if(data != null){

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

                System.out.println("USER GOT FROM FILE");



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  mUser;

        } else {
            return  null;
        } */


        //return mUser;

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


    public MyUser setContext(Context context) {

        String filename = "glostarsus";
        FileOutputStream outputStream;

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



            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            System.out.println("user saved");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
        System.out.println("could not save files");
    }







        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user",data);
        editor.putString("NAME", mUser.getName());
        editor.putString("USER_ID", mUser.getUserId());
        editor.putString("PROFILE_PIC", mUser.getProfilePicURL());
        editor.putString("TOKEN", mUser.getToken());
        editor.putString("SEX", mUser.getSex());

        return mUser;


    }


    private static MyUser getFromCache(Context context){

        final Auth auth = new Auth(context);
        mUser.setToken(auth.getAcessToken());

        String filename = "glostarsus";
        InputStream inputStream;
        String data = "";

        try{
            inputStream = context.openFileInput(filename);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receive = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receive = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receive);
                }

                inputStream.close();
                data = stringBuilder.toString();
                System.out.println("files retrieved");

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
                        " sex: " + mUser.getSex() +
                        " token: "  + mUser.getToken());


                System.out.println("USER GOT FROM FILE");
                return mUser;

            } else{
                return  null;
            }

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("cannot read file");
        } catch (JSONException e) {
            e.printStackTrace();
        }





        return null;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(profilePicURL);
        dest.writeString(userId);
        dest.writeString(sex);
        dest.writeString(token);
    }
}
