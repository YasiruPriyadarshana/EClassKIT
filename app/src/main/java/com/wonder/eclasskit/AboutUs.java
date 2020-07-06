package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.wonder.eclasskit.Object.UploadPDF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import in.gauriinfotech.commons.Commons;

public class AboutUs extends AppCompatActivity {
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

            teacherimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(AboutUs.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectIMG();
                    } else {
                        ActivityCompat.requestPermissions(AboutUs.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
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

                                Toast.makeText(AboutUs.this, "Profile uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else{
                        Toast.makeText(AboutUs.this, "Select picture", Toast.LENGTH_SHORT).show();
                    }
                    teacherimage.setClickable(true);
                    return true;
                }
            });




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
                String url = value.getImgurl();

                if (!TextUtils.isEmpty(url)) {
                    Picasso.with(AboutUs.this).load(url).into(teacherimage);
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

    private void selectIMG(){
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("crop", "true");
        pickImageIntent.putExtra("outputX", 400);
        pickImageIntent.putExtra("outputY", 400);
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
//        pickImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        pickImageIntent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, 76);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==76 && resultCode == RESULT_OK && data!=null) {
            imgUri=data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(AboutUs.this.getContentResolver(),imgUri);
                Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
                teacherimage.setImageBitmap(circleBitmap);

                teacherimage.setDrawingCacheEnabled(true);
                teacherimage.buildDrawingCache();
                Bitmap bitmap1 = teacherimage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data1 = baos.toByteArray();
                settodb=true;

            }catch (IOException e){
                Toast.makeText(AboutUs.this, "Error: "+e, Toast.LENGTH_SHORT).show();
            }



        }
    }
}
