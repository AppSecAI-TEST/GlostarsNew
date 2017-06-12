package com.golstars.www.glostars.ModelData;

import android.os.Parcel;
import android.os.Parcelable;

public class Rating implements Parcelable {

    private int starsCount;
    private String raterId;
    private String ratingTime;

    protected Rating(Parcel in) {
        starsCount = in.readInt();
        raterId = in.readString();
        ratingTime = in.readString();
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(starsCount);
        dest.writeString(raterId);
        dest.writeString(ratingTime);
    }
}