package com.wonder.eclasskit.video;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.wonder.eclasskit.R;

public class FullScreenVideo extends AppCompatActivity {
    private Button minimize, play;
    private SeekBar seekBar;
    private TextView current, duration;
    private boolean isPlaying;
    private VideoView videoView;
    private int currentV = 0;
    private int durationV = 0;
    private AsyncTask task;

    private int currentApiVersion;
    private static final String TAG = "FullScreenVideo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        play = (Button) findViewById(R.id.f_play_btn);
        minimize = (Button) findViewById(R.id.minimize_btn);
        seekBar = (SeekBar) findViewById(R.id.fseekBar);
        current = (TextView) findViewById(R.id.f_current);
        duration = (TextView) findViewById(R.id.f_duration);

        play.setVisibility(View.GONE);
        minimize.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        current.setVisibility(View.GONE);
        duration.setVisibility(View.GONE);



        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");


        videoView = (VideoView) findViewById(R.id.launcherVideo);
        Uri src = Uri.parse(url);
        videoView.setVideoURI(src);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                durationV = mp.getDuration() / 1000;
                String durationString = String.format("%02d:%02d", durationV / 60, durationV % 60);
                duration.setText(durationString);
                seekBar.setMax(100);
                videoView.start();
                isPlaying=true;
                task=new vProgress().execute();
                play.setBackgroundResource(R.drawable.ic_pausevideo);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(videoView != null && fromUser){
                    videoView.seekTo(progress * durationV *10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isPlaying) {
                            play.setVisibility(View.GONE);
                            minimize.setVisibility(View.GONE);
                            seekBar.setVisibility(View.GONE);
                            current.setVisibility(View.GONE);
                            duration.setVisibility(View.GONE);
                        }
                    }
                }, 1000);
            }
        });



        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }


        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.VISIBLE);
                minimize.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                current.setVisibility(View.VISIBLE);
                duration.setVisibility(View.VISIBLE);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isPlaying) {
                            play.setVisibility(View.GONE);
                            minimize.setVisibility(View.GONE);
                            seekBar.setVisibility(View.GONE);
                            current.setVisibility(View.GONE);
                            duration.setVisibility(View.GONE);
                        }
                    }
                }, 2000);

            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    videoView.pause();
                    isPlaying=false;
                    play.setBackgroundResource(R.drawable.ic_playvideo);
                }else {
                    videoView.start();
                    isPlaying=true;
                    play.setBackgroundResource(R.drawable.ic_pausevideo);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isPlaying) {
                                play.setVisibility(View.GONE);
                                minimize.setVisibility(View.GONE);
                                seekBar.setVisibility(View.GONE);
                                current.setVisibility(View.GONE);
                                duration.setVisibility(View.GONE);
                            }
                        }
                    }, 500);
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



    }




    @Override
    protected void onStop() {
        super.onStop();
        isPlaying = false;

    }

    public class vProgress extends AsyncTask<Void, Integer, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            do {

                if (isCancelled())
                    break;
                if (isPlaying){
                    currentV = videoView.getCurrentPosition() / 1000;
                    publishProgress(currentV);
                }

            }while (seekBar.getProgress() <= 100);

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            try {
                int currentPercent = values[0] * 100 / durationV;
                seekBar.setProgress(currentPercent);

                String currentString = String.format("%02d:%02d", values[0] / 60, values[0] % 60);
                current.setText(currentString);


            }catch (Exception e){

            }
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        task.cancel(true);
        finish();
    }
}

