package com.golstars.www.glostars;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by edson on 04/03/17.
 */

public class FollowerService {

    private final OkHttpClient client = new OkHttpClient();
    String baseURL = "http://www.glostars.com/";
    private String data;

    public void LoadFollowers(String usrId, String token) throws Exception {
        URL url = new URL(baseURL + "api/account/GetUserFollowById?userId=" + usrId);

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

    private String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

}
