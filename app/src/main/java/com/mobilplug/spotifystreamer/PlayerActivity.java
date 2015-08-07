package com.mobilplug.spotifystreamer;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by setico on 09/07/15.
 */
public class PlayerActivity extends AppCompatActivity {
    private final String ARTIST = "artist";
    private final String TRACK_LIST = "tracklist";
    private final String SELECTED_TRACK = "track_position";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_player);
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putString(ARTIST, getIntent().getStringExtra(ARTIST));
            args.putInt(SELECTED_TRACK, getIntent().getIntExtra(SELECTED_TRACK, 0));
            args.putParcelableArrayList(TRACK_LIST, ((ArrayList<Parcelable>) getIntent().getParcelableArrayListExtra(TRACK_LIST)));

            PlayerFragment fragment = new PlayerFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_container, fragment)
                    .commit();
        }
    }
}
