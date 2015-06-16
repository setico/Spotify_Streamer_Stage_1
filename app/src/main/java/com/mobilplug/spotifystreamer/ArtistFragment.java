package com.mobilplug.spotifystreamer;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilplug.spotifystreamer.models.Artist;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by setico on 06/06/15.
 */
public class ArtistFragment extends Fragment {

    public static final String LOG_TAG = ArtistFragment.class.getSimpleName();
    //private EditText search_artist;
    private SearchView search_artist;
    private ListView listView;
    private ArtistAdapter adapter;
    private ArrayList<Artist> artistList = new ArrayList<Artist>();
    private final String ARTIST_ID = "id";
    private final String ARTIST_NAME = "name";
    private final String ARTIST_LIST = "artistlist";
    private Parcelable listState;

    public ArtistFragment(){

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
        if(savedInstanceState != null && savedInstanceState.containsKey(ARTIST_LIST)) {
            artistList = savedInstanceState.getParcelableArrayList(ARTIST_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARTIST_LIST, artistList);
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

        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        adapter = new ArtistAdapter(
                        getActivity(),
                        artistList);

        //search_artist = (EditText)rootView.findViewById(R.id.search_artist);
        search_artist = (SearchView)rootView.findViewById(R.id.search_artist);
        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        search_artist.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Start searching on submit
                        artistList.clear();
                        if (!query.isEmpty()) {
                            loadArtists(query);
                        }
                        adapter.notifyDataSetChanged();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
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


                new AsyncTask<String, Void, ArrayList<Artist>>() {
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
                    protected ArrayList<Artist> doInBackground(String... strings) {

                        SpotifyApi api = new SpotifyApi();
                        SpotifyService spotify = api.getService();
                        Log.d(LOG_TAG, "Starting searching");
                        Log.d(LOG_TAG, strings[0]);
                        ArtistsPager artistsPager = spotify.searchArtists(strings[0]+"\n");
                        Log.d(LOG_TAG, "End of search");
                        ArrayList<Artist> results = new ArrayList<Artist>();
                        for(kaaes.spotify.webapi.android.models.Artist a:artistsPager.artists.items) {
                            results.add(new Artist(a.id, a.name, a.images));
                        }
                        return results;

                    }

                    @Override
                    protected void onPostExecute(ArrayList<Artist> results) {
                        super.onPostExecute(results);
                        if(results.isEmpty())
                            Toast.makeText(getActivity(), getString(R.string.no_artist), Toast.LENGTH_SHORT).show();
                        artistList.clear();
                        for(Artist a : results)
                            artistList.add(a);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }.execute(search);

            }catch (Exception e){
                e.printStackTrace();
            }

    }




}