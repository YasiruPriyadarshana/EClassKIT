package com.wonder.eclasskit.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.wonder.eclasskit.R;

public class FullScreenVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        VideoView videoView = (VideoView) findViewById(R.id.launcherVideo);
        Uri src = Uri.parse("https://firebasestorage.googleapis.com/v0/b/learn-with-chirath.appspot.com/o/videos%2FWhat%20We%20Miss%20When%20We're%20Complaining%20by%20Jay%20shetty%20_%20wordables_HD.mp4?alt=media&token=5ccf1729-143a-4de6-9e06-4394dc0389dc");
        videoView.setVideoURI(src);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });



        videoView.start();
    }


}
