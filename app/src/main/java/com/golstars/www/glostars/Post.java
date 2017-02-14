package com.golstars.www.glostars;

import android.graphics.Bitmap;

/**
 * Created by edson on 07/02/17.
 * Post object class
 */

public class Post {
    private String author;
    private String userId;
    private String photoId;
    private String description;
    private Bitmap picURL;
    private Bitmap profilePicURL;
    private boolean isFeatured;
    private boolean isCompeting;
    private int starsCount;
    private int commentCount;

    public Post(){
    }

    public Post(String author, String usr, String photoID, String description, Bitmap picURL, Bitmap profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount, Integer commentCount){
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

    public Bitmap getPicURL() {
        return picURL;
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

    public Bitmap getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(Bitmap profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void setPicURL(Bitmap picURL) {
        this.picURL = picURL;
    }
}
