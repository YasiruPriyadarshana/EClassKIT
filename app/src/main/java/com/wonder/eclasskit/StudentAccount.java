package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.Adpter.ListAdapterMyCourses;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Course;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        databaseReference = FirebaseDatabase.getInstance().getReference("EnrollmentKey/");

        enroll=(Button)findViewById(R.id.enroll_btn);
        enrollkey=(EditText)findViewById(R.id.enroll_txt);

        courses=new ArrayList<>();
        CourseListView = (ListView)findViewById(R.id.listView_Courses);

        databaseRefCourse =FirebaseDatabase.getInstance().getReference("student/"+readFile()+"/courses");
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enroll.setEnabled(false);
                String key=enrollkey.getText().toString();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        addCourses(dataSnapshot.child(key).getValue().toString());
                        enroll.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        enroll.setEnabled(true);
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
        adapter.clear();
    }

    public String readFile() {
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


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stKey;
    }

}
