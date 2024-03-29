package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Teachers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AboutTeacher extends AppCompatActivity {
    private ImageButton Web,Facebook,Twitter,Youtube,profilepic;
    private Button setname,setdesc;
    private DatabaseReference databaseReference;
    private String weburl,fburl,twitterurl,youtubeurl;
    private ImageButton teacherimage;
    private Uri imgUri;
    private StorageReference storage;
    private byte[] data1;
    private boolean settodb=false;

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
        teacherimage=(ImageButton)findViewById(R.id.teacher_img);

        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/"+ Common.uidmain+"/Main");

        setTeacherData();



        Web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(weburl)){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(weburl));
                    startActivity(i);
                }else {
                    Toast.makeText(AboutTeacher.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AboutTeacher.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AboutTeacher.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AboutTeacher.this, "Not Set By Teacher", Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (Common.limit != 1){

            teacherimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(AboutTeacher.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectIMG();
                    } else {
                        ActivityCompat.requestPermissions(AboutTeacher.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                    }
                }
            });

            teacherimage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    teacherimage.setClickable(false);
                    if (settodb) {
                        storage = FirebaseStorage.getInstance().getReference();
                        StorageReference reference = storage.child("Teacher/" + System.currentTimeMillis() + ".png");

                        reference.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete()) ;
                                Uri p = uri.getResult();
                                databaseReference.child("imgurl").setValue(p.toString());

                                Toast.makeText(AboutTeacher.this, "Profile uploaded", Toast.LENGTH_SHORT).show();
                                data1=null;
                                settodb=false;
                            }
                        });

                    } else{
                        Toast.makeText(AboutTeacher.this, "Select picture", Toast.LENGTH_SHORT).show();
                    }
                    teacherimage.setClickable(true);
                    return true;
                }
            });




            setname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            AboutTeacher.this);
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
                            AboutTeacher.this);
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
                            AboutTeacher.this);
                    adb.setMessage("Set Youtube Link");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            if (URLUtil.isValidUrl(val)){
                            databaseReference.child("youtube").setValue(val);}
                            else {
                                Toast.makeText(AboutTeacher.this, "Not a URL", Toast.LENGTH_SHORT).show();
                            }
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
                            AboutTeacher.this);
                    adb.setMessage("Set Facebook Link");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            if (URLUtil.isValidUrl(val)){
                            databaseReference.child("facebook").setValue(val);}
                            else {
                                Toast.makeText(AboutTeacher.this, "Not a URL", Toast.LENGTH_SHORT).show();
                            }
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
                            AboutTeacher.this);
                    adb.setMessage("Set Twitter Link");
                    View v1= getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e=(EditText)v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val=e.getText().toString();
                            if (URLUtil.isValidUrl(val)){
                            databaseReference.child("twitter").setValue(val);}
                            else {
                                Toast.makeText(AboutTeacher.this, "Not a URL", Toast.LENGTH_SHORT).show();
                            }
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
                            AboutTeacher.this);
                    adb.setMessage("Set Web URL");
                    View v1 = getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e = (EditText) v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val = e.getText().toString();
                            if (URLUtil.isValidUrl(val)){
                            databaseReference.child("web").setValue(val);}
                            else {
                                Toast.makeText(AboutTeacher.this, "Not a URL", Toast.LENGTH_SHORT).show();
                            }
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
                String url = value.getImgurl();

                if (!TextUtils.isEmpty(url)) {
                    Picasso.with(AboutTeacher.this).load(url).into(teacherimage);
                }

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


    @SuppressLint("IntentReset")
    private void selectIMG(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 360);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 76);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 76 && resultCode == RESULT_OK && data != null) {

            final Bundle extras = data.getExtras();

            if (extras != null) {
                Bitmap bitmap = extras.getParcelable("data");
                assert bitmap != null;
                Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle((float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2, (float) bitmap.getWidth() / 2, paint);
                teacherimage.setImageBitmap(circleBitmap);

                teacherimage.setDrawingCacheEnabled(true);
                teacherimage.buildDrawingCache();
                Bitmap bitmap1 = teacherimage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data1 = baos.toByteArray();
                settodb = true;
            }


        }
    }

}
