package com.mobilplug.spotifystreamer;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mobilplug.spotifystreamer.models.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaPlayer;

/**
 * Created by setico on 09/07/15.
 */
public class PlayerFragment extends Fragment {
    protected static FFmpegMediaPlayer player;
    private ProgressBar progress;
    private Handler seekHandler = new Handler();
    private ImageButton btn_previous;
    private ImageButton btn_play;
    private ImageButton btn_next;
    private TextView artist_namev;
    private TextView album_namev;
    private ImageView album_thumbnail;
    private TextView track_namev;
    private TextView current_duration;
    private TextView final_duration;
    private SeekBar seekBarv;
    private Boolean isPlaying;
    private String artist_name;
    private Track track;
    private int track_position;
    private final String TRACK_LIST = "tracklist";
    private final String SELECTED_TRACK = "track_position";
    private final String ARTIST = "artist";
    private ArrayList<Parcelable> trackList = new ArrayList<Parcelable>();
    private final int FINAL_DURATION = 30000; //30s


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        track_position = getArguments().getInt(SELECTED_TRACK, 0);
        trackList = ((ArrayList<Parcelable>) getArguments().getParcelableArrayList(TRACK_LIST));
        artist_name = getArguments().getString(ARTIST);


        if (player != null) {
            player.pause();
        }

        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        btn_previous = (ImageButton) rootView.findViewById(R.id.btn_previous);
        btn_play = (ImageButton) rootView.findViewById(R.id.btn_play);
        btn_next = (ImageButton) rootView.findViewById(R.id.btn_next);
        seekBarv = (SeekBar) rootView.findViewById(R.id.seekBar);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        artist_namev = (TextView) rootView.findViewById(R.id.artist_name);
        album_namev = (TextView) rootView.findViewById(R.id.album_name);
        track_namev = (TextView) rootView.findViewById(R.id.track_name);
        current_duration = (TextView) rootView.findViewById(R.id.current_duration);
        final_duration = (TextView) rootView.findViewById(R.id.final_duration);
        album_thumbnail = (ImageView) rootView.findViewById(R.id.album_thumbnail);




        seekBarv.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (player != null&&fromUser) {
                    player.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        // prev button on click listener
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                track_position--;
                if (track_position < 0)
                    track_position = trackList.size() - 1;
                btn_play.setImageResource(android.R.drawable.ic_media_play);
                if (player != null) {
                    player.release();
                }

                loadMusic();
            }
        });

        // next button on click listener
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                track_position++;
                if (track_position > trackList.size() - 1)
                    track_position = 0;

                //setUi();
                btn_play.setImageResource(android.R.drawable.ic_media_play);
                if (player != null) {
                    player.release();
                }
                loadMusic();
            }
        });

        run.run();
        loadMusic();

        return rootView;
    }


    private void loadMusic() {

        track = (Track)trackList.get(track_position);
        artist_namev.setText(artist_name);
        album_namev.setText(track.getAlbumName());
        track_namev.setText(track.getName());
        // seekbar setup and progress listener
        seekBarv.setMax(FINAL_DURATION);
        Picasso.with(getActivity().getApplicationContext()).load(track.getAlbumThumbnail(Track.LARGE)).into(album_thumbnail);
        // set start position of track
        current_duration.setText("0:00");
        // set end duration of track//preview is 30s
        final_duration.setText("0:30");

        int seconds = (FINAL_DURATION / 1000) % 60;
        int minutes = (FINAL_DURATION / 1000) / 60;
        if (seconds < 10) {
            final_duration.setText(String.valueOf(minutes) + ":0" + String.valueOf(seconds));
        } else {
            final_duration.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
        }

        progress.setVisibility(View.VISIBLE);
            player = new FFmpegMediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                player.setDataSource(track.getPreview_url());
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // default not playing
            isPlaying = false;

            btn_play.setClickable(false);
            btn_play.setImageResource(android.R.drawable.ic_media_pause);

            player.setOnPreparedListener(new FFmpegMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(FFmpegMediaPlayer mp) {

                    btn_play.setClickable(true);
                    btn_play.setImageResource(android.R.drawable.ic_media_pause);

                    player.start();
                    //now is playing
                    isPlaying = true;
                    progress.setVisibility(View.GONE);

                    // play | pause
                    btn_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isPlaying) {
                                player.start();
                                btn_play.setImageResource(android.R.drawable.ic_media_pause);
                                isPlaying = true;
                            } else {
                                player.pause();
                                isPlaying = false;
                                btn_play.setImageResource(android.R.drawable.ic_media_play);
                            }
                        }
                    });
                }
            });

        player.setOnCompletionListener(new FFmpegMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(FFmpegMediaPlayer fFmpegMediaPlayer) {
                isPlaying = false;
                btn_play.setImageResource(android.R.drawable.ic_media_play);
                player.seekTo(0);
            }
        });


        player.setOnErrorListener(new FFmpegMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(FFmpegMediaPlayer fFmpegMediaPlayer, int i, int i1) {
                //player.release();
                return false;
            }
        });


        }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (player != null) {
                seekBarv.setProgress(player.getCurrentPosition());
                int seconds = ((player.getCurrentPosition() / 1000) % 60);
                int minutes = ((player.getCurrentPosition() / 1000) / 60);
                if (seconds < 10) {
                    current_duration.setText(String.valueOf(minutes) + ":0" + String.valueOf(seconds));
                } else {
                    current_duration.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                }
            }
            seekHandler.postDelayed(run, 1000);

        }
    };

    @Override
    public void onDetach() {
        player.stop();
        super.onDetach();
    }

}
