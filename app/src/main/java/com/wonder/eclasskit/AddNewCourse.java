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


import java.util.ArrayList;

public class AddNewCourse extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView QuizHometListView;
    private ListAdapterAddClass adapter;
    private ArrayList<AddCourse> addCourses;
    private View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers1");
        QuizHometListView=(ListView)findViewById(R.id.add_newclass_listview);
        QuizHometListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AddNewCourse.this,MainActivity.class);
                startActivity(intent);
            }
        });
        addCourses=new ArrayList<>();
        viewAllFiles();
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AddCourse addCourse = postSnapshot.getValue(AddCourse.class);
                    addCourses.add(addCourse);
                    String mkey = postSnapshot.getKey();

                }

                adapter = new ListAdapterAddClass(AddNewCourse.this, R.layout.itemaddnewclass, addCourses,null);

                if (QuizHometListView.getFooterViewsCount() > 0)
                {
                    QuizHometListView.removeFooterView(v);
                }
                v = getLayoutInflater().inflate(R.layout.footerviewaddnewclass, null);
                QuizHometListView.addFooterView(v);


                Button updatequiz = (Button) v.findViewById(R.id.addnewcourse);
                final EditText name=(EditText)v.findViewById(R.id.sub_name);
                final EditText year=(EditText)v.findViewById(R.id.class_year);

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
        databaseReference.child(databaseReference.push().getKey()).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewCourse.this, "New Course Added", Toast.LENGTH_SHORT).show();
                adapter.clear();
            }
        });
    }
}
