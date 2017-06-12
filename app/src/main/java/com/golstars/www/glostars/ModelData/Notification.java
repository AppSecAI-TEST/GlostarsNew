package com.golstars.www.glostars.ModelData;

/**
 * Created by Arif on 6/9/2017.
 */

public class Notification {
    public int id;
    public String userId;
    public String originatedById;
    public int pictureId;
    public boolean seen;
    public boolean checked;
    public String date;
    public String description;
    public String name;
    public String profilePicURL;
    public String picUrl;

    public Notification() {
    }


    public Notification(int id, String userId, String originatedById, int pictureId, boolean seen, boolean checked, String date, String description, String name, String profilePicURL, String picUrl) {
        super();
        this.id = id;
        this.userId = userId;
        this.originatedById = originatedById;
        this.pictureId = pictureId;
        this.seen = seen;
        this.checked = checked;
        this.date = date;
        this.description = description;
        this.name = name;
        this.profilePicURL = profilePicURL;
        this.picUrl = picUrl;
    }
}
