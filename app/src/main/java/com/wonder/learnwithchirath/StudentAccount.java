package com.wonder.learnwithchirath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.learnwithchirath.Adpter.ListAdapterMyCourses;
import com.wonder.learnwithchirath.Adpter.ListAdpterVideo;
import com.wonder.learnwithchirath.Object.Common;
import com.wonder.learnwithchirath.Object.Course;
import com.wonder.learnwithchirath.Object.UploadVideo;
import com.wonder.learnwithchirath.video.VideoPlayer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StudentAccount extends AppCompatActivity {

    private DatabaseReference databaseReference,databaseRefCourse;
    private Button enroll;
    private EditText enrollkey;
    private ArrayList<Course> courses;
    private ListAdapterMyCourses adapter;
    private ListView CourseListView;
    private  String stKey;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        readFile();

        databaseReference = FirebaseDatabase.getInstance().getReference("EnrollmentKey/");
        databaseRefCourse =FirebaseDatabase.getInstance().getReference("student/"+stKey+"/courses");
        enroll=(Button)findViewById(R.id.enroll_btn);
        enrollkey=(EditText)findViewById(R.id.enroll_txt);

        courses=new ArrayList<>();
        CourseListView = (ListView)findViewById(R.id.listView_Courses);

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=enrollkey.getText().toString();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        uid=dataSnapshot.child(key).getValue().toString();
                        addCourses(uid);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        CourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courses.get(position);
                Common.uid=course.getUid();
                Common.limit=1;
                Intent intent = new Intent(StudentAccount.this, MainActivity.class);
                startActivity(intent);
            }
        });

        viewCourses();
    }

    private void viewCourses(){
        databaseRefCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Course course = postSnapshot.getValue(Course.class);
                    courses.add(course);
                }


                adapter = new ListAdapterMyCourses(StudentAccount.this,R.layout.itemcourses,courses);


                CourseListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addCourses(String uid){
        Course course = new Course("IT","Teacher",uid);
        databaseRefCourse.child(databaseRefCourse.push().getKey()).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(StudentAccount.this, "Enrollment success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void readFile() {
        try {
            FileInputStream fileInputStream = openFileInput("student.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();


            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines + "\n");
            }
            String str = stringBuffer.toString();
            String[] array = str.split(",");
            stKey=array[0];
            Toast.makeText(this, "s: "+stKey, Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
