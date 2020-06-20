package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.wonder.eclasskit.Adpter.ListAdapterAddClass;
import com.wonder.eclasskit.Object.AddCourse;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Teachers;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AddNewCourse extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView QuizHometListView;
    private ListAdapterAddClass adapter;
    private ArrayList<AddCourse> addCourses;
    private View v;
    private String tKey;
    private ArrayList<String> keys;
    private EditText course,year;
    private Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uid+"/newcourse");
        QuizHometListView=(ListView)findViewById(R.id.add_newclass_listview);
        QuizHometListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = keys.get(position);
                Common.uid=key;
                AddCourse adc=addCourses.get(position);
                Intent intent=new Intent(AddNewCourse.this,MainActivity.class);
                intent.putExtra("year",adc.getClass_yr());
                intent.putExtra("sub",adc.getSubjectname());
                startActivity(intent);
            }
        });
        addCourses=new ArrayList<>();
        viewAllFiles();

        save=(Button)findViewById(R.id.save_ftcourse);
        course=(EditText)findViewById(R.id.sub_name_1);
        year=(EditText)findViewById(R.id.class_yr_1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uid);
                Teachers teachers=new Teachers("",course.getText().toString(),year.getText().toString());
                databaseReference.setValue(teachers);
                Toast.makeText(AddNewCourse.this, "Details added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys=new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AddCourse addCourse = postSnapshot.getValue(AddCourse.class);
                    addCourses.add(addCourse);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                }

                adapter = new ListAdapterAddClass(AddNewCourse.this, R.layout.itemaddnewclass, addCourses);


                if (QuizHometListView.getFooterViewsCount() > 0)
                {
                    QuizHometListView.removeFooterView(v);
                }
                v = getLayoutInflater().inflate(R.layout.footerviewaddnewclass, null);
                QuizHometListView.addFooterView(v);


                Button updatequiz = (Button) v.findViewById(R.id.addnewcourse);
                EditText name=(EditText)v.findViewById(R.id.sub_name);
                EditText year=(EditText)v.findViewById(R.id.class_year);

                updatequiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name_st=name.getText().toString();
                        String year_st=year.getText().toString();
                        if (name_st.isEmpty() && year_st.isEmpty()){
                            Toast.makeText(AddNewCourse.this, "fill details", Toast.LENGTH_SHORT).show();
                        }else {
                            uplodeFile(name_st,year_st);
                        }
                    }
                });

                QuizHometListView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uplodeFile(String subname,String classyr) {
        AddCourse course=new AddCourse(subname,classyr);
        adapter.clear();
        databaseReference.child(databaseReference.push().getKey()).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewCourse.this, "New Course Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
