package com.golstars.www.glostars;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by edson on 07/02/17.
 * class for handling general user data methods
 */

public class SearchUser {

    public SearchUser(){}

    private final OkHttpClient client = new OkHttpClient();

    private String data;


    public void search(String email, String token, String userid) throws Exception{

        String urlBody = "";

        if(!email.isEmpty() || (email != null)){
            urlBody = "http://www.glostars.com/api/account/GetUserInfo?userEmail=" + email;
        } else if(!userid.isEmpty() || (userid != null)){
            urlBody= "http://www.glostars.com/api/account/GetUserInfoById?userId=" + userid;
        }

        URL url = new URL(urlBody);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                setData(response.body().string());
                System.out.println(getData());

                /*
                try{
                    threadMsg(data);

                } catch (Throwable t){
                    t.printStackTrace();
                } */



            }
        });



    }

    public void userEditData(String token) throws Exception{
        URL url = new URL("http://www.glostars.com/api/user/Edit");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                setData(response.body().string());
                System.out.println(getData());

                /*
                try{
                    threadMsg(data);

                } catch (Throwable t){
                    t.printStackTrace();
                } */



            }
        });

    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
