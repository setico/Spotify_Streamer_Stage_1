package com.mobilplug.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mobilplug.spotifystreamer.models.Artist;


/**
 * Created by setico on 05/06/15.
 */
public class MainActivity extends AppCompatActivity implements ArtistFragment.Callback{
    private Boolean mTwoPane;
    private final String ARTIST = "artist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if (findViewById(R.id.tracks_container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                Bundle args = new Bundle();
                TrackFragment fragment = new TrackFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tracks_container,fragment )
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onArtistSelected(Artist artist) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(ARTIST, artist);

            TrackFragment fragment = new TrackFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tracks_container, fragment)
                    .commit();

        } else {
            Intent i = new Intent(this, TrackActivity.class);
            i.putExtra(ARTIST,artist);
            startActivity(i);

        }


    }
}
