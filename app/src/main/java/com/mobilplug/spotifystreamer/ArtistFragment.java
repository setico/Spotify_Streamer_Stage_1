package com.mobilplug.spotifystreamer;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilplug.spotifystreamer.models.Artist;

import java.util.LinkedList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by setico on 06/06/15.
 */
public class ArtistFragment extends Fragment {

    public static final String LOG_TAG = ArtistFragment.class.getSimpleName();
    private EditText search_artist;
    private ListView listView;
    private ArtistAdapter adapter;
    private List<Artist> artistList = new LinkedList<Artist>();
    private final String ARTIST_ID = "id";
    private final String ARTIST_NAME = "name";

    public ArtistFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        adapter = new ArtistAdapter(
                        getActivity(),
                        artistList);

        //
        search_artist = (EditText)rootView.findViewById(R.id.search_artist);
        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        search_artist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                artistList.clear();
                if (!search_artist.getText().toString().isEmpty()) {
                    //artistList.addAll(Utils.loadArtists(getActivity(), search_artist.getText().toString()));
                    loadArtists(search_artist.getText().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(getActivity(), TrackActivity.class);
                i.putExtra(ARTIST_ID, artistList.get(position).getId());
                i.putExtra(ARTIST_NAME, artistList.get(position).getName());
                startActivity(i);
            }
        });



        return rootView;
    }

    public void loadArtists(final String search) {
        if(Utils.checkNetworkState(getActivity()))
            try {
                new AsyncTask<String, Void, List<Artist>>() {

                    @Override
                    protected List<Artist> doInBackground(String... strings) {

                        SpotifyApi api = new SpotifyApi();
                        SpotifyService spotify = api.getService();
                        Log.d(LOG_TAG, "Starting searching");
                        Log.d(LOG_TAG, strings[0]);
                        ArtistsPager artistsPager = spotify.searchArtists(strings[0]+"\n");
                        Log.d(LOG_TAG, "End of search");
                        List<Artist> results = new LinkedList<Artist>();
                        for(kaaes.spotify.webapi.android.models.Artist a:artistsPager.artists.items) {
                            results.add(new Artist(a.id, a.name, a.images));
                        }
                        return results;

                    }

                    @Override
                    protected void onPostExecute(List<Artist> results) {
                        super.onPostExecute(results);
                        if(results.isEmpty())
                            Toast.makeText(getActivity(), getString(R.string.no_artist), Toast.LENGTH_SHORT).show();
                        artistList.clear();
                        for(Artist a : results)
                            artistList.add(a);
                        adapter.notifyDataSetChanged();
                    }

                }.execute(search);

            }catch (Exception e){
                e.printStackTrace();
            }


    }


}