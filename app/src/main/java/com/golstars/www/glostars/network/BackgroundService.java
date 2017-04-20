package com.golstars.www.glostars.network;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;

import com.golstars.www.glostars.MainFeed;
import com.golstars.www.glostars.R;
import com.golstars.www.glostars.notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by edson on 17/03/17.
 */

public class BackgroundService extends IntentService{
    public static final String ACTION = "com.golstars.www.glostars.network.BackgroundService";
    private static final int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification mNotification;

    private final OkHttpClient client = new OkHttpClient();


    public BackgroundService() {
        super("notification-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Intent i = new Intent(ACTION);
        i.putExtra("resultCode", Activity.RESULT_OK);
        i.putExtra("resultValue", "new notifications");

        String token = intent.getStringExtra("token");
        String usrId = intent.getStringExtra("usrId");

        System.out.println("token " + token);
        try {
            getNotifs(token, usrId);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendNotification(Integer nots){

        String msg;
        if(nots > 1){
            msg = nots + " new notifications received";
        } else {
            msg = nots + " new notification received";
        }

        mNotification = new Notification();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.triangle)
                        .setContentTitle("Glostars")
                        .setAutoCancel(true)
                        .setContentText(msg);

        Intent in = new Intent(this, notification.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainFeed.class);
        stackBuilder.addNextIntent(in);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(MY_NOTIFICATION_ID, mBuilder.build());
    }

    private void getNotifs(String token, String usrId) throws Exception{

        URL url = new URL("https://www.glostars.com/api/notifications/user/" + usrId);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();


        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String res = response.body().string();
        try {
            JSONObject dat = new JSONObject(res);
            JSONObject data = dat.getJSONObject("resultPayload");
            JSONArray activityNotifications = data.getJSONArray("activityNotifications");
            JSONArray followerNotifications = data.getJSONArray("followerNotifications");

            Integer newNotifs = 0;

            for(int i = 0; i < activityNotifications.length(); i++){
                if (activityNotifications.getJSONObject(i).getString("seen").equals("false")) {
                    newNotifs++;
                }
            }

            for(int i = 0; i < followerNotifications.length(); i++){
                if (followerNotifications.getJSONObject(i).getString("seen").equals("false")) {
                    newNotifs++;
                }
            }
            if(newNotifs > 0){
                sendNotification(newNotifs);
            } else{
                System.out.println("zero new notifications");
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
