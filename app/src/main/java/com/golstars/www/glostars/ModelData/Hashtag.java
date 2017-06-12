package com.golstars.www.glostars.ModelData;

import com.golstars.www.glostars.Timestamp;

import java.util.List;

public class Hashtag {

    private Poster poster;
    private int id;
    private String description;
    private String picUrl;
    private String privacy;
    private boolean isCompeting;
    private boolean isfeatured;
    private String uploaded;
    private int starsCount;
    private int myStarCount;
    private List<Comment> comments = null;
    private List<Rating> ratings = null;


    private boolean is_mutual;
    private boolean me_follow;
    private boolean he_follow;

    public boolean is_mutual() {
        return is_mutual;
    }

    public void setIs_mutual(boolean is_mutual) {
        this.is_mutual = is_mutual;
    }

    public boolean isMe_follow() {
        return me_follow;
    }

    public void setMe_follow(boolean me_follow) {
        this.me_follow = me_follow;
    }

    public boolean isHe_follow() {
        return he_follow;
    }

    public void setHe_follow(boolean he_follow) {
        this.he_follow = he_follow;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public boolean isIsCompeting() {
        return isCompeting;
    }

    public void setIsCompeting(boolean isCompeting) {
        this.isCompeting = isCompeting;
    }

    public boolean isIsfeatured() {
        return isfeatured;
    }

    public void setIsfeatured(boolean isfeatured) {
        this.isfeatured = isfeatured;
    }

    public String getUploaded() {
        try {
            return Timestamp.getInterval(Timestamp.getOwnZoneDateTime(uploaded));
        } catch (Exception e) {
            System.out.println("Time Error photo id is "+id);
            return "";
        }
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public int getMyStarCount() {
        return myStarCount;
    }

    public void setMyStarCount(int myStarCount) {
        this.myStarCount = myStarCount;
    }

    @Override
    public String toString() {
        return "Hashtag{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", privacy='" + privacy + '\'' +
                ", isCompeting=" + isCompeting +
                ", isfeatured=" + isfeatured +
                '}';
    }
}