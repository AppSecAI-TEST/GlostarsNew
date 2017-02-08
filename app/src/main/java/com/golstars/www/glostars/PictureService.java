package com.golstars.www.glostars;

import java.io.IOException;
import java.net.URL;

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
 */

public class PictureService {

    private static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private String data;

    public PictureService(){}

    public void getMutualPictures(String userid, Integer count, String token) throws Exception {

        URL url = new URL("http://www.glostars.com/api/images/mutualpic/" + userid + "/" + count);
        String postMessage = "";
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

                setData((response.body().string()));
                System.out.println(getData());


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
