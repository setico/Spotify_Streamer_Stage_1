package com.mobilplug.spotifystreamer.models;


import java.io.Serializable;

/**
 * Created by setico on 15/06/15.
 */
public class Image implements Serializable{
    private Integer width;
    private Integer height;
    private String url;

    public Image(Integer width, Integer height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
