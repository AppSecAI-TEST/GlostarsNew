package com.golstars.www.glostars.network;

import android.content.Context;

import com.golstars.www.glostars.R;
import com.golstars.www.glostars.ServerInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedReader;
import java.net.URL;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by edson on 08/02/17.
 * this class has several methods for requesting pictures from server side
 * and its method GetData returns a JSONarray with several pictures
 */

public class PictureService {

    private static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType txtType = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private JSONArray data;
    private JSONObject dat;
    String baseURL = "https://www.glostars.com/";


    private static AsyncHttpClient AsyncClient = new AsyncHttpClient();

    public PictureService(){}

    public void getMutualPictures(String userid, Integer count, String token) throws Exception {

        URL url = new URL("https://www.glostars.com/api/images/mutualpic/" + userid + "/" + count);
        String postMessage = "{ListPhoto:[]}";
        RequestBody body = RequestBody.create(JSONType, postMessage);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .post(body)
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
                    JSONArray pics = resultPayload.getJSONArray("data");
                    setData(pics);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setData((response.body().string()));
                System.out.println(data);


            }
        });
    }

    public void getUserPictures(String userid, Integer count, String token) throws Exception{
        URL url = new URL("https://www.glostars.com/api/images/user/" + userid + "/" + count);

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

    public void getCompetitionPictures(Integer count, String token) throws Exception{
        URL url = new URL(baseURL + "api/images/competition/" + count);

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
                    JSONArray resultPayload = dat.getJSONArray("resultPayload");
                    //JSONObject model = resultPayload.getJSONObject("model");
                    setData(resultPayload);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setData((response.body().string()));
                System.out.println(data);


            }
        });
    }

    public void uploadPicture(String description, Boolean isCompeting, String privacy, String uri, String token) throws Exception{
        URL url = new URL(baseURL+"api/images/upload");

        JSONObject msg = new JSONObject();
        msg.put("Description", description);
        msg.put("IsCompeting", isCompeting.toString());
        msg.put("Privacy", privacy);
        msg.put("ImageDataUri", uri);
        System.out.println(uri);
        System.out.println(token);

        RequestBody body =  RequestBody.create(JSONType, msg.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
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
                System.out.println(data);


            }
        });


    }


    public static void ratePicture(Context context, String token, StringEntity jsonEntity, AsyncHttpResponseHandler responseHandler){
        AsyncClient.addHeader("Content-Type", "application/json");
        AsyncClient.addHeader("Authorization", "Bearer " + token);
        AsyncClient.post(context ,"https://www.glostars.com/api/images/rating", jsonEntity , "application/json", responseHandler);
    }

    public static void unratePicture(Context context, String token, String picId, StringEntity jsonEntity, AsyncHttpResponseHandler responseHandler){
        AsyncClient.addHeader("Content-Type", "application/json");
        AsyncClient.addHeader("Authorization", "Bearer " + token);
        AsyncClient.post(context ,"https://www.glostars.com/api/images/removerate/" + picId, jsonEntity , "application/json", responseHandler);
    }


    public void getPublicPictures(Integer count, String token) throws Exception{
        URL url = new URL(baseURL + "api/images/public/" + count);

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

    public void commentPicture(String picID, String message, String token) throws Exception{
        URL url = new URL(baseURL + "api/images/comment");

        JSONObject msg = new JSONObject();
        msg.put("CommentText", message);
        msg.put("PhotoId", picID);

        RequestBody body = RequestBody.create(JSONType, msg.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e)
            {
                JSONObject res = new JSONObject();
                try {
                    res.put("responseCode", 0);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                setDataObject(res);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String data = response.body().string();
                System.out.println(data);

                JSONObject res = new JSONObject();
                try {
                    res.put("responseCode", 1);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                setDataObject(res);

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
}
