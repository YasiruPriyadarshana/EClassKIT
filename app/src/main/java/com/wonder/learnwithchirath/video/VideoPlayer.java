package com.wonder.learnwithchirath.video;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.wonder.learnwithchirath.R;

public class VideoPlayer extends AppCompatActivity {
    private VideoView videoView;
    private ImageView playBtn;
    private TextView currentTime;
    private TextView durationtime;
    private ProgressBar currentProgress;
    private ProgressBar bufferBar;
    private boolean isPlaying;

    private Uri videoUri;

    private int current=0;
    private int duration=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        String name=intent.getStringExtra("name");

        videoView=(VideoView)findViewById(R.id.videoView);
        playBtn=(ImageView)findViewById(R.id.play_btn);
        currentTime=(TextView)findViewById(R.id.currentTime);
        durationtime=(TextView)findViewById(R.id.durationTime);
        currentProgress=(ProgressBar)findViewById(R.id.videoProgress);
        bufferBar=(ProgressBar)findViewById(R.id.bufferProgress);
        currentProgress.setMax(100);

        videoUri=Uri.parse(url);

        videoView.setVideoURI(videoUri);
        videoView.requestFocus();

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        bufferBar.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        bufferBar.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                        bufferBar.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                duration = mp.getDuration()/1000;
                String durationString = String.format("%02d:%02d",duration/60,duration%60);
                durationtime.setText(durationString);
            }
        });

        videoView.start();
        isPlaying=true;
//        playBtn.setImageResource(R.drawable.ic_play);

        new videoProgress().execute();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    videoView.pause();
                    isPlaying=false;
//                    playBtn.setImageResource(R.drawable.ic_pause);
                }else {
                    videoView.start();
                    isPlaying=true;
//                    playBtn.setImageResource(R.drawable.ic_play);
                }
            }
        });


    }



    @Override
    protected void onStop() {
        super.onStop();
        isPlaying = false;
    }

    public class videoProgress extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            do {
                if (isPlaying) {
                    current = videoView.getCurrentPosition() / 1000;
                    publishProgress(current);
                }

            }while (currentProgress.getProgress() <= 100);

            return null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            try {
                int currentPercent = values[0] * 100 / duration;
                currentProgress.setProgress(currentPercent);

                String currentString = String.format("%02d:%02d", values[0] / 60, values[0] % 60);
                currentTime.setText(currentString);

            }catch (Exception e){

            }
        }
    }
}
