package com.mobilplug.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by setico on 12/06/15.
 */
public class Artist implements Parcelable {
    private String id;
    private String name;
    private ArrayList<Image> thumbnails;

    public Artist(String id, String name, List<kaaes.spotify.webapi.android.models.Image> thumbnails) {
        this.id = id;
        this.name = name;
        this.thumbnails = new ArrayList<Image>();
        for(kaaes.spotify.webapi.android.models.Image image : thumbnails)
        this.thumbnails.add(new Image(image.width, image.height, image.url));
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

    public ArrayList<Image> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<Image> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getThumbnail() {
        if(!thumbnails.isEmpty()) {
            return thumbnails.get(0).getUrl();
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeSerializable(this.thumbnails);
    }

    private Artist(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.thumbnails = (ArrayList<Image>) in.readSerializable();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
