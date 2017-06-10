package com.golstars.www.glostars.ModelData;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    private int commentId;
    private String commentMessage;
    private String commenterUserName;
    private String commenterId;
    private String commentTime;
    private String profilePicUrl;
    private String firstName;
    private String lastName;

    public Comment(int commentId, String commentMessage, String commenterUserName, String commenterId, String commentTime, String profilePicUrl, String firstName, String lastName) {
        this.commentId = commentId;
        this.commentMessage = commentMessage;
        this.commenterUserName = commenterUserName;
        this.commenterId = commenterId;
        this.commentTime = commentTime;
        this.profilePicUrl = profilePicUrl;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected Comment(Parcel in) {
        commentId = in.readInt();
        commentMessage = in.readString();
        commenterUserName = in.readString();
        commenterId = in.readString();
        commentTime = in.readString();
        profilePicUrl = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public String getCommenterUserName() {
        return commenterUserName;
    }

    public void setCommenterUserName(String commenterUserName) {
        this.commenterUserName = commenterUserName;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentId);
        dest.writeString(commentMessage);
        dest.writeString(commenterUserName);
        dest.writeString(commenterId);
        dest.writeString(commentTime);
        dest.writeString(profilePicUrl);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}