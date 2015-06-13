package com.mobilplug.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Created by setico on 05/06/15.
 */
public class TrackActivity extends AppCompatActivity{
    private final String ARTIST_ID = "id";
    private final String ARTIST_NAME = "name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);


        if (savedInstanceState == null) {
            // Create the track fragment and add it to the activity
            // using a fragment transaction.
            String id = getIntent().getStringExtra(ARTIST_ID);
            String name = getIntent().getStringExtra(ARTIST_NAME);
            Bundle arguments = new Bundle();
            arguments.putString(ARTIST_ID, id);
            arguments.putString(ARTIST_NAME, name);

            TrackFragment fragment = new TrackFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.track_container, fragment)
                    .commit();
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



}
