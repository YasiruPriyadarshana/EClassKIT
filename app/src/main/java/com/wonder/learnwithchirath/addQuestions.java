package com.wonder.learnwithchirath;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wonder.learnwithchirath.Object.Eventobj;
import com.wonder.learnwithchirath.Object.Quizobj;

import java.io.IOException;



public class addQuestions extends AppCompatActivity {
    private ImageButton selectimage;
    private Uri imgUri;
    private Button addquest;
    private EditText quest,ans1,ans2,ans3,ans4,ans5;
    private String quest_st,ans1_st,ans2_st,ans3_st,ans4_st,ans5_st,type,keyname;
    private CheckBox a1,a2,a3,a4,a5;
    private Spinner questCategory_spinner;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);
        Intent intent=getIntent();
        keyname=intent.getStringExtra("key");
        databaseReference = FirebaseDatabase.getInstance().getReference("quizHome/"+keyname+"/quiz");
        storage= FirebaseStorage.getInstance().getReference();

        questCategory_spinner = (Spinner)findViewById(R.id.categoryquest_spin);
        addquest=(Button)findViewById(R.id.addnew_quest);
        quest=(EditText)findViewById(R.id.add_quest);
        ans1=(EditText)findViewById(R.id.add_ans1);
        ans2=(EditText)findViewById(R.id.add_ans2);
        ans3=(EditText)findViewById(R.id.add_ans3);
        ans4=(EditText)findViewById(R.id.add_ans4);
        ans5=(EditText)findViewById(R.id.add_ans5);
        a1=(CheckBox)findViewById(R.id.checkBox1);
        a2=(CheckBox)findViewById(R.id.checkBox2);
        a3=(CheckBox)findViewById(R.id.checkBox3);
        a4=(CheckBox)findViewById(R.id.checkBox4);
        a5=(CheckBox)findViewById(R.id.checkBox5);

        selectimage = (ImageButton)findViewById(R.id.question_img_btn);
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(addQuestions.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectIMG();
                } else {
                    ActivityCompat.requestPermissions(addQuestions.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        addquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quest_st=quest.getText().toString();
                if (a1.isChecked()){
                    ans1_st="1";
                }else ans1_st="0";
                if (a2.isChecked()){
                    ans2_st="1";
                }else ans2_st="0";
                if (a3.isChecked()){
                    ans3_st="1";
                }else ans3_st="0";
                if (a4.isChecked()){
                    ans4_st="1";
                }else ans4_st="0";
                if (a5.isChecked()){
                    ans5_st="1";
                    ans5_st=ans5_st+ans5.getText().toString();
                }else if(!ans5.getText().toString().isEmpty()){
                    ans5_st="0";
                    ans5_st=ans5_st+ans5.getText().toString();
                }

                ans1_st=ans1_st+ans1.getText().toString();
                ans2_st=ans2_st+ans2.getText().toString();
                ans3_st=ans3_st+ans3.getText().toString();
                ans4_st=ans4_st+ans4.getText().toString();

                type= questCategory_spinner.getSelectedItem().toString();
                if (quest_st.isEmpty() || ans1_st.isEmpty() || ans2_st.isEmpty()){
                    Toast.makeText(addQuestions.this, "fill details", Toast.LENGTH_SHORT).show();
                }else {
                    uplodeFile(imgUri);
                }
            }
        });
    }


    private void selectIMG() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 76);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectIMG();
        }else {
            Toast.makeText(addQuestions.this, "please provide permission", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==76 && resultCode == RESULT_OK && data!=null) {
            imgUri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(addQuestions.this.getContentResolver(),imgUri);
                selectimage.setImageBitmap(bitmap);
            }catch (IOException e){
                Toast.makeText(addQuestions.this, "fucked: "+e, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(addQuestions.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uplodeFile(final Uri imgUri) {
        if (imgUri == null) {
            Quizobj quizobj = new Quizobj(quest_st,null, ans1_st, ans2_st, ans3_st, ans4_st, ans5_st, type);
            databaseReference.child(databaseReference.push().getKey()).setValue(quizobj);
            Toast.makeText(addQuestions.this, "Quiz Details uploaded", Toast.LENGTH_SHORT).show();

        } else {
            final ProgressDialog progressDialog = new ProgressDialog(addQuestions.this);
            progressDialog.setTitle("Uploading File");
            progressDialog.show();

            //imageuploade
            StorageReference reference2 = storage.child("Question/" + System.currentTimeMillis() + ".png");
            reference2.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uri.isComplete()) ;
                    Uri p = uri.getResult();

                    Quizobj quizobj = new Quizobj(quest_st, p.toString(), ans1_st, ans2_st, ans3_st, ans4_st, ans5_st, type);

                    databaseReference.child(databaseReference.push().getKey()).setValue(quizobj);
                    Toast.makeText(addQuestions.this, "Quiz Details uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    //pdf uplode

                    //end
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double currentProgress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded: " + (int) currentProgress + "%");

                }
            });//end

        }
    }
}
