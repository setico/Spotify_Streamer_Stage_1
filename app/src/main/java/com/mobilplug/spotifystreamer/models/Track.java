package com.mobilplug.spotifystreamer.models;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by setico on 12/06/15.
 */
public class Track {
    private String id;
    private String name;
    private String albumName;
    private List<Image> albumThumbnails;
    public static int SMALL = 200;
    public static int LARGE = 640;

    public Track(String id, String name, String albumName, List<Image> albumThumbnails) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.albumThumbnails = albumThumbnails;
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Image> getAlbumThumbnails() {
        return albumThumbnails;
    }

    public void setAlbumThumbnails(List<Image> albumThumbnails) {
        this.albumThumbnails = albumThumbnails;
    }

    public String getAlbumThumbnail(int size) {
        if(!albumThumbnails.isEmpty()) {
            for(Image i : albumThumbnails)
            if(i.width==size)
                return i.url;
            return albumThumbnails.get(0).url;
        }
        return null;
    }


}
