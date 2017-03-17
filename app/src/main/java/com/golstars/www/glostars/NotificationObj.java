package com.golstars.www.glostars;

/**
 * Created by edson on 22/02/17.
 * notification object class
 */

public class NotificationObj {
    private String originID;
    private String picID;
    private String description;
    private String name;
    private String profilePicURL;
    private String picURL;
    private Boolean seen;
    private Boolean checked;
    private String date;



    public NotificationObj(String originID, String picID, String description, String name, String profilePicURL, String picURL, Boolean seen, Boolean checked) {
        this.originID = originID;
        this.picID = picID;
        this.description = description;
        this.name = name;
        this.profilePicURL = profilePicURL;
        this.picURL = picURL;
        this.seen = seen;
        this.checked = checked;
    }

    public String getOriginID() {
        return originID;
    }

    public void setOriginID(String originID) {
        this.originID = originID;
    }

    public String getPicID() {
        return picID;
    }

    public void setPicID(String picID) {
        this.picID = picID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
