package com.golstars.www.glostars.models;

import android.graphics.Bitmap;

import org.json.JSONArray;

/**
 * Created by edson on 07/02/17.
 * Post object class
 */

public class Post {
    private String author;
    private String userId;
    private String photoId;
    private String description;
    private String picURL;
    private String profilePicURL;
    private String uploaded;
    private String privacy;
    private boolean isFeatured;
    private boolean isCompeting;
    private int starsCount;
    private int commentCount;
    private JSONArray ratings;
    private JSONArray comments;


    public Post(){
    }

    public Post(String author, String usr, String photoID, String description, String picURL, String profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount, Integer commentCount){
        this.author = author;
        this.userId = usr;
        this.photoId= photoID;
        this.description = description;
        this.picURL = picURL;
        this.profilePicURL = profilePicURL;
        this.isFeatured = isFeatured;
        this.isCompeting = isCompeting;
        this.starsCount = starsCount;
        this.commentCount = commentCount;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public boolean isCompeting() {
        return isCompeting;
    }

    public void setCompeting(boolean competing) {
        isCompeting = competing;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getPicURL() {
        return picURL;
    }

    public JSONArray getRatings() {
        return ratings;
    }

    public void setRatings(JSONArray ratings) {
        this.ratings = ratings;
    }

    public JSONArray getComments() {
        return comments;
    }

    public void setComments(JSONArray comments) {
        this.comments = comments;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }


    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
