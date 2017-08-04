package com.golstars.www.glostars.Database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Arif on 8/4/2017.
 */

public class OfflineInfo {
    SharedPreferences sharedpreferences;
    Context context;
    public OfflineInfo(Context context){
        this.context=context;
        sharedpreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }
    public void saveToken(String token){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    public String getToken(){
        return sharedpreferences.getString("token","");
    }

    public void saveUserId(String userId){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("userid",userId);
        editor.commit();
    }

    public String getUserId(){
        return sharedpreferences.getString("userid","");
    }
}
