package com.mobilplug.spotifystreamer;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.HashMap;
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
    private ArrayList<Track> trackList = new ArrayList<Track>();
    private String id;
    private String name;
    private final String ARTIST_ID = "id";
    private final String ARTIST_NAME = "name";
    private final String TRACK_LIST = "tracklist";
    private Parcelable listState;


    public TrackFragment(){

    }

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        listState = listView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getActivity().getIntent().getStringExtra(ARTIST_ID);
        name = getActivity().getIntent().getStringExtra(ARTIST_NAME);
        if(savedInstanceState != null && savedInstanceState.containsKey(TRACK_LIST)) {
            trackList = savedInstanceState.getParcelableArrayList(TRACK_LIST);
        }else {
            loadTracks(id);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRACK_LIST, trackList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(listState != null) {
            listView.onRestoreInstanceState(listState);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setSubtitle(name);

        View rootView = inflater.inflate(R.layout.fragment_track, container, false);

        adapter = new TrackAdapter(
                        getActivity(),
                        trackList);

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
                new AsyncTask<String, Void, ArrayList<Track>>() {
                    private ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new ProgressDialog(getActivity());
                        dialog.setIndeterminate(true);
                        dialog.setMessage(getString(R.string.loading));
                        dialog.show();
                    }

                    @Override
                    protected ArrayList<Track> doInBackground(String... strings) {

                        SpotifyApi api = new SpotifyApi();
                        SpotifyService spotify = api.getService();
                        Log.d(LOG_TAG, "Starting searching");
                        Log.d(LOG_TAG, strings[0]);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(SpotifyService.COUNTRY, Utils.getCountryCode(getActivity()));
                        List<kaaes.spotify.webapi.android.models.Track> tracks = spotify.getArtistTopTrack(strings[0],map).tracks;
                        Log.d(LOG_TAG, "End of search");
                        ArrayList<Track> results = new ArrayList<Track>();
                        for (kaaes.spotify.webapi.android.models.Track t : tracks)
                        results.add(new Track(t.id, t.name, t.album.name, t.album.images));
                        return results;

                    }

                    @Override
                    protected void onPostExecute(ArrayList<Track> results) {
                        super.onPostExecute(results);
                        if(results.isEmpty())
                            Toast.makeText(getActivity(),getString(R.string.no_top_track),Toast.LENGTH_SHORT).show();
                        trackList.clear();
                        for(Track t: results)
                        trackList.add(t);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }.execute(id);

            }catch (Exception e){
                e.printStackTrace();
            }

    }

}

