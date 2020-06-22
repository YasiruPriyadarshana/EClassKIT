package com.wonder.eclasskit.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wonder.eclasskit.Object.CommentM;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity implements AdpterVideoComment.CallbackInterface{
    private VideoView videoView;
    private ImageView playBtn;
    private TextView currentTime;
    private TextView durationtime;
    private SeekBar currentProgress;
    private ProgressBar bufferBar;
    private boolean isPlaying;
    private DatabaseReference databaseReference,databaseRfTeacher;
    private Uri videoUri;
    private int cmtSort=2;
    private int current=0;
    private int duration=0;

    private ValueEventListener valueEventListener;
    private AdpterVideoComment adapter;
    private ListView CommentListView;
    private ArrayList<CommentM> commentMS;
    private String cmt_str,uri,uname,name;
    private String[] array;
    private View v;
    private Button updateComment,fullscreen;
    private ImageView image,imageView;
    private ImageButton imagepdf;
    private EditText desc;
    private AdpterVideoComment.CallbackInterface anInterface;
    private AsyncTask task;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        uname=intent.getStringExtra("name");

        databaseReference = FirebaseDatabase.getInstance().getReference("VideoComments/"+ Common.uid+"/" +uname.substring(0, uname.length() - 4));
        databaseRfTeacher=FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uid);
        commentMS= new ArrayList<>();
        CommentListView=(ListView)findViewById(R.id.video_comment_list);
        anInterface=this;

        videoView=(VideoView)findViewById(R.id.videoView);
        playBtn=(ImageView)findViewById(R.id.play_btn);
        currentTime=(TextView)findViewById(R.id.currentTime);
        durationtime=(TextView)findViewById(R.id.durationTime);
        currentProgress=(SeekBar) findViewById(R.id.videoProgress);
        bufferBar=(ProgressBar)findViewById(R.id.bufferProgress);
        fullscreen=(Button)findViewById(R.id.fullScreen_btn);


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
                currentProgress.setMax(100);
                videoView.start();
            }
        });




        isPlaying=true;
        playBtn.setImageResource(R.drawable.ic_playvideo);

        task=new videoProgress().execute();

        currentProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(videoView != null && fromUser){
                    videoView.seekTo(progress * duration *10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    videoView.pause();
                    isPlaying=false;
                    playBtn.setImageResource(R.drawable.ic_pausevideo);
                }else {
                    videoView.start();
                    isPlaying=true;
                    playBtn.setImageResource(R.drawable.ic_playvideo);
                }

            }
        });

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=false;
                Intent intent1 =new Intent(VideoPlayer.this,FullScreenVideo.class);
                intent1.putExtra("url",url);
                startActivity(intent1);
            }
        });

        viewVideoComments();
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

            }while (currentProgress.getProgress() <= 100000);

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

    private void viewVideoComments(){

        valueEventListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    CommentM commentM = postSnapshot.getValue(CommentM.class);
                    commentMS.add(commentM);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);

                }


                adapter = new AdpterVideoComment(getApplicationContext(),R.layout.itemcomment,commentMS,uname,keys,getName(),anInterface);

                if (CommentListView.getFooterViewsCount() > 0)
                {
                    CommentListView.removeFooterView(v);
                }

                v=getLayoutInflater().inflate(R.layout.footerviewcomment, null);

                CommentListView.addFooterView(v);


                desc = (EditText) v.findViewById(R.id.comment_in);
                updateComment = (Button) v.findViewById(R.id.addcmt);
                Button im = (Button) v.findViewById(R.id.addimage);
                im.setVisibility(View.GONE);






                updateComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cmt_str=desc.getText().toString();
                        if (cmt_str.isEmpty()){
                            Toast.makeText(VideoPlayer.this, "Type comment", Toast.LENGTH_SHORT).show();
                        }else {
                            uplodeFile();
                        }
                    }
                });




                CommentListView.setAdapter(adapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void uplodeFile() {

        CommentM commentobj = new CommentM(getName(), cmt_str,null);
        databaseReference.child(cmtSort+""+databaseReference.push().getKey()).setValue(commentobj);
        Toast.makeText(VideoPlayer.this, "Add new comment", Toast.LENGTH_SHORT).show();
        adapter.clear();

    }



    public String getName(){
        try {
            if (Common.limit == 1) {
                FileInputStream fileInputStream = openFileInput("apprequirement.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();


                String lines;
                while ((lines = bufferedReader.readLine()) != null) {
                    stringBuffer.append(lines + "\n");
                }
                String str = stringBuffer.toString();
                array = str.split(",");

                name = array[0];
            }else{
                databaseRfTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name =  dataSnapshot.child("name").getValue().toString();
                        cmtSort = 1;
                        Common.cmtSort = "1";
                        Common.repname = name;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return name;
    }










    @Override
    public void onHandleSelectionClear() {
        adapter.clear();
    }



    @Override
    public void popUp(final String key,final String uri) {
        databaseReference.removeEventListener(valueEventListener);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Are you sure?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!TextUtils.isEmpty(uri)) {
                    StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
                    photoRef.delete();
                }


                databaseReference.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(VideoPlayer.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                recreate();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();



    }


    @Override
    public void popUpReply(final String key,final String uri,final DatabaseReference dr) {
        databaseReference.removeEventListener(valueEventListener);
        AlertDialog.Builder adb2 = new AlertDialog.Builder(this);
        adb2.setMessage("Are you sure?");
        adb2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!TextUtils.isEmpty(uri)) {
                    StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
                    photoRef.delete();
                }


                dr.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(VideoPlayer.this, "Reply deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                recreate();
            }
        });
        adb2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb2.show();
//        Toast.makeText(Comments.this, "reply: "+dr+"key: "+key, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPlaying = false;
        task.cancel(true);
        finish();
    }


}
