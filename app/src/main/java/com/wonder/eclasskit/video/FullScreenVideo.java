package com.wonder.eclasskit.video;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.wonder.eclasskit.R;

public class FullScreenVideo extends AppCompatActivity {
    private Button minimize,play;
    private SeekBar seekBar;
    private TextView current,duration;
    private boolean isPlaying;
    private VideoView videoView;
    private int currentV=0;
    private int durationV=0;
    private AsyncTask task;
    private  int currentPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        play = (Button) findViewById(R.id.f_play_btn);
        minimize = (Button)findViewById(R.id.minimize_btn);
        seekBar = (SeekBar)findViewById(R.id.fseekBar);
        current = (TextView)findViewById(R.id.f_current);
        duration = (TextView)findViewById(R.id.f_duration);

        play.setVisibility(View.GONE);
        minimize.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        current.setVisibility(View.GONE);
        duration.setVisibility(View.GONE);


        View decorView = getWindow().getDecorView();
        int uiOptions =  View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                play.setVisibility(View.VISIBLE);
                minimize.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                current.setVisibility(View.VISIBLE);
                duration.setVisibility(View.VISIBLE);
                seekBar.setProgress(currentPercent);

                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        decorView.setSystemUiVisibility(uiOptions);
                        handler.postDelayed(this, 8000);
                        if (isPlaying) {
                            play.setVisibility(View.GONE);
                            minimize.setVisibility(View.GONE);
                            seekBar.setVisibility(View.GONE);
                            current.setVisibility(View.GONE);
                            duration.setVisibility(View.GONE);
                        }
                    }
                };

                handler.postDelayed(r, 8000);
            }
        });
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");

        videoView = (VideoView) findViewById(R.id.launcherVideo);
        Uri src = Uri.parse(url);
        videoView.setVideoURI(src);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                durationV = mp.getDuration()/1000;
                String durationString = String.format("%02d:%02d",durationV/60,durationV%60);
                duration.setText(durationString);
                seekBar.setMax(100);
                videoView.start();
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    videoView.pause();
                    isPlaying=false;
//                    play.setBackground(R.drawable.ic_pausevideo);
                }else {
                    videoView.start();
                    isPlaying=true;
//                    play.setLabelFor(R.drawable.ic_playvideo);
                }
            }
        });

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=false;
                task.cancel(true);
                finish();
            }
        });
        isPlaying=true;
        task=new FullScreenVideo.videoProgress().execute();

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
                if (isCancelled())
                    break;
                if (isPlaying) {
                    currentV = videoView.getCurrentPosition() / 1000;
                    publishProgress(currentV);
                }

            }while (seekBar.getProgress() <= 100000);

            return null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            try {
                currentPercent = values[0] * 100 / durationV;
                seekBar.setProgress(currentPercent);

                String currentString = String.format("%02d:%02d", values[0] / 60, values[0] % 60);
                current.setText(currentString);

            }catch (Exception e){

            }
        }

    }
}

