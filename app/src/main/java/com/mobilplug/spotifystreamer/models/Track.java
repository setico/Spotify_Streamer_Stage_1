package com.mobilplug.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by setico on 12/06/15.
 */
public class Track implements Parcelable {
    private String id;
    private String name;
    private String albumName;
    private String preview_url;
    private String uri;
    private Long duration;
    private ArrayList<Image> albumThumbnails;
    public static int SMALL = 200;
    public static int LARGE = 640;

    public Track(String id, String name, String albumName, List<kaaes.spotify.webapi.android.models.Image> albumThumbnails, String preview_url,String uri, Long duration) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.albumThumbnails = new ArrayList<Image>();
        for(kaaes.spotify.webapi.android.models.Image image : albumThumbnails)
            this.albumThumbnails.add(new Image(image.width, image.height, image.url));
        this.uri = uri;
        this.preview_url = preview_url;
        this.duration = duration;
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

    public ArrayList<Image> getAlbumThumbnails() {
        return albumThumbnails;
    }

    public void setAlbumThumbnails(ArrayList<Image> albumThumbnails) {
        this.albumThumbnails = albumThumbnails;
    }

    public String getAlbumThumbnail(int size) {
        if(!albumThumbnails.isEmpty()) {
            for(Image i : albumThumbnails)
            if(i.getWidth()==size)
                return i.getUrl();
            return albumThumbnails.get(0).getUrl();
        }
        return null;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.albumName);
        dest.writeString(this.preview_url);
        dest.writeString(this.uri);
        dest.writeLong(this.duration);
        dest.writeSerializable(this.albumThumbnails);
    }

    private Track(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.albumName = in.readString();
        this.preview_url = in.readString();
        this.uri = in.readString();
        this.duration = in.readLong();
        this.albumThumbnails = (ArrayList<Image>) in.readSerializable();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
