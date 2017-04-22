package com.golstars.www.glostars.ModelData;

public class Rating {

    private int starsCount;
    private String raterId;
    private String ratingTime;

    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
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