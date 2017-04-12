package com.golstars.www.glostars.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by edson on 12/03/17.
 */

public class UploadService {

    private static final String baseURL = "https://www.glostars.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void uploadPhoto(Context context, String url, StringEntity jsonEntity, AsyncHttpResponseHandler responseHandler, String token){
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "Bearer " + token);
        client.post(context ,getAbsoluteUrl(url), jsonEntity , "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl){
        return baseURL + relativeUrl;
    }
}
