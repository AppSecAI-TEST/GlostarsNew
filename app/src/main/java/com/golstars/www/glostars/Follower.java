package com.golstars.www.glostars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

public class Follower  {

    private String profilePicture;
    private String userId;
    private String userName;
    private String status;

    public String getStatus() {
        return status;
    }


    public Follower(String profilePicture, String userId, String userName) {
        this.profilePicture = profilePicture;
        this.userId = userId;
        this.userName = userName;
    }

    public Follower() {
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStatus(JSONArray followerList, JSONArray followingList){
        boolean isFollower = false;
        boolean isFollowing = false;

        for(int i = 0; i < followerList.length(); i++){
            try {
                if(this.userId.equals(followerList.getJSONObject(i).getString("id"))){
                    isFollower = true;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < followingList.length(); i++){
            try {
                if(this.userId.equals(followingList.getJSONObject(i).getString("id"))){
                    isFollowing = true;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(isFollower && isFollowing){
            this.status = "mutual";
        } else if(isFollower && !isFollowing){
            this.status = "follower";
        } else if(!isFollower && isFollowing){
            this.status = "following";
        } else if(!isFollower && !isFollowing){
            this.status = "follow";
        }



    }

    public void setRigStatus(String status){
        this.status = status;
    }
}
