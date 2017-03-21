package com.golstars.www.glostars;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by edson on 22/02/17.
 * server request class for notifications
 */

public class NotificationService {

    /*
    * private static final  String baseURL = "http://www.glostars.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void LoadFollowers(Context context,String usrId, String token, AsyncHttpResponseHandler responseHandler)  {
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "Bearer " + token);
        client.get(baseURL+"api/account/GetUserFollowById?userId=" + usrId,  responseHandler);
    }
    *
    * */

    private static final  String baseURL = "http://www.glostars.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getNotifications(Context context, String usrId, String token, AsyncHttpResponseHandler responseHandler)  {
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "Bearer " + token);
        client.get(baseURL+"api/notifications/user/" + usrId ,  responseHandler);
    }






    /*
    public void getNotifications(String userid , String token) throws Exception{
        URL url = new URL("http://www.glostars.com/api/notifications/user/" + userid );

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String data = response.body().string();
                try {
                    JSONObject dat = new JSONObject(data);
                    JSONObject resultPayload = dat.getJSONObject("resultPayload");
                    //JSONObject model = resultPayload.getJSONObject("model");
                    setDataObject(resultPayload);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setData((response.body().string()));
                System.out.println(data);


            }
        });
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public JSONObject getDataObject() {
        return dat;
    }

    public void setDataObject(JSONObject dat) {
        this.dat = dat;
    }
    */
}
