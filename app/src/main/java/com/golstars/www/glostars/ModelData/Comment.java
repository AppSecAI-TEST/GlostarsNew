package com.golstars.www.glostars.ModelData;

public class Comment {

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

}