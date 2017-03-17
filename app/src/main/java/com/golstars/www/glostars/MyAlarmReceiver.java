package com.golstars.www.glostars;

/**
 * Created by edson on 17/03/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.golstars.www.glostars.MyAlarmReceiver";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        String token = intent.getStringExtra("token");
        String usrId = intent.getStringExtra("usrId");

        Intent i = new Intent(context, BackgroundService.class);
        i.putExtra("token", token);
        i.putExtra("usrId", usrId);
        context.startService(i);
    }
}
