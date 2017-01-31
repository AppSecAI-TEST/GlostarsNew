package com.golstars.www.glostars;

import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by edson on 31/01/17.
 *
 * class for registrating users
 *
 */

public class Register {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "Register" ;
    private OkHttpClient client = new OkHttpClient();


    public Register(){}

    public void createAccount(String username, String email, String name, Integer bdayY, Integer bdayM, Integer bdayD, String gender, String lastname, String password) throws IOException {
        URL url = new URL("http://www.glostars.com/api/account/register");
        String postMessage =    "{'UserName':" + username +
                                 ",'Email':" + email +
                                 ",'Name':" + name +
                                 ",'BirthdayYear':" + bdayY +
                                 ",'BirthdayMonth':" + bdayM +
                                 ",'BirthdayDay':" + bdayD +
                                 ",'Gender':" + gender +
                                 ",'LastName':" + lastname +
                                 ",'Password':" + password + "}";

        RequestBody body = RequestBody.create(JSON, postMessage);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            Log.v(TAG, response.body().string());
        }


    }

}
