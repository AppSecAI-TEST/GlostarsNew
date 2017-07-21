package com.golstars.www.glostars.ModelData;

import android.os.Parcel;
import android.os.Parcelable;

import com.golstars.www.glostars.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Hashtag implements Parcelable{

    private Poster poster;
    private int id;
    private String description;
    private String picUrl;
    private String picUrlSmall;
    private String picUrlMini;
    private String picUrlMedium;
    private String privacy;
    private boolean isCompeting;
    private boolean isfeatured;
    private String uploaded;
    private int starsCount;
    private int myStarCount;
    private List<Comment> comments = new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();


    private boolean is_mutual;
    private boolean me_follow;
    private boolean he_follow;

    protected Hashtag(Parcel in) {
        id = in.readInt();
        description = in.readString();
        picUrl = in.readString();
        picUrlSmall  = in.readString();
        picUrlMini  = in.readString();
        picUrlMedium  = in.readString();
        privacy = in.readString();
        isCompeting = in.readByte() != 0;
        isfeatured = in.readByte() != 0;
        uploaded = in.readString();
        starsCount = in.readInt();
        myStarCount = in.readInt();
        is_mutual = in.readByte() != 0;
        me_follow = in.readByte() != 0;
        he_follow = in.readByte() != 0;
        in.readTypedList(ratings, Rating.CREATOR);
        in.readTypedList(comments, Comment.CREATOR);


    }

    public static final Creator<Hashtag> CREATOR = new Creator<Hashtag>() {
        @Override
        public Hashtag createFromParcel(Parcel in) {
            return new Hashtag(in);
        }

        @Override
        public Hashtag[] newArray(int size) {
            return new Hashtag[size];
        }
    };

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
                ", picUrlMedium='" + picUrlMedium + '\'' +
                ", picUrlMini='" + picUrlMini + '\'' +
                ", picUrlSmall='" + picUrlSmall + '\'' +
                ", privacy='" + privacy + '\'' +
                ", isCompeting=" + isCompeting +
                ", isfeatured=" + isfeatured +
                '}';

    }
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        //dest.writeParcelable(poster, flags);

        dest.writeInt(id);
        dest.writeString(description);
        dest.writeString(picUrl);
        dest.writeString(picUrlMedium);
        dest.writeString(picUrlMini);
        dest.writeString(picUrlSmall);
        dest.writeString(privacy);
        dest.writeByte((byte) (isCompeting ? 1 : 0));
        dest.writeByte((byte) (isfeatured ? 1 : 0));
        dest.writeString(uploaded);
        dest.writeInt(starsCount);
        dest.writeInt(myStarCount);
        dest.writeByte((byte) (is_mutual ? 1 : 0));
        dest.writeByte((byte) (me_follow ? 1 : 0));
        dest.writeByte((byte) (he_follow ? 1 : 0));
        dest.writeTypedList(ratings);
        dest.writeTypedList(comments);
    }

    public String getPicUrlSmall() {
        return picUrlSmall;
    }

    public void setPicUrlSmall(String picUrlSmall) {
        this.picUrlSmall = picUrlSmall;
    }

    public String getPicUrlMini() {
        return picUrlMini;
    }

    public void setPicUrlMini(String picUrlMini) {
        this.picUrlMini = picUrlMini;
    }

    public String getPicUrlMedium() {
        return picUrlMedium;
    }

    public void setPicUrlMedium(String picUrlMedium) {
        this.picUrlMedium = picUrlMedium;
    }
}