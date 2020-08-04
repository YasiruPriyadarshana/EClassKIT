package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.Adpter.ListAdapterMyCourses;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Course;
import com.wonder.eclasskit.Object.Enroll;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StudentAccount extends AppCompatActivity implements ListAdapterMyCourses.CallbackDelete{

    private DatabaseReference databaseReference,databaseRefCourse;
    private Button enroll;
    private EditText enrollkey;
    private ArrayList<Course> courses;
    private ListAdapterMyCourses adapter;
    private ListView CourseListView;
    private  String stKey;
    private ArrayList<String> keys;
    private ListAdapterMyCourses.CallbackDelete anInterface;
    private ArrayList<String> oldEnroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);

        anInterface=this;

        enroll=(Button)findViewById(R.id.enroll_btn);
        enrollkey=(EditText)findViewById(R.id.enroll_txt);

        courses=new ArrayList<>();
        oldEnroll=new ArrayList<>();
        CourseListView = (ListView)findViewById(R.id.listView_Courses);

        String refkey=Common.studentregkey;
        if (TextUtils.isEmpty(refkey)){
            refkey=readFile();
        }



        databaseRefCourse =FirebaseDatabase.getInstance().getReference("student/"+refkey+"/courses");
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enroll.setEnabled(false);
                String key=enrollkey.getText().toString();

                if (TextUtils.isEmpty(key)){
                    Toast.makeText(StudentAccount.this, "Enter Enroll key", Toast.LENGTH_SHORT).show();
                }else {

                    databaseReference = FirebaseDatabase.getInstance().getReference("EnrollmentKey/" + key);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                addCourses(dataSnapshot.child("subject").getValue().toString(), dataSnapshot.child("tname").getValue().toString(), dataSnapshot.child("id").getValue().toString());
                                enrollkey.setText("");
                            }else {
                                Toast.makeText(StudentAccount.this, "Enroll key Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                enroll.setEnabled(true);
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
                keys = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Course course = postSnapshot.getValue(Course.class);
                    courses.add(course);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                    oldEnroll.add(course.getcName()+"a"+course.getcTeacher());
                }


                adapter = new ListAdapterMyCourses(StudentAccount.this,R.layout.itemcourses,courses,keys,stKey,anInterface);


                CourseListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addCourses(String cname,String tea,String uid){
        Course course = new Course(cname,tea,uid);

        if (!oldEnroll.contains(cname+"a"+tea)) {
            databaseRefCourse.child(databaseRefCourse.push().getKey()).setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(StudentAccount.this, "Enrollment success", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                }
            });
        }


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

    @Override
    public void onHandledelete() {
        adapter.clear();
    }
}
