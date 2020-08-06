package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.Adpter.ListAdapterTimetable;
import com.wonder.eclasskit.Firebase.FirebaseDatabaseHelper2;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.TimetableObj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeTable extends AppCompatActivity implements ListAdapterTimetable.CallbackInterface2{

    private DatabaseReference databaseReference;
    private ArrayList<TimetableObj> timetableObjs;
    private ListView TimetableListView;
    private View v,v1,v2;
    private Spinner Category_day;
    private Spinner Category_grade;
    private EditText Category_institute;
    private Spinner Category_class;
    private Button Addclassdet,sort,time;
    private EditText search;
    private ListAdapterTimetable.CallbackInterface2 anInterface;
    private int hour,minute,hour2,minute2;
    private int hourFinal,minuteFinal,hourFinal2,minuteFinal2;
    private int i=0;
    private ListAdapterTimetable adapter;
    private String tmp,ampm,ampm2;;
    private int e1,e2,e3,e4,e5,e6,e7=0;
    private ArrayList<TimetableObj> t1,t2,t3,t4,t5,t6,t7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        databaseReference = FirebaseDatabase.getInstance().getReference("timetable/"+ Common.uid);
        TimetableListView = (ListView) findViewById(R.id.recyclerviewclass);
        timetableObjs = new ArrayList<>();

        anInterface=this;

        viewAllFiles();


    }








    private void viewAllFiles() {



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (i==1){
                    timetableObjs.clear();
                    adapter.notifyDataSetChanged();
                }
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TimetableObj ttable = postSnapshot.getValue(TimetableObj.class);
//                    make list in order
                    tmp = ttable.getDate();
                    String mkey = postSnapshot.getKey();
                    if (tmp.equals("Monday")) {
                        if (e1 == 1) {
                            ttable.setDate("");
                        }
                        e1 = 1;
                    } else if (tmp.equals("Tuesday")) {
                        if (e2 == 2) {
                            ttable.setDate("");
                        }
                        e2 = 2;
                    } else if (tmp.equals("Wednesday")) {
                        if (e3 == 3) {
                            ttable.setDate("");
                        }
                        e3 = 3;
                    } else if (tmp.equals("Thursday")) {
                        if (e4 == 4) {
                            ttable.setDate("");
                        }
                        e4 = 4;
                    } else if (tmp.equals("Friday")) {
                        if (e5 == 5) {
                            ttable.setDate("");
                        }
                        e5 = 5;
                    } else if (tmp.equals("Saturday")) {
                        if (e6 == 6) {
                            ttable.setDate("");
                        }
                        e6 = 6;
                    } else if (tmp.equals("Sunday")) {
                        if (e7 == 7) {
                            ttable.setDate("");
                        }
                        e7 = 7;
                    }
                    timetableObjs.add(ttable);
                    keys.add(mkey);
                }


                adapter = new ListAdapterTimetable(TimeTable.this, R.layout.itemclass, timetableObjs, keys,anInterface);

                if (TimetableListView.getFooterViewsCount() > 0) {
                    TimetableListView.removeFooterView(v);
                }
                if (TimetableListView.getHeaderViewsCount() > 0) {
                    TimetableListView.removeHeaderView(v2);
                }


                v = getLayoutInflater().inflate(R.layout.footerviewclass, null);
                v2 = getLayoutInflater().inflate(R.layout.header_class, null);
                TimetableListView.addHeaderView(v2);
                if (Common.limit != 1) {
                    TimetableListView.addFooterView(v);
                }

                Category_day = (Spinner) v.findViewById(R.id.category_day);
                Category_grade = (Spinner) v.findViewById(R.id.category_grade);
                Category_institute = (EditText) v.findViewById(R.id.category_institute);
                Category_class = (Spinner) v.findViewById(R.id.category_class);
                Addclassdet = (Button) v.findViewById(R.id.addclassdet);

                time = (Button) v.findViewById(R.id.time);



                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar c=Calendar.getInstance();
                        TimePickerDialog timePickerDialog=new TimePickerDialog(TimeTable.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourFinal=hourOfDay;
                                ampm="AM";
                                if (hourOfDay>12){
                                    hourFinal=hourOfDay-12;
                                    ampm="PM";
                                }
                                minuteFinal=minute;

                                Calendar c=Calendar.getInstance();
                                TimePickerDialog timePickerDialog=new TimePickerDialog(TimeTable.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        hourFinal2=hourOfDay;
                                        ampm2="AM";
                                        if (hourOfDay>12){
                                            hourFinal2=hourOfDay-12;
                                            ampm2="PM";
                                        }
                                        minuteFinal2=minute;

                                        time.setText(""+hourFinal+"."+minuteFinal+" "+ampm+" - "+hourFinal2+"."+minuteFinal2+" "+ampm2);
                                    }
                                }, hour, minute, false);
                                timePickerDialog.show();

                            }
                        }, hour, minute, false);
                        timePickerDialog.show();
                    }
                });



                Addclassdet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(Category_institute.getText().toString())) {
                            Toast.makeText(TimeTable.this, "Institute is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TimeTable.this, "class record has been inserted", Toast.LENGTH_SHORT).show();
                            TimetableObj timetableObj = new TimetableObj();
                            timetableObj.setDate(Category_day.getSelectedItem().toString());
                            timetableObj.setGrade(Category_grade.getSelectedItem().toString());
                            timetableObj.setTime(time.getText().toString());
                            timetableObj.setInstitute(Category_institute.getText().toString());
                            timetableObj.setGcalss(Category_class.getSelectedItem().toString());


                            if (time.getText().toString().isEmpty()) {
                                Toast.makeText(TimeTable.this, "Enter time", Toast.LENGTH_SHORT).show();
                            } else {


                                new FirebaseDatabaseHelper2().addClassDetails(timetableObj, new FirebaseDatabaseHelper2.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<TimetableObj> timetableObjs, List<String> keys) {

                                    }

                                    @Override
                                    public void DataIsInserted() {

                                        Toast.makeText(TimeTable.this, "class record has been inserted", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void DataIsUpdated() {

                                    }

                                    @Override
                                    public void DataIsDeleted() {

                                    }
                                });
                                timetableObjs.clear();
                                adapter.notifyDataSetChanged();
                                e1 = 0;
                                e2 = 0;
                                e3 = 0;
                                e4 = 0;
                                e5 = 0;
                                e6 = 0;
                                e7 = 0;
                            }


                        }
                    }
                });
                search=(EditText)v2.findViewById(R.id.search_box);
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                TimetableListView.setAdapter(adapter);
                i=1;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onHandleSelection2() {
        timetableObjs.clear();
        adapter.notifyDataSetChanged();
        e1 = 0;
        e2 = 0;
        e3 = 0;
        e4 = 0;
        e5 = 0;
        e6 = 0;
        e7 = 0;

    }




}
