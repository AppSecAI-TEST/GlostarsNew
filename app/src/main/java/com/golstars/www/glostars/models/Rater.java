package com.golstars.www.glostars.models;

/**
 * Created by edson on 10/03/17.
 */

public class Rater {

    Integer starsCount;
    String raterId;
    String ratingTime;

    public Rater() {
    }

    public Rater(Integer starsCount, String raterId, String ratingTime) {
        this.starsCount = starsCount;
        this.raterId = raterId;
        this.ratingTime = ratingTime;
    }

    public Integer getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(Integer starsCount) {
        this.starsCount = starsCount;
    }

    public String getRaterId() {
        return raterId;
    }

    public void setRaterId(String raterId) {
        this.raterId = raterId;
    }

    public String getRatingTime() {
        return ratingTime;
    }

    public void setRatingTime(String ratingTime) {
        this.ratingTime = ratingTime;
    }
}
