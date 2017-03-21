package com.golstars.www.glostars;

/**
 * Created by edson on 20/03/17.
 */

public class GridImages {
    private String author;
    private String picUrl;
    private String timesTamp;

    public GridImages(String author, String picUrl, String timesTamp) {
        this.author = author;
        this.picUrl = picUrl;
        this.timesTamp = timesTamp;
    }

    public GridImages() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(String timesTamp) {
        this.timesTamp = timesTamp;
    }
}
