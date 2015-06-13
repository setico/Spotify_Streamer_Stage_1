package com.mobilplug.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilplug.spotifystreamer.models.Track;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by setico on 04/06/15.
 */
public class TrackAdapter extends BaseAdapter {
    private Context context;
    private List<Track> trackList;


    public TrackAdapter(Context context, List<Track> trackList){
        this.context = context;
        this.trackList = trackList;
    }

    public static class ViewHolder {
        public final ImageView album_thumbnail;
        public final TextView album_name;
        public final TextView track_name;

        public ViewHolder(View view) {
            album_thumbnail = (ImageView) view.findViewById(R.id.album_thumbnail);
            album_name = (TextView) view.findViewById(R.id.album_name);
            track_name = (TextView) view.findViewById(R.id.track_name);
        }
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int i) {
        return trackList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Track track = trackList.get(i);
        View v = view;
        ViewHolder viewHolder;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.track_items,viewGroup,false);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        if(track.getAlbumThumbnail(Track.SMALL)!=null)
            Picasso.with(context).load(track.getAlbumThumbnail(Track.SMALL)).into(viewHolder.album_thumbnail);
        else
            Picasso.with(context).load(R.drawable.ic_track).into(viewHolder.album_thumbnail);
        viewHolder.album_name.setText(track.getAlbumName());
        viewHolder.track_name.setText(track.getName());
        return v;

    }


}