package com.mobilplug.spotifystreamer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilplug.spotifystreamer.models.Track;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by setico on 05/06/15.
 */
public class TrackFragment extends Fragment {

    public static final String LOG_TAG = TrackFragment.class.getSimpleName();
    private ListView listView;
    private TrackAdapter adapter;
    private List<Track> trackList = new LinkedList<Track>();
    private String id;
    private String name;
    private final String ARTIST_ID = "id";
    private final String ARTIST_NAME = "name";


    public TrackFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_track, container, false);
        id = getActivity().getIntent().getStringExtra(ARTIST_ID);
        name = getActivity().getIntent().getStringExtra(ARTIST_NAME);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setSubtitle(name);



        adapter = new TrackAdapter(
                        getActivity(),
                        trackList);
        loadTracks(id);

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });



        return rootView;
    }


    public void loadTracks(final String id) {
        if(Utils.checkNetworkState(getActivity()))
            try {
                new AsyncTask<String, Void, List<Track>>() {

                    @Override
                    protected List<Track> doInBackground(String... strings) {

                        SpotifyApi api = new SpotifyApi();
                        SpotifyService spotify = api.getService();
                        Log.d(LOG_TAG, "Starting searching");
                        Log.d(LOG_TAG, strings[0]);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(SpotifyService.COUNTRY, Utils.getCountryCode(getActivity()));
                        List<kaaes.spotify.webapi.android.models.Track> tracks = spotify.getArtistTopTrack(strings[0],map).tracks;
                        Log.d(LOG_TAG, "End of search");
                        List<Track> results = new LinkedList<Track>();
                        for (kaaes.spotify.webapi.android.models.Track t : tracks)
                        results.add(new Track(t.id, t.name, t.album.name, t.album.images));
                        return results;

                    }

                    @Override
                    protected void onPostExecute(List<Track> results) {
                        super.onPostExecute(results);
                        if(results.isEmpty())
                            Toast.makeText(getActivity(),getString(R.string.no_top_track),Toast.LENGTH_SHORT).show();
                        trackList.clear();
                        for(Track t: results)
                        trackList.add(t);
                        adapter.notifyDataSetChanged();
                    }

                }.execute(id);

            }catch (Exception e){
                e.printStackTrace();
            }

    }

}

