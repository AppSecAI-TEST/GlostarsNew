package com.golstars.www.glostars;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by edson on 14/02/17.
 */

public class DownloadImageTask {

    public DownloadImageTask(){}

    private final OkHttpClient client = new OkHttpClient();

    private Bitmap data = null;
    private String uri = null;

    public void getImage(String path) throws Exception{
        URL url  = new URL(path);

        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                setData(BitmapFactory.decodeStream(response.body().byteStream()));
            }
        });
    }

    public Bitmap getData() {
        return data;
    }

    public Bitmap getResizedData(int width){
        // Returns a resized bitmap, scaled according to the given width
        if(data != null){
            int height = (width * data.getHeight())/(data.getWidth());
            data = Bitmap.createScaledBitmap(data, width, height, true);
        }

        return  data;

    }

    public void setData(Bitmap data) {
        this.data = data;
    }






}
