package com.mobilplug.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilplug.spotifystreamer.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by setico on 04/06/15.
 */
public class ArtistAdapter extends BaseAdapter {
    private Context context;
    private List<Artist> artistList;


    public ArtistAdapter(Context context, List<Artist> artistList){
        this.context = context;
        this.artistList = artistList;
    }

    public static class ViewHolder {
        public final ImageView artist_thumbnail;
        public final TextView artist_name;

        public ViewHolder(View view) {
            artist_thumbnail = (ImageView) view.findViewById(R.id.artist_thumbnail);
            artist_name = (TextView) view.findViewById(R.id.artist_name);
        }
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @Override
    public Object getItem(int i) {
        return artistList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Artist artist = artistList.get(i);
        View v = view;
        ViewHolder viewHolder;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.artist_items,viewGroup,false);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        if(artist.getThumbnail()!=null)
            Picasso.with(context).load(artist.getThumbnail()).into(viewHolder.artist_thumbnail);
        else
            Picasso.with(context).load(R.drawable.ic_artist).into(viewHolder.artist_thumbnail);
        viewHolder.artist_name.setText(artist.getName());
        return v;

    }



}