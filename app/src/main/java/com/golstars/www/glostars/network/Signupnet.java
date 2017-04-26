package com.golstars.www.glostars.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.MediaType;

/**
 * Created by edson on 22/04/17.
 */

public class Signupnet {

    private static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private static AsyncHttpClient AsyncClient = new AsyncHttpClient();
    String baseURL = "https://www.glostars.com/";


    public static void signupService(Context context , StringEntity jsonEntity, AsyncHttpResponseHandler responseHandler){
        AsyncClient.addHeader("Content-Type", "application/json");
        AsyncClient.post(context ,"https://www.glostars.com/api/account/register", jsonEntity , "application/json", responseHandler);
    }

}
