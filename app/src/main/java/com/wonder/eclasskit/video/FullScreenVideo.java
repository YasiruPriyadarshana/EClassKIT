package com.wonder.eclasskit.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.wonder.eclasskit.R;

public class FullScreenVideo extends AppCompatActivity {
    private Button minimize,play;
    private SeekBar seekBar;
    private TextView current,duration;
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

                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        decorView.setSystemUiVisibility(uiOptions);
                        handler.postDelayed(this, 5000);
                        play.setVisibility(View.GONE);
                        minimize.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        current.setVisibility(View.GONE);
                        duration.setVisibility(View.GONE);
                    }
                };

                handler.postDelayed(r, 5000);
            }
        });
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");

        VideoView videoView = (VideoView) findViewById(R.id.launcherVideo);
        Uri src = Uri.parse(url);
        videoView.setVideoURI(src);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isPlaying){
//                    videoView.pause();
//                    isPlaying=false;
//                    play.setImageResource(R.drawable.ic_pausevideo);
//                }else {
//                    videoView.start();
//                    isPlaying=true;
//                    play.setImageResource(R.drawable.ic_playvideo);
//                }
            }
        });

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(FullScreenVideo.this,VideoPlayer.class);
                startActivity(intent1);
            }
        });

        videoView.start();
    }


}
