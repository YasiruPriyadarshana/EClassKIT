package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Teachers;
import com.wonder.eclasskit.Object.UploadPDF;

public class AboutUs extends AppCompatActivity {
    private ImageButton Web,Facebook,Twitter,Youtube;
    private Button setname,setdesc;
    private DatabaseReference databaseReference;
    String weburl,fburl,twitterurl,youtubeurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Web=(ImageButton)findViewById(R.id.web);
        Facebook=(ImageButton)findViewById(R.id.facebok);
        Twitter=(ImageButton)findViewById(R.id.twitter);
        Youtube=(ImageButton)findViewById(R.id.youtube);
        setname=(Button)findViewById(R.id.teacherN_btn);
        setdesc=(Button)findViewById(R.id.teacher_desc_btn);

        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/"+ Common.uid);

        setTeacherData();



        Web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(weburl)){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(weburl));
                    startActivity(i);
                }else {
                    Toast.makeText(AboutUs.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(fburl)){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(fburl));
                    startActivity(i);
                }else {
                    Toast.makeText(AboutUs.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(twitterurl)){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(twitterurl));
                    startActivity(i);
                }else {
                    Toast.makeText(AboutUs.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(youtubeurl)){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(youtubeurl));
                    startActivity(i);
                }else {
                    Toast.makeText(AboutUs.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
                }

            }
        });



        if (Common.limit != 1){


            setname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutUs.this);
                    adb.setMessage("Change Teacher name");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            databaseReference.child("name").setValue(val);
                        }
                    });
                    adb.setNegativeButton("No",null);
                    adb.show();
                }
            });

            setdesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutUs.this);
                    adb.setMessage("Change Teacher description");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            databaseReference.child("desc").setValue(val);
                        }
                    });
                    adb.setNegativeButton("No",null);
                    adb.show();
                }
            });



            Youtube.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutUs.this);
                    adb.setMessage("Set Youtube Link");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            databaseReference.child("youtube").setValue(val);
                        }
                    });
                    adb.setNegativeButton("No",null);
                    adb.show();

                    return false;
                }
            });

            Facebook.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutUs.this);
                    adb.setMessage("Set Facebook Link");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            databaseReference.child("facebook").setValue(val);
                        }
                    });
                    adb.setNegativeButton("No",null);
                    adb.show();
                    return false;
                }
            });

            Twitter.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutUs.this);
                    adb.setMessage("Set Twitter Link");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            databaseReference.child("twitter").setValue(val);
                        }
                    });
                    adb.setNegativeButton("No",null);
                    adb.show();
                    return false;
                }
            });

            Web.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutUs.this);
                    adb.setMessage("Set Web URL");
                    View v1 = getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e = (EditText) v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val = e.getText().toString();
                            databaseReference.child("web").setValue(val);
                        }
                    });
                    adb.setNegativeButton("No", null);
                    adb.show();

                    return false;
                }
            });

        }

    }


    public void setTeacherData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Teachers value = dataSnapshot.getValue(Teachers.class);
                fburl = value.getFacebook();
                youtubeurl = value.getYoutube();
                twitterurl = value.getTwitter();
                weburl = value.getWeb();

                if (!TextUtils.isEmpty(value.getName())) {
                    setname.setText(value.getName());
                }if (!TextUtils.isEmpty(value.getDesc())){
                    setdesc.setText(value.getDesc());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
