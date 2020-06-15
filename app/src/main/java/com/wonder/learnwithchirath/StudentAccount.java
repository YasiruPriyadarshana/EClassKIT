package com.wonder.learnwithchirath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.learnwithchirath.Object.Common;

public class StudentAccount extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button enroll;
    private EditText enrollkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        databaseReference = FirebaseDatabase.getInstance().getReference("EnrollmentKey/");
        enroll=(Button)findViewById(R.id.enroll_btn);
        enrollkey=(EditText)findViewById(R.id.enroll_txt);

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=enrollkey.getText().toString();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String uid=dataSnapshot.child(key).getValue().toString();
                        Common.uid=uid;
                        Common.limit=1;

                        Intent intent= new Intent(StudentAccount.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        viewCourses();
    }

    private void viewCourses(){

    }
}
