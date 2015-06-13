package com.mobilplug.spotifystreamer.models;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by setico on 12/06/15.
 */
public class Artist {
    private String id;
    private String name;
    private List<Image> thumbnails;

    public Artist(String id, String name, List<Image> thumbnails) {
        this.id = id;
        this.name = name;
        this.thumbnails = thumbnails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Image> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getThumbnail() {
        if(!thumbnails.isEmpty()) {
            return thumbnails.get(0).url;
        }
        return null;
    }
}
