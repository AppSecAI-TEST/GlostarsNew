package com.golstars.www.glostars.network;

import android.content.Context;

import com.golstars.www.glostars.models.GuestUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private final OkHttpClient client = new OkHttpClient();

    private String data;
    private JSONArray dataArray;
    private JSONObject dataObj;


    private static final  String baseURL = "http://www.glostars.com/";
    private static AsyncHttpClient AsyncClient = new AsyncHttpClient();

    public static void searchUsrInfo(Context context, String usrId, String token, AsyncHttpResponseHandler responseHandler)  {
        AsyncClient.addHeader("Content-Type", "application/json");
        AsyncClient.addHeader("Authorization", "Bearer " + token);
        AsyncClient.get(baseURL+ "api/user/GetUserInfo?id=" + usrId ,  responseHandler);
    }


    public void search(String email, String token, String userid) throws Exception{

        String urlBody = "";

        if((email != "")){
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

                String data = response.body().string() ;
                setData(data);
                System.out.println("DATA IS: " + data);

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

    public void findUserByName(String name, String token) throws Exception{
        URL url = new URL(baseURL + "api/account/GetUserListByName?Name=" + name);

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

                String data = response.body().string();
                try {
                    JSONObject dat = new JSONObject(data);
                    JSONArray resultPayload = dat.getJSONArray("resultPayload");
                    //JSONObject model = resultPayload.getJSONObject("model");
                    setDataArray(resultPayload);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setData((response.body().string()));
                System.out.println(data);



            }
        });
    }

    public void searchHashtags(String hashtag, String token) throws Exception{
        URL url = new URL(baseURL + "api/images/GetSimilarHashTag?searchTag=" + hashtag);
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

    public void findUserInfo(String userId, String token) throws Exception{
        URL url = new URL(baseURL + "api/user/GetUserInfo?id=" + userId);

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

                String data = response.body().string();
                try {
                    JSONObject dat = new JSONObject(data);
                    setDataObj(dat.getJSONObject("resultPayload"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setData((response.body().string()));
                System.out.println(data);

                //setData((response.body().string()));
                System.out.println(data);



            }
        });
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONArray getDataArray() {
        return dataArray;
    }

    public void setDataArray(JSONArray data) {
        this.dataArray = data;
    }

    public JSONObject getDataObj() {
        return dataObj;
    }

    public void setDataObj(JSONObject dataObj) {
        this.dataObj = dataObj;
    }


    public GuestUser getGuestUser(String usrId, String token) throws Exception {
        GuestUser gUser = new GuestUser();

        String data = null;
        search("", token, usrId);
        while(data == null){
            data = getData();
        }

        JSONObject dat = new JSONObject(data);
        JSONObject usrData = dat.getJSONObject("resultPayload");
        gUser.setEmail(usrData.getString("email"));
        gUser.setName(usrData.getString("name"));
        gUser.setUserId(usrData.getString("userId"));
        gUser.setProfilePicURL(usrData.getString("profilePicURL"));

        return gUser;

    }


}
