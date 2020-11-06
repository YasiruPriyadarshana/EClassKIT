package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.wonder.eclasskit.Adpter.ListAdapterAddClass;
import com.wonder.eclasskit.Object.AddCourse;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Teachers;
import java.util.ArrayList;

public class AddNewCourse extends AppCompatActivity implements ListAdapterAddClass.CallbackDelete{

    private DatabaseReference databaseReference,databaseReference2;
    private ListView QuizHometListView;
    private ListAdapterAddClass adapter;
    private ArrayList<AddCourse> addCourses;
    private View v;
    private String tKey;
    private ArrayList<String> keys;
    private EditText course,year;
    private Button save;
    private ListAdapterAddClass.CallbackDelete anInterface;
    private String tname;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        anInterface=this;

        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uidmain+"/newcourse");
        QuizHometListView=(ListView)findViewById(R.id.add_newclass_listview);
        QuizHometListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.uid= keys.get(position);
                AddCourse adc=addCourses.get(position);
                Intent intent=new Intent(AddNewCourse.this,MainActivity.class);
                intent.putExtra("year",adc.getClass_yr());
                intent.putExtra("sub",adc.getSubjectname());
                intent.putExtra("enroll","1");
                startActivity(intent);
            }
        });
        addCourses=new ArrayList<>();
        viewAllFiles();

        save=(Button)findViewById(R.id.save_ftcourse);
        course=(EditText)findViewById(R.id.sub_name_1);
        year=(EditText)findViewById(R.id.class_yr_1);

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uidmain+"/Main");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("subject")){
                    course.setText(dataSnapshot.child("subject").getValue().toString());
                    year.setText(dataSnapshot.child("syear").getValue().toString());

                }
                if (dataSnapshot.hasChild("name")){
                    tname =dataSnapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(course.getText().toString())){
                    Toast.makeText(AddNewCourse.this, "Course name is empty", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(year.getText().toString())){
                    Toast.makeText(AddNewCourse.this, "Course year is empty", Toast.LENGTH_SHORT).show();
                }else {
                    Teachers teachers=new Teachers(tname,course.getText().toString(),year.getText().toString());
                    databaseReference2.setValue(teachers);
                    Toast.makeText(AddNewCourse.this, "Details added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardView=(CardView)findViewById(R.id.card_maincourse);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.uid= Common.uidmain;
                Intent intent=new Intent(AddNewCourse.this,MainActivity.class);
                startActivity(intent);
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

                adapter = new ListAdapterAddClass(AddNewCourse.this, R.layout.itemaddnewclass, addCourses,keys,anInterface);


                if (QuizHometListView.getFooterViewsCount() > 0)
                {
                    QuizHometListView.removeFooterView(v);
                }
                v = getLayoutInflater().inflate(R.layout.footerviewaddnewclass, null);
                QuizHometListView.addFooterView(v);


                Button addnewcourse = (Button) v.findViewById(R.id.addnewcourse);
                EditText name=(EditText)v.findViewById(R.id.sub_name);
                EditText year=(EditText)v.findViewById(R.id.class_year);

                addnewcourse.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onHandledelete() {
        adapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
