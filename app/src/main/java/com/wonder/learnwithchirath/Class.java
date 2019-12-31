package com.wonder.learnwithchirath;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.learnwithchirath.Adpter.ListAdapterTimetable;
import com.wonder.learnwithchirath.Firebase.FirebaseDatabaseHelper2;
import com.wonder.learnwithchirath.Object.Timetable;


import java.util.ArrayList;
import java.util.List;


public class Class extends Fragment {
    private DatabaseReference databaseReference;
    private ArrayList<Timetable> timetables;
    private ListView TimetableListView;
    private View v;
    private Spinner Category_day;
    private Spinner Category_grade;
    private Spinner Category_institute;
    private Spinner Category_class;
    private Button Addclassdet;
    private EditText time;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_class, container, false);

        // Inflate the layout for this fragment
        databaseReference = FirebaseDatabase.getInstance().getReference("timetable");
        TimetableListView=(ListView)v.findViewById(R.id.recyclerviewclass);
        timetables = new ArrayList<>();
        viewAllFiles();

        //add class data


        return v;
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Timetable ttable = postSnapshot.getValue(Timetable.class);
                    timetables.add(ttable);
                }


                ListAdapterTimetable adapter = new ListAdapterTimetable(getContext(),R.layout.itemclass,timetables);

                View v=getLayoutInflater().inflate(R.layout.footerviewclass, null);
                TimetableListView.addFooterView(v);
                Category_day = (Spinner)v.findViewById(R.id.category_day);
                Category_grade = (Spinner)v.findViewById(R.id.category_grade);
                Category_institute = (Spinner)v.findViewById(R.id.category_institute);
                Category_class = (Spinner)v.findViewById(R.id.category_class);
                Addclassdet=(Button)v.findViewById(R.id.addclassdet);
                time=(EditText)v.findViewById(R.id.time);

                Addclassdet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "class record has been inserted", Toast.LENGTH_SHORT).show();
                        Timetable timetable=new Timetable();
                        timetable.setDate(Category_day.getSelectedItem().toString());
                        timetable.setGrade(Category_grade.getSelectedItem().toString());
                        timetable.setTime(time.getText().toString());
                        timetable.setInstitute(Category_institute.getSelectedItem().toString());
                        timetable.setGcalss(Category_class.getSelectedItem().toString());

                        new FirebaseDatabaseHelper2().addClassDetails(timetable, new FirebaseDatabaseHelper2.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<Timetable> timetables, List<String> keys) {

                            }

                            @Override
                            public void DataIsInserted() {
                                Toast.makeText(getContext(), "class record has been inserted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });

                    }
                });


                TimetableListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
